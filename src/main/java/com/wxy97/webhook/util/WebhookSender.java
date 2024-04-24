package com.wxy97.webhook.util;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Author: wxy97.com
 * Date: 2024/4/19 10:01
 * Description:
 */
public class WebhookSender {


    /**
     * 发送请求
     *
     * @param webhookUrl
     * @param body
     */
    public static void sendWebhook(String webhookUrl, Object body) {
        WebClient webClient = WebClient.create(webhookUrl);
        webClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(Void.class)
            .subscribe();
    }
}