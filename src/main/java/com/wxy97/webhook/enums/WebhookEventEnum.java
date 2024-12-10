package com.wxy97.webhook.enums;

import lombok.Getter;

/**
 * Author: wxy97.com
 * Date: 2024/4/19 09:52
 * Description:
 */
@Getter
public enum WebhookEventEnum {
    TEST_WEBHOOK("测试webhook"),

    NEW_POST("发布文章"),
    DELETE_POST("删除文章"),

    NEW_COMMENT("发表评论"),
    DELETE_COMMENT("删除评论"),
    REPLY_COMMENT("回复评论"),
    DELETE_REPLY_COMMENT("删除回复"),

    NEW_DEVICE_LOGIN("新设备登录");

    private final String description;

    WebhookEventEnum(String description) {
        this.description = description;
    }
}
