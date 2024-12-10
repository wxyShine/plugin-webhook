package com.wxy97.webhook.bean;

import lombok.Data;

/**
 * Author: wxy97.com
 * Date: 2024/12/10 15:04
 * Description:
 */
@Data
public class PluginData {

    String pluginName;

    public PluginData() {
    }

    public PluginData(String pluginName) {
        this.pluginName = pluginName;
    }
}
