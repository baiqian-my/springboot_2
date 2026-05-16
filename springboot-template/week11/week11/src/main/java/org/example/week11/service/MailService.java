package org.example.week11.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.week11.dto.MailDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(MailDTO mailDTO) {
        // 创建邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置收件人，寄件人
        message.setTo(mailDTO.getTo());
        message.setFrom(from);
        message.setSubject(mailDTO.getSubject());
        message.setText(mailDTO.getContent());
        // 发送邮件
        mailSender.send(message);
    }

    public void sendHtmlMail(MailDTO mailDTO) throws Exception {
        // 创建邮件消息
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(mailDTO.getTo());
        helper.setSubject(mailDTO.getSubject());
        // true 表示支持html
        helper.setText(mailDTO.getContent(), true);
        mailSender.send(message);
    }

    public void sendAttachmentsMail(MailDTO mailDTO) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        // 带附件第二个参数true
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(mailDTO.getTo());
        helper.setSubject(mailDTO.getSubject());
        helper.setText(mailDTO.getContent(), true);
        // 添加附件资源
        if (mailDTO.getFilePaths() != null) {
            for (String filePath : mailDTO.getFilePaths()) {
                FileSystemResource file = new FileSystemResource(new File(filePath));
                if (file.exists()) {
                    String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
                    helper.addAttachment(fileName, file);
                } else {
                    throw new IllegalArgumentException("附件文件不存在: " + filePath);
                }
            }
        }
        mailSender.send(message);
    }
}
