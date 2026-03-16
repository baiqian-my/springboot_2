package com.example.week02.entity;

import com.example.week02.constant.GenderEnum;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author author1
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {
    private Long id;
    private String name;
    private String avatar;
    private String mobile;
    private GenderEnum gender;
    private LocalDate birthday;
    private Boolean enabled;
    private LocalDateTime createTime;

}
