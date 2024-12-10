package com.wxy97.webhook.strategy;

import com.wxy97.webhook.watch.ExtensionChangedEvent;
import java.util.Optional;
import org.springframework.stereotype.Component;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ReactiveExtensionClient;

/**
 * Author: wxy97.com
 * Date: 2024/4/19 10:41
 * Description:
 */
@Component
public class DefaultStrategy implements ExtensionStrategy{
    @Override
    public void process(ExtensionChangedEvent event,
        ReactiveExtensionClient reactiveExtensionClient, String webhookUrl) {

        Extension extension = event.getExtension();
        ExtensionChangedEvent.EventType eventType = event.getEventType();

        Optional<Extension> optionalExtension = Optional.ofNullable(extension);
        // 通过 ifPresent() 检查并调用 getKind() 方法
        optionalExtension.ifPresent(ext -> {
            String kind = ext.getKind();
            // 后续逻辑
            if (("Moment").equals(kind) && ExtensionChangedEvent.EventType.UPDATED.equals(eventType)) {
                System.out.println("发布瞬间了");
            }
        });


    }
}
