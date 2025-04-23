package com.wxy97.webhook.util;

import com.wxy97.webhook.config.BasicSetting;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import run.halo.app.plugin.ReactiveSettingFetcher;

/**
 * Author: wxy97.com
 * Date: 2025/4/22 17:27
 * Description:
 */
@Component
@RequiredArgsConstructor
public class WebhookSender {

    private final WebClient.Builder webClientBuilder;
    private final ReactiveSettingFetcher reactiveSettingFetcher;

    public void sendWebhook(Object body) {
        reactiveSettingFetcher.fetch("basic", BasicSetting.class)
            .defaultIfEmpty(new BasicSetting()) // 防空
            .flatMap(setting -> {
                WebClient.RequestBodySpec requestSpec = webClientBuilder
                    .baseUrl(setting.getWebhookUrl())
                    .build()
                    .post()
                    .contentType(MediaType.APPLICATION_JSON);

                // 加头
                if (setting.getHeaders() != null) {
                    setting.getHeaders().forEach(h -> {
                        String name = String.valueOf(h.get("name"));
                        String value = String.valueOf(h.get("value"));
                        requestSpec.header(name, value);
                    });
                }

                // 发送请求
                return requestSpec
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Void.class);
            })
            .subscribe();
    }
}
