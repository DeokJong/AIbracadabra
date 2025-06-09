package com.aibracadabra.ai.memory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

/**
 * RedisChatMemory + InMemoryChatMemory 동시 통합 테스트
 */
@SpringBootTest
class RedisChatMemoryTest {

    private static final String KEY_PREFIX = "chat:mem";
    @Autowired
    @Qualifier("redisChatMemory")
    private ChatMemory redisChatMemory;
    @Autowired
    @Qualifier("inMemoryChatMemory")
    private ChatMemory inMemoryChatMemory;
    @Autowired
    private RedisTemplate<String, String> redis;

    private static List<Message> sampleThread() {
        return List.of(
                new SystemMessage("대화 규칙입니다."),
                new UserMessage("안녕하세요!"),
                new AssistantMessage("무엇을 도와드릴까요?"),
                new ToolResponseMessage(
                        List.of(new ToolResponseMessage.ToolResponse(
                                "tool-001", "function-call", "{ \"result\": \"42\" }"
                        ))
                )
        );
    }

    @AfterEach
    void cleanupAllConvs() {
        redis.keys(KEY_PREFIX + "*").forEach(redis::delete);
    }

    @Nested
    @DisplayName("1. 기본 동작 테스트")
    class BasicOperations {

        @Test
        @DisplayName("add → get 기본 round-trip & InMemory 비교")
        void addAndGet_basicRoundTrip() {
            String convId = UUID.randomUUID().toString();
            List<Message> original = sampleThread();

            // both memories
            redisChatMemory.add(convId, original);
            inMemoryChatMemory.add(convId, original);

            // get
            List<Message> redisOut = redisChatMemory.get(convId, 10);
            List<Message> memOut = inMemoryChatMemory.get(convId, 10);

            // Redis 자체 검증
            assertEquals(original.size(), redisOut.size());
            assertEquals(MessageType.USER, redisOut.get(1).getMessageType());

            assertLinesMatch(redisOut.toString().lines(), memOut.toString().lines());
            // InMemory vs Redis 비교
        }

        @Test
        @DisplayName("add 두 번 호출 – 순서 보존 & InMemory 비교")
        void addTwice_preserveOrder() {
            String convId = UUID.randomUUID().toString();
            List<Message> first = List.of(new UserMessage("A"));
            List<Message> second = List.of(new AssistantMessage("B"));

            redisChatMemory.add(convId, first);
            redisChatMemory.add(convId, second);
            inMemoryChatMemory.add(convId, first);
            inMemoryChatMemory.add(convId, second);

            List<Message> redisOut = redisChatMemory.get(convId, 10);
            List<Message> memOut = inMemoryChatMemory.get(convId, 10);

            // Redis 검증
            assertThat(redisOut).extracting(Message::getText)
                    .containsExactly("A", "B");
            // 비교
            assertLinesMatch(redisOut.toString().lines(), memOut.toString().lines());
        }

        @Test
        @DisplayName("lastN < 전체 길이 – 마지막 N개만 반환 & InMemory 비교")
        void get_lastN_subset() {
            String convId = UUID.randomUUID().toString();
            redisChatMemory.add(convId, sampleThread());
            inMemoryChatMemory.add(convId, sampleThread());

            List<Message> redisOut = redisChatMemory.get(convId, 2);
            List<Message> memOut = inMemoryChatMemory.get(convId, 2);

            // Redis 검증
            assertThat(redisOut).extracting(Message::getText)
                    .containsExactly("무엇을 도와드릴까요?", "");

            // 비교
            assertLinesMatch(redisOut.toString().lines(), memOut.toString().lines());
        }

        @Test
        @DisplayName("get 호출 전 아무 것도 없으면 빈 리스트 반환 & InMemory 비교")
        void get_noMessages_returnsEmpty() {
            String convId = UUID.randomUUID().toString();
            List<Message> redisOut = redisChatMemory.get(convId, 5);
            List<Message> memOut = inMemoryChatMemory.get(convId, 5);

            assertThat(redisOut).isEmpty();
            assertThat(memOut).isEmpty();
        }

        @Test
        @DisplayName("clear 후 get 해도 빈 리스트 & InMemory 비교")
        void clear_thenEmpty() {
            String convId = UUID.randomUUID().toString();
            redisChatMemory.add(convId, sampleThread());
            inMemoryChatMemory.add(convId, sampleThread());

            redisChatMemory.clear(convId);
            inMemoryChatMemory.clear(convId);

            assertThat(redisChatMemory.get(convId, 10)).isEmpty();
            assertThat(inMemoryChatMemory.get(convId, 10)).isEmpty();
        }

        @Test
        @DisplayName("다른 conversationId 간섭 없음 & InMemory 비교")
        void multiConversation_isolated() {
            redisChatMemory.add("A", List.of(new UserMessage("foo")));
            redisChatMemory.add("B", List.of(new UserMessage("bar")));
            inMemoryChatMemory.add("A", List.of(new UserMessage("foo")));
            inMemoryChatMemory.add("B", List.of(new UserMessage("bar")));

            assertThat(redisChatMemory.get("A", 1))
                    .singleElement().extracting(Message::getText).isEqualTo("foo");
            assertThat(redisChatMemory.get("B", 1))
                    .singleElement().extracting(Message::getText).isEqualTo("bar");

            assertLinesMatch(redisChatMemory.get("A", 1).toString().lines(), inMemoryChatMemory.get("A", 1).toString().lines());
            assertLinesMatch(redisChatMemory.get("B", 1).toString().lines(), inMemoryChatMemory.get("B", 1).toString().lines());
        }
    }

    @Nested
    @DisplayName("2. 잘못된 입력 처리 (InvalidInput)")
    class InvalidInput {
        @Test
        @DisplayName("conversationId가 null일 때 NPE 발생 & InMemory 비교")
        void nullConversationId_throws() {
            assertThatExceptionOfType(NullPointerException.class)
                    .isThrownBy(() -> redisChatMemory.add(null, sampleThread()));
            assertThatExceptionOfType(NullPointerException.class)
                    .isThrownBy(() -> inMemoryChatMemory.add(null, sampleThread()));

            assertThatExceptionOfType(NullPointerException.class)
                    .isThrownBy(() -> inMemoryChatMemory.get(null, 5));
            assertThatExceptionOfType(NullPointerException.class)
                    .isThrownBy(() -> redisChatMemory.get(null, 5));

            assertThatExceptionOfType(NullPointerException.class)
                    .isThrownBy(() -> inMemoryChatMemory.clear(null));
            assertThatExceptionOfType(NullPointerException.class)
                    .isThrownBy(() -> redisChatMemory.clear(null));
        }

        @Test
        @DisplayName("messages 리스트에 null 요소 포함 시 보존 또는 NPE & InMemory 비교")
        void messagesContainNull() {
            String id = UUID.randomUUID().toString();
            List<Message> list = new ArrayList<>();
            list.add(new UserMessage("ok"));
            list.add(null);

            redisChatMemory.add(id, list);
            inMemoryChatMemory.add(id, list);

            List<Message> redisOut = redisChatMemory.get(id, 2);
            List<Message> memOut = inMemoryChatMemory.get(id, 2);

            assertThat(redisOut).hasSize(2);
            assertThat(memOut).hasSize(2);

            assertThat(redisOut.get(1)).isNull();
            assertThat(memOut.get(1)).isNull();
        }

        @Test
        @DisplayName("messages 파라미터가 null이면 NPE & InMemory 비교")
        void add_nullList_throws() {
            String id = UUID.randomUUID().toString();
            assertThatExceptionOfType(NullPointerException.class)
                    .isThrownBy(() -> redisChatMemory.add(id, (Message) null));
            assertThatExceptionOfType(NullPointerException.class)
                    .isThrownBy(() -> inMemoryChatMemory.add(id, (Message) null));
        }

        @Test
        @DisplayName("clear 없는 conversationId 호출 안전 & InMemory 비교")
        void clear_nonexistent() {
            assertDoesNotThrow(() -> redisChatMemory.clear("no-such"));
            assertDoesNotThrow(() -> inMemoryChatMemory.clear("no-such"));

            assertThat(redisChatMemory.get("no-such", 10)).isEmpty();
            assertThat(inMemoryChatMemory.get("no-such", 10)).isEmpty();
        }

        @Test
        @DisplayName("lastN=MAX_VALUE 처리 & InMemory 비교")
        void lastN_maxValue() {
            String id = UUID.randomUUID().toString();
            redisChatMemory.add(id, sampleThread());
            inMemoryChatMemory.add(id, sampleThread());

            List<Message> redisOut = redisChatMemory.get(id, Integer.MAX_VALUE);
            List<Message> memOut = inMemoryChatMemory.get(id, Integer.MAX_VALUE);

            assertThat(redisOut).hasSize(memOut.size());
            assertLinesMatch(redisOut.toString().lines(), memOut.toString().lines());
        }
    }

    @Nested
    @DisplayName("3. 경계값 및 대용량 (BoundaryAndBulk)")
    class BoundaryAndBulk {
        @Test
        @DisplayName("lastN == 0 → 빈 리스트 & 비교")
        void lastN_zero() {
            String id = UUID.randomUUID().toString();
            redisChatMemory.add(id, sampleThread());
            inMemoryChatMemory.add(id, sampleThread());

            assertThat(redisChatMemory.get(id, 0)).isEmpty();
            assertThat(inMemoryChatMemory.get(id, 0)).isEmpty();
        }

        @Test
        @DisplayName("lastN < 0 → 빈 리스트 & 비교")
        void lastN_negative() {
            String id = UUID.randomUUID().toString();
            redisChatMemory.add(id, sampleThread());
            inMemoryChatMemory.add(id, sampleThread());

            assertThat(redisChatMemory.get(id, -5)).isEmpty();
            assertThat(inMemoryChatMemory.get(id, -5)).isEmpty();
        }

        @Test
        @DisplayName("lastN > size → 전체 반환 & 비교")
        void lastN_greaterThanSize() {
            String id = UUID.randomUUID().toString();
            redisChatMemory.add(id, sampleThread());
            inMemoryChatMemory.add(id, sampleThread());

            assertThat(redisChatMemory.get(id, 999)).hasSize(4);
            assertLinesMatch(redisChatMemory.get(id, 999).toString().lines(), inMemoryChatMemory.get(id, 999).toString().lines());
        }

        @Test
        @DisplayName("lastN == 1 → 맨 마지막 하나 & 비교")
        void lastN_one() {
            String id = UUID.randomUUID().toString();
            redisChatMemory.add(id, sampleThread());
            inMemoryChatMemory.add(id, sampleThread());

            assertThat(redisChatMemory.get(id, 1))
                    .extracting(Message::getText).containsExactly("");

            assertLinesMatch(redisChatMemory.get(id, 1).toString().lines(), inMemoryChatMemory.get(id, 1).toString().lines());
        }

        @Test
        @DisplayName("대량 메시지 처리(10k) & 비교")
        void bulkInsert_performance() {
            String id = UUID.randomUUID().toString();
            List<Message> bulk = new ArrayList<>();
            IntStream.range(0, 10_000)
                    .forEach(i -> bulk.add(new UserMessage("m" + i)));

            redisChatMemory.add(id, bulk);
            inMemoryChatMemory.add(id, bulk);

            assertThat(redisChatMemory.get(id, 10_000)).hasSize(10_000);
            assertThat(inMemoryChatMemory.get(id, 10_000)).hasSize(10_000);

            assertLinesMatch(redisChatMemory.get(id, 10_000).toString().lines(), inMemoryChatMemory.get(id, 10_000).toString().lines());
        }
    }

    @Nested
    @DisplayName("4. 동시성 테스트 (Concurrency)")
    class Concurrency {
        @Test
        @DisplayName("여러 스레드 동시 add – 누락 없이 저장 & 비교 크기만")
        void concurrentAdds() throws InterruptedException {
            String id = UUID.randomUUID().toString();
            int threads = 8, perThread = 500;
            ExecutorService exec = Executors.newFixedThreadPool(threads);
            CountDownLatch start = new CountDownLatch(1);
            Runnable taskRedis = () -> {
                try {
                    start.await();
                    List<Message> msgs = new ArrayList<>();
                    for (int i = 0; i < perThread; i++)
                        msgs.add(new UserMessage("R-" + i));
                    redisChatMemory.add(id, msgs);
                } catch (Exception ignored) {
                }
            };
            Runnable taskMem = () -> {
                try {
                    start.await();
                    List<Message> msgs = new ArrayList<>();
                    for (int i = 0; i < perThread; i++)
                        msgs.add(new UserMessage("M-" + i));
                    inMemoryChatMemory.add(id, msgs);
                } catch (Exception ignored) {
                }
            };
            for (int i = 0; i < threads; i++) {
                exec.submit(taskRedis);
                exec.submit(taskMem);
            }
            start.countDown();
            exec.shutdown();
            assertTrue(exec.awaitTermination(5, TimeUnit.SECONDS));

            // 크기만 비교 (순서 보장은 불가)
            assertThat(redisChatMemory.get(id, threads * perThread))
                    .hasSize(inMemoryChatMemory.get(id, threads * perThread).size());
        }

        @Test
        @DisplayName("동시 add 와 clear 병행 호출 안전 & 비교")
        void concurrentAddAndClear() throws InterruptedException {
            String id = UUID.randomUUID().toString();
            ExecutorService exec = Executors.newCachedThreadPool();
            exec.submit(() -> {
                for (int i = 0; i < 1000; i++) {
                    redisChatMemory.add(id, List.of(new UserMessage("x" + i)));
                }
            });
            exec.submit(() -> {
                for (int i = 0; i < 100; i++) {
                    redisChatMemory.clear(id);
                }
            });
            exec.submit(() -> {
                for (int i = 0; i < 1000; i++) {
                    inMemoryChatMemory.add(id, List.of(new UserMessage("y" + i)));
                }
            });
            exec.submit(() -> {
                for (int i = 0; i < 100; i++) {
                    inMemoryChatMemory.clear(id);
                }
            });
            exec.shutdown();
            assertTrue(exec.awaitTermination(5, TimeUnit.SECONDS));

            // 호출 안전 여부만 확인
            assertDoesNotThrow(() -> {
                redisChatMemory.get(id, 10);
                inMemoryChatMemory.get(id, 10);
            });
        }
    }
}
