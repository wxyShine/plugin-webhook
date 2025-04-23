package com.wxy97.webhook.strategy;

import com.wxy97.webhook.bean.BaseBody;
import com.wxy97.webhook.bean.DeviceLoginData;
import com.wxy97.webhook.enums.WebhookEventEnum;
import com.wxy97.webhook.util.DateUtil;
import com.wxy97.webhook.util.WebhookSender;
import com.wxy97.webhook.watch.ExtensionChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.Device;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ReactiveExtensionClient;

/**
 * Author: wxy97.com
 * Date: 2024/4/19 10:41
 * Description:
 */
@StrategyKind("Device")
@Component
@RequiredArgsConstructor
public class DeviceStrategy implements ExtensionStrategy {

    private final WebhookSender webhookSender;

    @Override
    public void process(ExtensionChangedEvent event,
                        ReactiveExtensionClient reactiveExtensionClient) {

        Extension extension = event.getExtension();
        ExtensionChangedEvent.EventType eventType = event.getEventType();
        Device device = (Device) extension;

        if (eventType.equals(ExtensionChangedEvent.EventType.ADDED)) {
            // 新设备登录
            BaseBody<Object> baseBody = new BaseBody<>();

            DeviceLoginData data = new DeviceLoginData();
            data.setPrincipalName(device.getSpec().getPrincipalName());
            data.setIpAddress(device.getSpec().getIpAddress());
            data.setOs(device.getStatus().getOs());
            data.setBrowser(device.getStatus().getBrowser());
            data.setRememberMeSeriesId(device.getSpec().getRememberMeSeriesId());

            baseBody.setData(data);
            baseBody.setEventType(WebhookEventEnum.NEW_DEVICE_LOGIN);
            baseBody.setEventTypeName(WebhookEventEnum.NEW_DEVICE_LOGIN.getDescription());
            baseBody.setHookTime(DateUtil.formatNow());
            webhookSender.sendWebhook(baseBody);
        }

    }
}
