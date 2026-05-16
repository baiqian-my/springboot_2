package org.example.week11.job;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.week11.dto.MailDTO;
import org.example.week11.entity.User;
import org.example.week11.service.MailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mqxu
 * @date 2026/5/15
 * @description ScheduledJobs
 * 每年5月15日下午3点35分发送生日祝福邮件
 */
@Slf4j
@Component
public class ScheduledJobs {

    @Resource
    private MailService mailService;


    @Scheduled(cron = "0 40 15 15 5 ?")
    public void sendBirthdayWish() {
        log.info("========== 开始执行生日祝福邮件定时任务 ==========");
        log.info("当前时间：{}", getCurrentTime());

        // 获取今天过生日的用户列表（实际应用中从数据库查询）
        List<User> birthdayUsers = getTodayBirthdayUsers();

        if (birthdayUsers.isEmpty()) {
            log.info("今天没有用户过生日，任务结束");
            return;
        }

        log.info("今天有 {} 位用户过生日", birthdayUsers.size());

        // 遍历发送邮件
        for (User user : birthdayUsers) {
            try {
                sendBirthdayMail(user);
                log.info("✅ 成功发送生日祝福邮件给：{}（{}）", user.getName(), user.getEmail());
            } catch (Exception e) {
                log.error("❌ 发送生日祝福邮件失败：{}（{}），错误：{}",
                        user.getName(), user.getEmail(), e.getMessage());
            }
        }

        log.info("========== 生日祝福邮件定时任务执行完毕 ==========");
    }

    /**
     * 发送生日祝福邮件
     *
     * @param user 用户信息
     * @throws Exception 发送异常
     */
    private void sendBirthdayMail(User user) throws Exception {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setTo(user.getEmail());
        mailDTO.setSubject("🎂 生日快乐！" + user.getName() + "，祝您幸福安康");

        String birthdayHtml = buildBirthdayHtml(user.getName());
        mailDTO.setContent(birthdayHtml);

        mailService.sendHtmlMail(mailDTO);
    }

    /**
     * 构建生日祝福邮件HTML内容
     *
     * @param userName 用户名
     * @return HTML字符串
     */
    private String buildBirthdayHtml(String userName) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: 'Microsoft YaHei', Arial, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); margin: 0; padding: 0; }" +
                ".container { max-width: 600px; margin: 0 auto; background: white; border-radius: 20px; overflow: hidden; box-shadow: 0 20px 60px rgba(0,0,0,0.3); }" +
                ".header { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); padding: 40px; text-align: center; }" +
                ".header h1 { color: white; margin: 0; font-size: 36px; text-shadow: 2px 2px 4px rgba(0,0,0,0.2); }" +
                ".content { padding: 40px; text-align: center; }" +
                ".user-name { font-size: 28px; color: #333; margin-bottom: 20px; }" +
                ".highlight { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; font-weight: bold; }" +
                ".cake { font-size: 80px; margin: 20px 0; }" +
                ".message { font-size: 16px; color: #666; line-height: 1.8; margin-bottom: 30px; }" +
                ".footer { background-color: #f8f9fa; padding: 20px; text-align: center; color: #999; font-size: 12px; }" +
                ".btn { display: inline-block; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px 40px; text-decoration: none; border-radius: 30px; font-weight: bold; margin-top: 20px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>🎉 生日快乐 🎉</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<div class='cake'>🎂</div>" +
                "<div class='user-name'>亲爱的 <span class='highlight'>" + userName + "</span></div>" +
                "<div class='message'>" +
                "<p>在这个特别的日子里，我们衷心祝愿您：</p>" +
                "<p><span class='highlight'>✨ 岁岁平安，年年如意 ✨</span></p>" +
                "<p><span class='highlight'>🌟 心想事成，万事顺遂 🌟</span></p>" +
                "<p><span class='highlight'>💫 前程似锦，幸福安康 💫</span></p>" +
                "</div>" +
                "<p style='color: #999; font-size: 14px;'>感谢您一直以来的支持与信任！</p>" +
                "<a href='#' class='btn'>领取生日福利 🎁</a>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>此邮件由系统自动发送，请勿直接回复</p>" +
                "<p>© 2026 公司名称 版权所有</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    /**
     * 模拟获取今天过生日的用户（实际应从数据库查询）
     *
     * @return 用户列表
     */
    private List<User> getTodayBirthdayUsers() {
        List<User> users = new ArrayList<>();
        
        // 模拟数据：假设今天是5月15日，这些用户今天生日
        users.add(new User("张三", "1282608067@qq.com", "05-15"));
        users.add(new User("李四", "1282608067@qq.com", "05-15"));
        users.add(new User("王五", "1282608067@qq.com", "05-15"));
        
        // 实际应用中应该：
        // 1. 从数据库查询所有用户
        // 2. 筛选出生日为今天（05-15）的用户
        // String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd"));
        // return userRepository.findByBirthday(today);
        
        return users;
    }

    /**
     * 获取当前时间字符串
     *
     * @return 格式化后的时间
     */
    private String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}