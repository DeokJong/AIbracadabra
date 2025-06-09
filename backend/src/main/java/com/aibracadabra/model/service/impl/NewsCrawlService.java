package com.aibracadabra.model.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aibracadabra.model.dao.NewsDao;
import com.aibracadabra.model.dto.domain.News;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsCrawlService {

    private final NewsDao nDao;
    private static final String BASE = "https://www.chookjenews.kr/news/articleList.html";
    private static final String DETAIL_PREFIX = "https://www.chookjenews.kr/news/";

    /**
     * 1시간마다 실행
     */

    @Scheduled(initialDelay = 0, fixedRate = 60 * 60 * 1000)
    public void crawlAndSave() {
        log.info("CrawlAndSave 실행 ▶ " + LocalDateTime.now());
        for (int i = 4; i <= 12; i++) {
            String regionCode = "S2N" + i;
            String listUrl = BASE + "?sc_sub_section_code=" + regionCode + "&view_type=sm";
            try {
                Document doc = Jsoup.connect(listUrl)
                                    .userAgent("Mozilla/5.0")
                                    .get();

                // ① 썸네일 DIV만 선택
                Elements thumbs = doc.select("div.auto-article");
                log.info("[Crawler] items found ▶ " + thumbs.size());

                for (Element thumb : thumbs) {
                    // ② <div> → <td> 로 올라가서
                    Element thumbTd = thumb.parent(); // <td width="180"…>
                    //    그 다음 형제 <td> (정보가 들어있는 셀)
                    Element infoTd = thumbTd.nextElementSibling();
                    if (infoTd == null) continue;

                    // ③ infoTd 내부의 nested table에서 각 요소 추출
                    // 제목 & URL
                    Element titleA = infoTd.selectFirst("td.list-titles a");
                    if (titleA == null) continue;  // 안전장치
                    String title = titleA.text();
                    String url   = DETAIL_PREFIX + titleA.attr("href");

                    // 요약 (링크 안의 텍스트)
                    String summary = Optional.ofNullable(
                        infoTd.selectFirst("td.list-summary a"))
                      .map(Element::text)
                      .orElse("");

                    // 발행일 (“|” 뒷부분만)
                    String publishAt = Optional.ofNullable(
                        infoTd.selectFirst("td.list-times.list-pad-10"))
                      .map(Element::text)
                      .map(t -> {
                          String[] parts = t.split("\\|");
                          return parts.length > 1 ? parts[1].trim() : t.trim();
                      })
                      .orElse("");

                    int sidoCode = i; // 4~12 숫자 그대로

                    log.info("[Crawler] %s | %s | %d | %s%n",
                                      title, publishAt, sidoCode, url);

                    // 중복 검사 & 저장
                    if (nDao.selectByUrl(url) == null) {
                        News news = new News();
                        news.setTitle(title);
                        news.setUrl(url);
                        news.setSummary(summary);
                        news.setPublishAt(publishAt);
                        news.setSidoCode(sidoCode);
                        save(news);
                        log.info("[Crawler] 저장 완료.");
                    } else {
                    	
                    	log.info("[Crawler] 이미 존재, 스킵.");
                    	return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void save(News news) {
        nDao.insert(news);
    }
}
