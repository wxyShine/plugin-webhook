package com.wxy97.webhook.handler;

import com.wxy97.webhook.config.BasicSetting;
import com.wxy97.webhook.strategy.ExtensionStrategy;
import com.wxy97.webhook.strategy.StrategyFactory;
import com.wxy97.webhook.watch.ExtensionChangedEvent;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.scheduler.Schedulers;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ReactiveExtensionClient;
import run.halo.app.plugin.SettingFetcher;

/**
 * <p>Watch extension changed event must in an async thread to avoid blocking the main thread.</p>
 *
 * @see Async
 * @see org.springframework.scheduling.annotation.EnableAsync
 */
@Slf4j
@Async
@Component
@RequiredArgsConstructor
public class WebhookHandler implements ApplicationListener<ExtensionChangedEvent> {

    private final SettingFetcher settingFetcher;
    private final ReactiveExtensionClient reactiveExtensionClient;
    private final StrategyFactory strategyFactory;

    @Override
    public void onApplicationEvent(@NonNull ExtensionChangedEvent event) {
        Assert.state(!Schedulers.isInNonBlockingThread(),
            "Must be called in a non-reactive thread.");
        Extension extension = null;
        if (event.getExtension() != null) {
            extension = event.getExtension();
        } else if (event.getOldExtension() != null) {
            extension = event.getOldExtension();
        } else {
            return;
        }
        log.info("Extension [{}] triggered the [{}] event.", extension.getClass(),
            event.getEventType());

        settingFetcher.fetch("basic", BasicSetting.class)
            .ifPresent(basicSetting -> {
                var webhookUrl = basicSetting.getWebhookUrl();
                var enableWebhook = basicSetting.getEnableWebhook();

                if (enableWebhook) {
                    Optional<ExtensionStrategy> optionalStrategy =
                        strategyFactory.getStrategyForExtension(event.getExtension());
                    if (optionalStrategy.isPresent()) {
                        ExtensionStrategy strategy = optionalStrategy.get();
                        strategy.process(event, reactiveExtensionClient, webhookUrl);
                    }
                }
            });
    }

}