package com.wxy97.webhook.watch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ExtensionClient;
import run.halo.app.extension.Watcher;

/**
 * Author: wxy97.com
 * Date: 2024/4/18 16:53
 * Description:
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookWatcher implements Watcher, InitializingBean {

    private final ExtensionClient client;
    private final ApplicationEventPublisher eventPublisher;

    private Runnable disposeHook;
    private volatile boolean disposed = false;

    @Override
    public void onAdd(Extension extension) {
        eventPublisher.publishEvent(ExtensionChangedEvent.onAdded(this, extension));
    }

    @Override
    public void onUpdate(Extension oldExtension, Extension newExtension) {
        eventPublisher.publishEvent(
            ExtensionChangedEvent.onUpdated(this, oldExtension, newExtension));
    }

    @Override
    public void onDelete(Extension extension) {
        eventPublisher.publishEvent(ExtensionChangedEvent.onDeleted(this, extension));
    }

    @Override
    public void registerDisposeHook(Runnable dispose) {
        this.disposeHook = dispose;
    }

    @Override
    public void dispose() {
        if (isDisposed()) {
            return;
        }
        this.disposed = true;
        if (this.disposeHook != null) {
            this.disposeHook.run();
        }
    }

    @Override
    public boolean isDisposed() {
        return this.disposed;
    }

    @Override
    public void afterPropertiesSet() {
        client.watch(this);
    }

}