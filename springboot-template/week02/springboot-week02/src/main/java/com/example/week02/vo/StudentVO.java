package com.example.week02.vo;

import lombok.Builder;
import lombok.Data;
import com.example.week02.constant.GenderEnum;

import java.time.LocalDateTime;


@Data
@Builder
public class StudentVO {
    private Long id;
    private String name;
    private String mobile;
    private GenderEnum gender;
    private Boolean enabled;
    private LocalDateTime createTime;
}