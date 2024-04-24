package com.wxy97.webhook.watch;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import run.halo.app.extension.Extension;

/**
 * Author: wxy97.com
 * Date: 2024/4/18 16:53
 * Description:
 */
@Getter
public class ExtensionChangedEvent extends ApplicationEvent {
    private final Extension extension;
    private final Extension oldExtension;

    public ExtensionChangedEvent(Object source, Extension oldExtension, Extension extension) {
        super(source);
        this.oldExtension = oldExtension;
        this.extension = extension;
    }

    public EventType getEventType() {
        if (oldExtension == null && extension != null) {
            return EventType.ADDED;
        } else if (oldExtension != null && extension != null) {
            return EventType.UPDATED;
        } else {
            return EventType.DELETED;
        }
    }

    public enum EventType {
        ADDED,
        UPDATED,
        DELETED
    }

    public static ExtensionChangedEvent onAdded(Object o, Extension extension) {
        return new ExtensionChangedEvent(o, null, extension);
    }

    public static ExtensionChangedEvent onUpdated(Object o, Extension oldExtension,
        Extension extension) {
        return new ExtensionChangedEvent(o, oldExtension, extension);
    }

    public static ExtensionChangedEvent onDeleted(Object o, Extension extension) {
        return new ExtensionChangedEvent(o, extension, null);
    }
}