package uz.pdp.linkedin.Config;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    public Map<String,String> verifycode=new HashMap<>();

    public void sendVerificationCode(String email) throws MessagingException {
        String code = generateRandomCode(6);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("Your Verification Code");

        String htmlContent = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: Arial, sans-serif;
                    background-color: #f4f4f4;
                    margin: 0;
                    padding: 0;
                }
                .container {
                    max-width: 600px;
                    margin: 20px auto;
                    background-color: #ffffff;
                    border-radius: 8px;
                    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                    padding: 20px;
                }
                .header {
                    text-align: center;
                    padding: 20px 0;
                    background-color: #007bff;
                    color: #ffffff;
                    border-radius: 8px 8px 0 0;
                }
                .header h1 {
                    margin: 0;
                    font-size: 24px;
                }
                .content {
                    padding: 20px;
                    text-align: center;
                }
                .code {
                    display: inline-block;
                    font-size: 32px;
                    font-weight: bold;
                    color: #007bff;
                    background-color: #f8f9fa;
                    padding: 10px 20px;
                    border-radius: 5px;
                    letter-spacing: 5px;
                    margin: 20px 0;
                }
                .footer {
                    text-align: center;
                    padding: 10px;
                    font-size: 12px;
                    color: #6c757d;
                }
                @media only screen and (max-width: 600px) {
                    .container {
                        width: 100%;
                        margin: 10px;
                    }
                    .code {
                        font-size: 24px;
                        padding: 8px 16px;
                    }
                }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>Email Verification</h1>
                </div>
                <div class="content">
                    <h2>Your Verification Code</h2>
                    <p>Please use the code below to verify your email address:</p>
                    <div class="code">%s</div>
                    <p>This code is valid for the next 10 minutes.</p>
                    <p>If you did not request this code, please ignore this email.</p>
                </div>
                <div class="footer">
                    <p>&copy; 2025 Your Company Name. All rights reserved.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(code);

        helper.setText(htmlContent, true);
        mailSender.send(message);
        verifycode.put(email, code);
        System.out.println("Verification code " + code + " sent to: " + email);
    }

    private String generateRandomCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
