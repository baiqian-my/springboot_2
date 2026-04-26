package org.example.week07.article.hot;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.example.week07.article.hot.dto.HotArticleView;
import org.example.week07.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HotArticleService {

    private final RedisUtil redisUtil;

public final class ArticleRedisKey {

    public static final String HOT_RANK_KEY = "week08:article:hot:rank";

    private ArticleRedisKey() {
    }
}
    public List<HotArticleView> top(int limit) {
        int safeLimit = Math.max(limit, 1);
        Set<ZSetOperations.TypedTuple<Object>> tuples =
                redisUtil.zRevRangeWithScores(ArticleRedisKey.HOT_RANK_KEY, 0, safeLimit - 1L);
        List<HotArticleView> result = new ArrayList<>();
        if (tuples == null || tuples.isEmpty()) {
            return result;
        }
        for (ZSetOperations.TypedTuple<Object> tuple : tuples) {
            if (tuple == null || tuple.getValue() == null || tuple.getScore() == null) {
                continue;
            }
            result.add(new HotArticleView(tuple.getValue().toString(), tuple.getScore()));
        }
        return result;
    }
}
