package com.wxy97.webhook.controller;

import com.wxy97.webhook.bean.BaseBody;
import com.wxy97.webhook.config.BasicSetting;
import com.wxy97.webhook.enums.WebhookEventEnum;
import com.wxy97.webhook.strategy.ExtensionStrategy;
import com.wxy97.webhook.util.DateUtil;
import com.wxy97.webhook.util.WebhookSender;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
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
import run.halo.app.plugin.ReactiveSettingFetcher;
import run.halo.app.plugin.SettingFetcher;

/**
 * Author: wxy97.com
 * Date: 2024/4/23 15:18
 * Description:
 */

@ApiVersion("wxy.halo.run/v1alpha1")
@RequestMapping("/webhook")
@RestController
@AllArgsConstructor
public class WebhookController {

    private final WebhookSender webhookSender;

    @PostMapping("/sendTest")
    public Mono<Void> test(@RequestBody BasicSetting basicSetting) {
        BaseBody<String> baseBody = new BaseBody<>();
        baseBody.setEventType(WebhookEventEnum.TEST_WEBHOOK);
        baseBody.setEventTypeName(WebhookEventEnum.TEST_WEBHOOK.getDescription());
        baseBody.setData("Test webhook success");
        baseBody.setHookTime(DateUtil.formatNow());
        webhookSender.sendWebhook(baseBody);
        return Mono.empty();
    }
}