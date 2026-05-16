package org.example.week11.dto;

import lombok.Data;


@Data
public class MailDTO {
    private String to;
    private String subject;
    private String content;
    private String[] filePaths;
}
