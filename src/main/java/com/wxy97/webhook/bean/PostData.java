package com.wxy97.webhook.bean;

import com.wxy97.webhook.util.DateUtil;
import lombok.Data;
import run.halo.app.core.extension.content.Post;

/**
 * Author: wxy97.com
 * Date: 2024/4/19 10:01
 * Description:
 */
@Data
public class PostData {

    private String title;

    private String slug;

    private String permalink;

    private String visible;

    private String owner;

    private String createTime;

    private String publishTime;

    /**
     * true 永久删除, false 删除到回收站
     */
    private Boolean isPermanent;
    /**
     * true 恢复文章
     */
    private Boolean isRestore;

    public static PostData convertToPostData(Post post) {
        PostData postData = new PostData();
        postData.setTitle(post.getSpec().getTitle());
        postData.setSlug(post.getSpec().getSlug());
        postData.setPermalink(post.getStatus().getPermalink());
        postData.setVisible(post.getSpec().getVisible().name());
        postData.setOwner(post.getSpec().getOwner());
        postData.setCreateTime(
            DateUtil.format(post.getMetadata().getCreationTimestamp()));
        postData.setPublishTime(DateUtil.formatNow());
        return postData;
    }
}
