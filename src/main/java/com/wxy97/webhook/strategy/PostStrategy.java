package com.wxy97.webhook.strategy;

import com.wxy97.webhook.bean.BaseBody;
import com.wxy97.webhook.bean.PostData;
import com.wxy97.webhook.enums.WebhookEventEnum;
import com.wxy97.webhook.util.DateUtil;
import com.wxy97.webhook.util.WebhookSender;
import com.wxy97.webhook.watch.ExtensionChangedEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.content.Post;
import run.halo.app.extension.Extension;
import run.halo.app.extension.MetadataOperator;
import run.halo.app.extension.ReactiveExtensionClient;

/**
 * Author: wxy97.com
 * Date: 2024/4/19 09:54
 * Description:
 */
@StrategyKind("Post")
@Component
@RequiredArgsConstructor
public class PostStrategy implements ExtensionStrategy {

    private final WebhookSender webhookSender;


    @Override
    public void process(ExtensionChangedEvent event,
                        ReactiveExtensionClient reactiveExtensionClient) {

        Extension extension = event.getExtension();
        ExtensionChangedEvent.EventType eventType = event.getEventType();


        if (ExtensionChangedEvent.EventType.UPDATED.equals(eventType)) {
            Post newPost = (Post) extension;
            AtomicReference<BaseBody<PostData>> baseBody = new AtomicReference<>();
            if (isNewArticle(newPost)) {

                Mono<Post> fetch =
                    reactiveExtensionClient.fetch(Post.class, newPost.getMetadata().getName());
                fetch.delayElement(Duration.ofSeconds(3)).subscribe(
                    post -> {
                        // 处理获取到的 Post 对象
                        baseBody.set(new BaseBody<>());
                        PostData postData = new PostData();
                        postData.setTitle(post.getSpec().getTitle());
                        postData.setOwner(post.getSpec().getOwner());
                        postData.setSlug(post.getSpec().getSlug());
                        postData.setCreateTime(
                            DateUtil.format(post.getMetadata().getCreationTimestamp()));
                        postData.setPublishTime(DateUtil.formatNow());
                        postData.setPermalink(post.getStatus().getPermalink());
                        postData.setVisible(post.getSpec().getVisible().name());

                        baseBody.get().setData(postData);
                        baseBody.get().setEventType(WebhookEventEnum.NEW_POST);
                        baseBody.get().setEventTypeName(WebhookEventEnum.NEW_POST.getDescription());
                        baseBody.get().setHookTime(DateUtil.formatNow());
                        webhookSender.sendWebhook(baseBody);
                    });
            }

            /*
             * 文章删除 根据 deleteLabel识别删除，根据deletionTimestamp区别删除
             */
            MetadataOperator metadata = newPost.getMetadata();

            if (metadata != null && metadata.getLabels() != null) {
                Map<String, String> labels = metadata.getLabels();
                String deleteLabel = labels.get(Post.DELETED_LABEL);
                Instant deletionTimestamp = newPost.getMetadata().getDeletionTimestamp();

                if ("true".equals(deleteLabel)) {
                    baseBody.set(new BaseBody<>());
                    PostData postData = new PostData();
                    postData.setTitle(newPost.getSpec().getTitle());
                    postData.setOwner(newPost.getSpec().getOwner());
                    postData.setSlug(newPost.getSpec().getSlug());
                    postData.setCreateTime(
                        DateUtil.format(newPost.getMetadata().getCreationTimestamp()));
                    postData.setPublishTime(DateUtil.format(newPost.getSpec().getPublishTime()));
                    postData.setPermalink(newPost.getStatus().getPermalink());
                    postData.setVisible(newPost.getSpec().getVisible().name());
                    baseBody.get().setData(postData);
                    baseBody.get().setEventType(WebhookEventEnum.DELETE_POST);
                    baseBody.get()
                        .setEventTypeName(WebhookEventEnum.DELETE_POST.getDescription());
                    // 删除到（回收站）finalizers=[post-protection]
                    // 永久删除 finalizers=[]
                    postData.setIsPermanent(!Objects.isNull(deletionTimestamp));

                    // 是否恢复文章
                    Boolean deleted = newPost.getSpec().getDeleted();
                    postData.setIsRestore(!deleted);

                    baseBody.get().setHookTime(DateUtil.formatNow());
                    webhookSender.sendWebhook(baseBody);

                }
            }

        }
    }


    /**
     * 文章发布
     * 在onUpdate 里面检查 如果newExtension.getSpec().getReleaseSnapshot()  不等于 newExtension
     * .getAnnotations.get(Post.LAST_RELEASED_SNAPSHOT_ANNO)
     * 且 getSpec().getPublish()是true就是文章发布
     */
    private boolean isNewArticle(Post newPost) {
        String lastReleaseSnapshot =
            newPost.getMetadata().getAnnotations().get(Post.LAST_RELEASED_SNAPSHOT_ANNO);
        String expectReleaseSnapshot = newPost.getSpec().getReleaseSnapshot();
        Boolean newPublish = newPost.getSpec().getPublish();
        return !Objects.equals(expectReleaseSnapshot, lastReleaseSnapshot) && newPublish;
    }
}