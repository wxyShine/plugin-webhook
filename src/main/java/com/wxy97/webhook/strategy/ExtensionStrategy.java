package com.wxy97.webhook.strategy;

import com.wxy97.webhook.watch.ExtensionChangedEvent;
import run.halo.app.extension.ReactiveExtensionClient;

/**
 * Author: wxy97.com
 * Date: 2024/4/19 09:50
 * Description:
 */
public interface ExtensionStrategy {
    void process(ExtensionChangedEvent event, ReactiveExtensionClient reactiveExtensionClient);
}
