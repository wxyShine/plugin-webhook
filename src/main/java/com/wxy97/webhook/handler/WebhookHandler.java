package com.wxy97.webhook.handler;

import com.wxy97.webhook.config.BasicSetting;
import com.wxy97.webhook.strategy.ExtensionStrategy;
import com.wxy97.webhook.strategy.StrategyFactory;
import com.wxy97.webhook.watch.ExtensionChangedEvent;
import java.util.List;
import java.util.Map;
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
import run.halo.app.plugin.ReactiveSettingFetcher;
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

    private final ReactiveSettingFetcher reactiveSettingFetcher;

    private final ReactiveExtensionClient reactiveExtensionClient;
    private final StrategyFactory strategyFactory;

    @Override
    public void onApplicationEvent(@NonNull ExtensionChangedEvent event) {
        Assert.state(!Schedulers.isInNonBlockingThread(),
            "Must be called in a non-reactive thread.");

        Extension extension = event.getExtension();
        if (extension == null) {
            return;
        }

        String kind = extension.getKind();
        log.debug("Kind [{}] triggered [{}] event.", kind, event.getEventType());

        reactiveSettingFetcher.fetch("basic", BasicSetting.class).doOnNext(setting -> {
            if (setting.getEnableWebhook()) {
                ExtensionStrategy strategy = strategyFactory.getStrategyForKind(kind);
                strategy.process(event, reactiveExtensionClient);
            }
        }).subscribe();
    }
}
