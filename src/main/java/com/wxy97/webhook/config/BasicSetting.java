package com.wxy97.webhook.config;

import lombok.Data;

/**
 * Author: wxy97.com
 * Date: 2024/4/19 09:03
 * Description:
 */
@Data
public class BasicSetting {

    public static final String GROUP_NAME = "basic";

    private Boolean enableWebhook;

    private String webhookUrl;
}
