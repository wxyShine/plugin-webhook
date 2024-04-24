package com.wxy97.webhook;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import run.halo.app.plugin.BasePlugin;
import run.halo.app.plugin.PluginContext;

/**
 * <p>Plugin main class to manage the lifecycle of the plugin.</p>
 * <p>This class must be public and have a public constructor.</p>
 * <p>Only one main class extending {@link BasePlugin} is allowed per plugin.</p>
 * <p>
 * Author: wxy97.com
 * Date: 2024/4/19 10:01
 * Description:
 */
@EnableAsync
@Component
public class WebhookPlugin extends BasePlugin {

    public WebhookPlugin(PluginContext pluginContext) {
        super(pluginContext);
    }

    @Override
    public void start() {
        System.out.println("Webhook插件启动成功！");
    }

    @Override
    public void stop() {
        System.out.println("Webhook插件停止！");
    }

    @Override
    public void delete() {
        System.out.println("Webhook插件删除！");

    }
}
