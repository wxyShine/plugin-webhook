package com.wxy97.webhook.bean;

import com.wxy97.webhook.util.DateUtil;
import lombok.Data;
import run.halo.app.core.extension.content.SinglePage;

/**
 * Author: wxy97.com
 * Date: 2024/4/24 10:27
 * Description:
 */
@Data
public class SinglePageData {

    private String title;

    private String slug;

    private String visible;

    private String owner;

    private String createTime;

    private String publishTime;


    public static SinglePageData convertToSinglePageData(SinglePage singlePage) {
        SinglePageData singlePageData = new SinglePageData();
        singlePageData.setTitle(singlePage.getSpec().getTitle());
        singlePageData.setSlug(singlePage.getSpec().getSlug());
        singlePageData.setVisible(singlePage.getSpec().getVisible().name());
        singlePageData.setOwner(singlePage.getSpec().getOwner());
        singlePageData.setCreateTime(
            DateUtil.format(singlePage.getMetadata().getCreationTimestamp()));
        singlePageData.setPublishTime(DateUtil.format(singlePage.getSpec().getPublishTime()));
        return singlePageData;
    }
}
