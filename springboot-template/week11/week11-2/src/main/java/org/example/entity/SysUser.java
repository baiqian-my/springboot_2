package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("sys_user")
public class SysUser {
    
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String realName;

    private String avatar;

    private Integer gender;

    private String email;

    private String mobile;
    

    private Integer status;
    

    @TableField("super_admin")
    private Integer superAdmin;
    

    @TableLogic
    private Integer deleted;
    

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}