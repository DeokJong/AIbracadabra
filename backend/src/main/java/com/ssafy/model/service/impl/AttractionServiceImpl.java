package com.ssafy.model.service.impl;

import com.ssafy.model.dao.AttractionDao;
import com.ssafy.model.dto.domain.Attraction;
import com.ssafy.model.dto.domain.Filter;
import com.ssafy.model.dto.domain.Route;
import com.ssafy.model.service.AttractionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AttractionServiceImpl implements AttractionService {

	private final AttractionDao attractionDao;

	@Override
	public List<Attraction> getByFilter(Filter filter) {
		List<Attraction> list = attractionDao.getByFilter(filter);
		return list;
	}

	public List<Route> getRoute(List<Route> route, int[][] adjMat, int startId, int endId) {
		int N = route.size();
		int startIdx = -1, endIdx = -1;

		// 1. contentid 기준으로 index 찾기
		for (int i = 0; i < N; i++) {
			if (route.get(i).getContentid() == startId)
				startIdx = i;
			if (route.get(i).getContentid() == endId)
				endIdx = i;
			if (startIdx >= 0 && endIdx >= 0)
				break;
		}

		if (startIdx == -1 || endIdx == -1) {
			throw new IllegalArgumentException("출발지 또는 도착지를 찾을 수 없습니다.");
		}

		int[][] dp = new int[N][1 << N];
		int[][] parent = new int[N][1 << N];

		for (int[] row : dp)
			Arrays.fill(row, Integer.MAX_VALUE);
		for (int[] row : parent)
			Arrays.fill(row, -1);

		dp[startIdx][1 << startIdx] = 0;

		// 2. Held–Karp 알고리즘 (경로 or 순회 분기)
		for (int mask = 0; mask < (1 << N); mask++) {
			for (int u = 0; u < N; u++) {
				if ((mask & (1 << u)) == 0 || dp[u][mask] == Integer.MAX_VALUE)
					continue;

				for (int v = 0; v < N; v++) {
					if ((mask & (1 << v)) == 0) {
						int nextMask = mask | (1 << v);
						int newCost = dp[u][mask] + adjMat[u][v];

						if (newCost < dp[v][nextMask]) {
							dp[v][nextMask] = newCost;
							parent[v][nextMask] = u;
						}
					}
				}
			}
		}

		int finalMask = (1 << N) - 1;

		// 3. 경로 역추적
		LinkedList<Route> ordered = new LinkedList<>();
		int curr;
		int mask = finalMask;

		if (startIdx == endIdx) {
			// 순회 (TSP)
			// 가장 짧은 회로 비용 찾기 (시작점으로 되돌아오는 비용 포함)
			int minTotalCost = Integer.MAX_VALUE;
			int bestLast = -1;
			for (int i = 0; i < N; i++) {
				if (i == startIdx)
					continue;
				if (dp[i][finalMask] == Integer.MAX_VALUE)
					continue;
				int totalCost = dp[i][finalMask] + adjMat[i][startIdx];
				if (totalCost < minTotalCost) {
					minTotalCost = totalCost;
					bestLast = i;
				}
			}

			if (bestLast == -1)
				throw new RuntimeException("순회 경로를 찾을 수 없습니다.");

			curr = bestLast;
			while (curr != -1) {
				ordered.addFirst(route.get(curr));
				int prev = parent[curr][mask];
				mask ^= (1 << curr);
				curr = prev;
			}
		} else {
			// 단순 경로
			int minCost = dp[endIdx][finalMask];
			if (minCost == Integer.MAX_VALUE) {
				throw new RuntimeException("경로를 찾을 수 없습니다.");
			}

			curr = endIdx;
			while (curr != -1) {
				ordered.addFirst(route.get(curr));
				int prev = parent[curr][mask];
				mask ^= (1 << curr);
				curr = prev;
			}
		}

		// System.out.println("최적 경로 탐색 완료!");
		return ordered;
	}

}
