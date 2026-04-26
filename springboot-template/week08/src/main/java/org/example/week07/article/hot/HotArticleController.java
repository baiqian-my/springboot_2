package org.example.week07.article.hot;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.example.week07.article.hot.dto.HotArticleView;
import org.example.week07.common.dto.ApiResult;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class HotArticleController {

    private final HotArticleService hotArticleService;

    @GetMapping("/hot")
    public ApiResult<List<HotArticleView>> hot(@RequestParam(defaultValue = "10") int limit) {
        return ApiResult.success(hotArticleService.top(limit));
    }
}
