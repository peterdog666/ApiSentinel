package com.apisentinel.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.apisentinel.entity.MonitorApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * 钉钉告警工具类
 *
 * @author ApiSentinel Team
 */
@Slf4j
@Component
public class DingTalkUtil {

    @Value("${dingtalk.webhook.url:}")
    private String defaultWebhookUrl;

    @Value("${dingtalk.webhook.secret:}")
    private String defaultSecret;

    /**
     * 发送告警通知
     *
     * @param api                监控接口
     * @param consecutiveFailures 连续失败次数
     */
    public void sendAlert(MonitorApi api, Integer consecutiveFailures) {
        String webhookUrl = api.getDingtalkWebhook();
        String secret = api.getDingtalkSecret();

        // 如果接口没有配置，使用全局配置
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            webhookUrl = defaultWebhookUrl;
        }
        if (secret == null) {
            secret = defaultSecret;
        }

        if (webhookUrl == null || webhookUrl.isEmpty()) {
            log.warn("未配置钉钉Webhook，跳过告警发送");
            return;
        }

        try {
            // 生成签名
            Long timestamp = System.currentTimeMillis();
            String sign = generateSign(timestamp, secret);

            // 构建完整URL
            String url = webhookUrl;
            if (secret != null && !secret.isEmpty()) {
                url = webhookUrl + "&timestamp=" + timestamp + "&sign=" + sign;
            }

            // 构建告警消息
            String content = buildAlertMessage(api, consecutiveFailures);

            // 发送请求
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("Content-Type", "application/json; charset=utf-8");

                JSONObject message = new JSONObject();
                message.put("msgtype", "markdown");

                JSONObject markdown = new JSONObject();
                markdown.put("title", "API监控告警");
                markdown.put("text", content);
                message.put("markdown", markdown);

                httpPost.setEntity(new StringEntity(message.toJSONString(), ContentType.APPLICATION_JSON));

                try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                    String result = EntityUtils.toString(response.getEntity());
                    log.info("钉钉告警发送结果: {}", result);
                }
            }

        } catch (Exception e) {
            log.error("发送钉钉告警失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 生成钉钉签名
     */
    private String generateSign(Long timestamp, String secret) throws Exception {
        if (secret == null || secret.isEmpty()) {
            return "";
        }

        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        return URLEncoder.encode(Base64.getEncoder().encodeToString(signData), StandardCharsets.UTF_8);
    }

    /**
     * 构建告警消息
     */
    private String buildAlertMessage(MonitorApi api, Integer consecutiveFailures) {
        StringBuilder sb = new StringBuilder();
        sb.append("## API监控告警\n\n");
        sb.append("---\n\n");
        sb.append("**接口名称：** ").append(api.getName()).append("\n\n");
        sb.append("**接口地址：** ").append(api.getUrl()).append("\n\n");
        sb.append("**请求方法：** ").append(api.getMethod()).append("\n\n");
        sb.append("**告警时间：** ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n\n");
        sb.append("**连续失败次数：** ").append(consecutiveFailures).append(" 次\n\n");
        sb.append("**告警阈值：** ").append(api.getFailThreshold()).append(" 次\n\n");
        sb.append("---\n\n");
        sb.append("**描述：** ").append(api.getDescription() != null ? api.getDescription() : "无").append("\n\n");
        sb.append("@所有人");

        return sb.toString();
    }
}
