package com.wxy97.webhook.strategy;

import com.wxy97.webhook.watch.ExtensionChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ReactiveExtensionClient;

/**
 * Author: wxy97.com
 * Date: 2024/4/19 09:55
 * Description:
 */
@Component
@RequiredArgsConstructor
public class MomentStrategy implements ExtensionStrategy {

    public static String KIND_NAME = "Moment";


    @Override
    public void process(ExtensionChangedEvent event,
        ReactiveExtensionClient reactiveExtensionClient, String webhookUrl) {

        Extension extension = event.getExtension();
        ExtensionChangedEvent.EventType eventType = event.getEventType();

        System.out.println("发布瞬间了");

    }
}