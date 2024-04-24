package com.wxy97.webhook.controller;

import com.wxy97.webhook.bean.BaseBody;
import com.wxy97.webhook.config.BasicSetting;
import com.wxy97.webhook.enums.WebhookEventEnum;
import com.wxy97.webhook.util.DateUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import run.halo.app.plugin.ApiVersion;

/**
 * Author: wxy97.com
 * Date: 2024/4/23 15:18
 * Description:
 */

@ApiVersion("wxy.halo.run/v1alpha1")
@RequestMapping("/webhook")
@RestController
public class WebhookController {

    @PostMapping("/sendTest")
    public Mono<Void> test(@RequestBody BasicSetting basicSetting) {

        String webhookUrl = basicSetting.getWebhookUrl();
        BaseBody<String> baseBody = new BaseBody<>();
        baseBody.setEventType(WebhookEventEnum.TEST_WEBHOOK);
        baseBody.setEventTypeName(WebhookEventEnum.TEST_WEBHOOK.getDescription());
        baseBody.setData("Test webhook success");
        baseBody.setHookTime(DateUtil.formatNow());
        WebClient webClient = WebClient.create();
        return webClient.post()
            .uri(webhookUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(baseBody)
            .retrieve()
            .bodyToMono(Void.class)
            .onErrorResume(WebClientException.class, error -> {
                // 处理请求失败的情况
                return Mono.error(
                    new ServerWebInputException("Webhook 测试失败: " + error.getMessage()));
            })
            .then();
    }
}