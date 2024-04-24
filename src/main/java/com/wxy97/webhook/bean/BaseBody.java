package com.wxy97.webhook.bean;

import com.wxy97.webhook.enums.WebhookEventEnum;
import lombok.Data;

/**
 * Author: wxy97.com
 * Date: 2024/4/19 10:00
 * Description:
 */
@Data
public class BaseBody<T> {

    private WebhookEventEnum eventType;

    private String eventTypeName;

    private String hookTime;

    private T data;

}
