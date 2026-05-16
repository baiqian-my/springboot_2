package org.example.week11.service;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.example.week11.dto.MailDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MailServiceTest {

    @Resource
    private MailService mailService;

    @Test
    void sendMail() {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setTo("1282608067@qq.com");
        mailDTO.setSubject("测试邮件");
        mailDTO.setContent("这是一封测试邮件");
        mailService.sendMail(mailDTO);
    }

    @Test
    void sendHtmlMail() throws Exception {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setTo("1282608067@qq.com");
        mailDTO.setSubject("测试HTML邮件");
        String htmlContent = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>" +
                "<h1 style='color: #333; text-align: center;'>HTML邮件测试</h1>" +
                "<p style='color: #666; line-height: 1.6;'>这是一封<strong style='color: #007bff;'>富文本HTML</strong>邮件，支持样式和格式。</p>" +
                "<div style='background-color: #e7f3ff; padding: 15px; border-left: 4px solid #007bff; margin: 20px 0;'>" +
                "<p style='margin: 0; color: #004085;'>✨ 这是高亮提示区域</p>" +
                "</div>" +
                "<p style='text-align: center;'><a href='#' style='background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>点击按钮</a></p>" +
                "</div>" +
                "</body>" +
                "</html>";
        mailDTO.setContent(htmlContent);
        mailService.sendHtmlMail(mailDTO);
    }

    @Test
    void sendAttachmentsMail() throws Exception {
        // 创建临时文件用于测试
        java.nio.file.Path tempDir = java.nio.file.Files.createTempDirectory("mail_test");
        java.nio.file.Path tempFile1 = tempDir.resolve("测试文件1.txt");
        java.nio.file.Path tempFile2 = tempDir.resolve("测试文件2.txt");

        // 写入测试内容
        java.nio.file.Files.write(tempFile1, "这是第一个附件文件的内容".getBytes());
        java.nio.file.Files.write(tempFile2, "这是第二个附件文件的内容".getBytes());

        try {
            MailDTO mailDTO = new MailDTO();
            mailDTO.setTo("1282608067@qq.com");
            mailDTO.setSubject("测试带附件的邮件");
            mailDTO.setContent("<h3>这是一封带附件的邮件</h3><p>请查收附件内容。</p>");
            // 使用临时文件路径
            mailDTO.setFilePaths(new String[]{
                    tempFile1.toAbsolutePath().toString(),
                    tempFile2.toAbsolutePath().toString()
            });
            mailService.sendAttachmentsMail(mailDTO);
        } finally {
            // 清理临时文件
            java.nio.file.Files.deleteIfExists(tempFile1);
            java.nio.file.Files.deleteIfExists(tempFile2);
            java.nio.file.Files.deleteIfExists(tempDir);
        }
    }

    @Test
    void sendBirthdayWishMail() throws Exception {
        String userName = "张三";
        MailDTO mailDTO = new MailDTO();
        mailDTO.setTo("1282608067@qq.com");
        mailDTO.setSubject("🎂 生日快乐！祝您幸福安康");

        String birthdayHtml = "<!DOCTYPE html>" +
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
                ".message { font-size: 16px; color: #666; line-height: 1.8; margin-bottom: 30px; }" +
                ".highlight { background: linear-gradient(135deg, #fa709a 0%, #fee140 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; font-weight: bold; }" +
                ".cake { font-size: 80px; margin: 20px 0; }" +
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
                "<p style='color: #999; font-size: 14px;'>感谢您一直以来的支持与信任，愿我们的服务能为您带来更多便利与惊喜！</p>" +
                "<a href='#' class='btn'>领取生日福利 🎁</a>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>此邮件由系统自动发送，请勿直接回复</p>" +
                "<p>© 2026 公司名称 版权所有</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        mailDTO.setContent(birthdayHtml);
        mailService.sendHtmlMail(mailDTO);
    }

    @Test
    void sendActivationMail() throws Exception {
        String userName = "张三";
        String activationLink = "https://example.com/activate?token=abc123xyz";
        MailDTO mailDTO = new MailDTO();
        mailDTO.setTo("1282608067@qq.com");
        mailDTO.setSubject("🎉 恭喜您注册成功！请激活您的账户");

        String activationHtml = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: 'Microsoft YaHei', Arial, sans-serif; background-color: #f5f7fa; margin: 0; padding: 20px; }" +
                ".container { max-width: 600px; margin: 0 auto; background: white; border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.08); overflow: hidden; }" +
                ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 40px 30px; text-align: center; }" +
                ".header h1 { color: white; margin: 0; font-size: 28px; font-weight: 600; }" +
                ".header p { color: rgba(255,255,255,0.9); margin: 10px 0 0 0; font-size: 16px; }" +
                ".content { padding: 40px 30px; }" +
                ".welcome { font-size: 20px; color: #333; margin-bottom: 20px; font-weight: 600; }" +
                ".welcome span { color: #667eea; }" +
                ".info { background-color: #f8f9ff; border-left: 4px solid #667eea; padding: 20px; margin: 25px 0; border-radius: 0 8px 8px 0; }" +
                ".info p { margin: 0; color: #555; line-height: 1.6; }" +
                ".btn-container { text-align: center; margin: 35px 0; }" +
                ".btn { display: inline-block; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 16px 50px; text-decoration: none; border-radius: 30px; font-weight: 600; font-size: 16px; box-shadow: 0 4px 15px rgba(102,126,234,0.4); transition: transform 0.2s; }" +
                ".btn:hover { transform: translateY(-2px); }" +
                ".link-box { background-color: #f5f7fa; padding: 15px; border-radius: 8px; word-break: break-all; font-size: 13px; color: #667eea; margin: 20px 0; }" +
                ".tips { color: #888; font-size: 14px; line-height: 1.6; }" +
                ".tips strong { color: #333; }" +
                ".footer { background-color: #f8f9fa; padding: 25px 30px; text-align: center; border-top: 1px solid #eee; }" +
                ".footer p { color: #999; font-size: 12px; margin: 5px 0; }" +
                ".social { margin-top: 15px; }" +
                ".social a { display: inline-block; margin: 0 10px; color: #667eea; text-decoration: none; font-size: 14px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>🎉 欢迎加入我们</h1>" +
                "<p>开启您的精彩之旅</p>" +
                "</div>" +
                "<div class='content'>" +
                "<div class='welcome'>您好，<span>" + userName + "</span> 👋</div>" +
                "<p style='color: #666; line-height: 1.8;'>感谢您注册我们的服务！您的账户已创建成功，只需一步即可激活并开始使用。</p>" +
                "<div class='info'>" +
                "<p>🔐 <strong>账户安全提示：</strong>请点击下方按钮或复制链接到浏览器中完成账户激活。该链接将在24小时后失效。</p>" +
                "</div>" +
                "<div class='btn-container'>" +
                "<a href='" + activationLink + "' class='btn'>立即激活账户</a>" +
                "</div>" +
                "<p style='color: #888; font-size: 14px; margin-bottom: 10px;'>如果按钮无法点击，请复制以下链接到浏览器地址栏：</p>" +
                "<div class='link-box'>" + activationLink + "</div>" +
                "<div class='tips'>" +
                "<p><strong>🔔 温馨提示：</strong></p>" +
                "<ul style='padding-left: 20px; color: #666;'>" +
                "<li>请确保使用您注册时的邮箱地址</li>" +
                "<li>如未进行注册操作，请忽略此邮件</li>" +
                "<li>如有任何问题，请联系客服支持</li>" +
                "</ul>" +
                "</div>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>此邮件由系统自动发送，请勿直接回复</p>" +
                "<p>© 2026 公司名称 版权所有</p>" +
                "<div class='social'>" +
                "<a href='#'>官网</a>" +
                "<a href='#'>帮助中心</a>" +
                "<a href='#'>联系我们</a>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        mailDTO.setContent(activationHtml);
        mailService.sendHtmlMail(mailDTO);
    }
}
