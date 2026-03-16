package com.example.week02.dto;

import com.example.week02.constant.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentAddDTO {
    private String name;
    private String avatar;
    private String mobile;
    private GenderEnum gender;
    private LocalDate birthday;
}

