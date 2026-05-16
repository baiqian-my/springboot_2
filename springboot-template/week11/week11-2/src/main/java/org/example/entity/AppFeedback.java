package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("app_feedback")
public class AppFeedback {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    

    private Long userId;
    

    private String nickname;
    

    private String content;
    

    private String contact;
    

    private String reply;
    

    private Integer status;
    

    @TableLogic
    private Integer deleted;
    

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}