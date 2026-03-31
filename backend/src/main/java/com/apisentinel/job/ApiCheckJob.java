package com.apisentinel.job;

import com.apisentinel.entity.CheckRecord;
import com.apisentinel.entity.MonitorApi;
import com.apisentinel.service.CheckRecordService;
import com.apisentinel.service.MonitorApiService;
import com.apisentinel.util.DingTalkUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * API健康检查定时任务
 *
 * @author ApiSentinel Team
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApiCheckJob extends QuartzJobBean {

    private final MonitorApiService monitorApiService;
    private final CheckRecordService checkRecordService;
    private final DingTalkUtil dingTalkUtil;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("开始执行API健康检查任务...");
        
        List<MonitorApi> apis = monitorApiService.listEnabledApis();
        
        for (MonitorApi api : apis) {
            try {
                checkApi(api);
            } catch (Exception e) {
                log.error("检查API失败: {} - {}", api.getName(), e.getMessage());
            }
        }
        
        log.info("API健康检查任务执行完成");
    }

    /**
     * 检查单个API
     */
    private void checkApi(MonitorApi api) {
        String url = api.getUrl();
        String method = api.getMethod();
        Integer timeout = api.getTimeout() != null ? api.getTimeout() : 10;
        
        CheckRecord record = new CheckRecord();
        record.setApiId(api.getId());
        record.setCheckTime(LocalDateTime.now());
        
        long startTime = System.currentTimeMillis();
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpUriRequestBase request;
            
            // 根据请求方法创建请求
            if ("POST".equalsIgnoreCase(method)) {
                request = new HttpPost(url);
                if (api.getBody() != null && !api.getBody().isEmpty()) {
                    ((HttpPost) request).setEntity(new StringEntity(api.getBody(), ContentType.APPLICATION_JSON));
                }
            } else if ("PUT".equalsIgnoreCase(method)) {
                request = new HttpPut(url);
                if (api.getBody() != null && !api.getBody().isEmpty()) {
                    ((HttpPut) request).setEntity(new StringEntity(api.getBody(), ContentType.APPLICATION_JSON));
                }
            } else if ("DELETE".equalsIgnoreCase(method)) {
                request = new HttpDelete(url);
            } else {
                request = new HttpGet(url);
            }
            
            // 设置请求头
            if (api.getHeaders() != null && !api.getHeaders().isEmpty()) {
                // 简单解析JSON格式的请求头
                String headers = api.getHeaders();
                if (headers.startsWith("{") && headers.endsWith("}")) {
                    headers = headers.substring(1, headers.length() - 1);
                    String[] pairs = headers.split(",");
                    for (String pair : pairs) {
                        String[] kv = pair.split(":", 2);
                        if (kv.length == 2) {
                            String key = kv[0].trim().replace("\"", "");
                            String value = kv[1].trim().replace("\"", "");
                            request.setHeader(key, value);
                        }
                    }
                }
            }
            
            // 设置超时
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(Timeout.ofSeconds(timeout))
                    .setResponseTimeout(Timeout.ofSeconds(timeout))
                    .build();
            request.setConfig(requestConfig);
            
            // 执行请求
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                long responseTime = System.currentTimeMillis() - startTime;
                int statusCode = response.getCode();
                
                record.setResponseTime(responseTime);
                record.setStatusCode(statusCode);
                record.setSuccess(statusCode >= 200 && statusCode < 300 ? 1 : 0);
                
                // 读取响应内容（限制长度）
                if (response.getEntity() != null) {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    if (responseBody.length() > 1000) {
                        responseBody = responseBody.substring(0, 1000) + "...";
                    }
                    record.setResponseBody(responseBody);
                }
                
                log.info("API检查完成: {}, 状态码: {}, 响应时间: {}ms", 
                        api.getName(), statusCode, responseTime);
            }
            
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            record.setResponseTime(responseTime);
            record.setStatusCode(0);
            record.setSuccess(0);
            record.setErrorMessage(e.getMessage());
            
            log.error("API检查异常: {}, 错误: {}", api.getName(), e.getMessage());
        }
        
        // 保存检查记录
        checkRecordService.saveRecord(record);
        
        // 检查是否需要发送告警
        if (record.getSuccess() == 0) {
            checkAndSendAlert(api);
        }
    }

    /**
     * 检查并发送告警
     */
    private void checkAndSendAlert(MonitorApi api) {
        Integer failThreshold = api.getFailThreshold() != null ? api.getFailThreshold() : 3;
        Integer consecutiveFailures = checkRecordService.countConsecutiveFailures(api.getId());
        
        if (consecutiveFailures != null && consecutiveFailures >= failThreshold) {
            // 检查是否已经发送过告警（避免重复发送）
            if (consecutiveFailures == failThreshold) {
                dingTalkUtil.sendAlert(api, consecutiveFailures);
            }
        }
    }

    // 内部类：HttpPut 和 HttpDelete
    private static class HttpPut extends org.apache.hc.client5.http.classic.methods.HttpPut {
        public HttpPut(String uri) {
            super(uri);
        }
    }

    private static class HttpDelete extends org.apache.hc.client5.http.classic.methods.HttpDelete {
        public HttpDelete(String uri) {
            super(uri);
        }
    }
}
