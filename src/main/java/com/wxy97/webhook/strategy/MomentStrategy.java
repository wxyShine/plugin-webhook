package com.wxy97.webhook.strategy;

import com.wxy97.webhook.config.BasicSetting;
import com.wxy97.webhook.watch.ExtensionChangedEvent;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ReactiveExtensionClient;
import run.halo.app.plugin.SettingFetcher;

/**
 * Author: wxy97.com
 * Date: 2024/4/19 09:55
 * Description:
 */
//@StrategyKind("Moment")
@Component
@RequiredArgsConstructor
public class MomentStrategy implements ExtensionStrategy {

    private final SettingFetcher settingFetcher;


    @Override
    public void process(ExtensionChangedEvent event,
                        ReactiveExtensionClient reactiveExtensionClient) {

        Extension extension = event.getExtension();
        ExtensionChangedEvent.EventType eventType = event.getEventType();

        System.out.println(extension);
        System.out.println(eventType);
        if (ExtensionChangedEvent.EventType.ADDED.equals(eventType)) {
            System.out.println("发布瞬间了");
        } else if (ExtensionChangedEvent.EventType.UPDATED.equals(eventType)) {
            Instant deletionTimestamp = extension.getMetadata().getDeletionTimestamp();
            Set<String> finalizers = extension.getMetadata().getFinalizers();
            if (deletionTimestamp != null && finalizers.isEmpty()) {
                settingFetcher.fetch("basic", BasicSetting.class)
                    .ifPresent(basicSetting -> {
                        List<Map<String, Object>> headers = basicSetting.getHeaders();
                        System.out.println(headers);
                    });
                System.out.println("删除瞬间了");
            }
        }
    }
}