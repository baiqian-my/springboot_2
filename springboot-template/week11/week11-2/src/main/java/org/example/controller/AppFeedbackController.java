package org.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.Result;
import org.example.entity.AppFeedback;
import org.example.service.AppFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/app/feedback")
public class AppFeedbackController {
    
    @Autowired
    private AppFeedbackService appFeedbackService;

    @PostMapping
    public Result<Void> submit(@RequestBody AppFeedback feedback) {
        feedback.setStatus(0); // 待处理状态
        appFeedbackService.save(feedback);
        return Result.success();
    }
    

    @GetMapping("/list")
    public Result<List<AppFeedback>> list() {
        List<AppFeedback> list = appFeedbackService.list();
        return Result.success(list);
    }
    

    @GetMapping("/page")
    public Result<Page<AppFeedback>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<AppFeedback> page = appFeedbackService.page(new Page<>(current, size));
        return Result.success(page);
    }

    @PutMapping("/{id}/reply")
    public Result<Void> reply(@PathVariable Long id, @RequestBody ReplyDTO replyDTO) {
        AppFeedback feedback = appFeedbackService.getById(id);
        if (feedback == null) {
            return Result.error("留言不存在");
        }
        feedback.setReply(replyDTO.getReply());
        feedback.setStatus(1); // 已回复状态
        appFeedbackService.updateById(feedback);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        appFeedbackService.removeById(id);
        return Result.success();
    }
    

    public static class ReplyDTO {
        private String reply;
        
        public String getReply() {
            return reply;
        }
        
        public void setReply(String reply) {
            this.reply = reply;
        }
    }
}