package com.wxy97.webhook.bean;

import lombok.Data;

/**
 * Author: wxy97.com
 * Date: 2024/10/25 15:34
 * Description:
 */
@Data
public class DeviceLoginData {

    private String principalName;

    private String ipAddress;

    private String rememberMeSeriesId;

    private String browser;

    private String os;
}
