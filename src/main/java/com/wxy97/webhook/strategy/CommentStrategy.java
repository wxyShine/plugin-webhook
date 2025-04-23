package com.wxy97.webhook.strategy;

import com.wxy97.webhook.bean.BaseBody;
import com.wxy97.webhook.bean.CommentData;
import com.wxy97.webhook.bean.PluginData;
import com.wxy97.webhook.bean.PostData;
import com.wxy97.webhook.bean.SinglePageData;
import com.wxy97.webhook.enums.WebhookEventEnum;
import com.wxy97.webhook.util.DateUtil;
import com.wxy97.webhook.util.WebhookSender;
import com.wxy97.webhook.watch.ExtensionChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.content.Comment;
import run.halo.app.core.extension.content.Post;
import run.halo.app.core.extension.content.SinglePage;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ReactiveExtensionClient;

import java.time.Instant;
import java.util.Objects;

@StrategyKind("Comment")
@Component
@RequiredArgsConstructor
public class CommentStrategy implements ExtensionStrategy {

    private final ReactiveExtensionClient reactiveExtensionClient;
    private final WebhookSender webhookSender;


    @Override
    public void process(ExtensionChangedEvent event,
                        ReactiveExtensionClient reactiveExtensionClient) {
        Extension extension = event.getExtension();
        ExtensionChangedEvent.EventType eventType = event.getEventType();
        Comment comment = (Comment) extension;
        switch (eventType) {
            case ADDED:
                handleAddedEvent(comment);
                break;
            case UPDATED:
                handleUpdatedEvent(comment);
                break;
            default:
        }
    }

    private void handleAddedEvent(Comment comment) {
        String refName = comment.getSpec().getSubjectRef().getName();
        String refKind = comment.getSpec().getSubjectRef().getKind();

        // 单独处理友链插件评论
        if ("PluginLinks".equals(refName)) {
            BaseBody<CommentData> baseBody = createBaseBody(comment, null);
            baseBody.setHookTime(DateUtil.formatNow());
            baseBody.getData().setPluginData(new PluginData("PluginLinks"));
            baseBody.setEventTypeName(WebhookEventEnum.NEW_COMMENT.getDescription());
            baseBody.setEventType(WebhookEventEnum.NEW_COMMENT);
            webhookSender.sendWebhook(baseBody);
            return;
        }

        Mono.defer(() -> fetchObject(refKind, refName))
            .subscribe(object -> {
                if (object != null) {
                    BaseBody<CommentData> baseBody = createBaseBody(comment, object);
                    baseBody.setEventTypeName(WebhookEventEnum.NEW_COMMENT.getDescription());
                    baseBody.setEventType(WebhookEventEnum.NEW_COMMENT);
                    webhookSender.sendWebhook(baseBody);
                }
            });
    }

    private void handleUpdatedEvent(Comment comment) {
        Instant deletionTimestamp = comment.getMetadata().getDeletionTimestamp();
        if (!Objects.isNull(deletionTimestamp)) {
            String refKind = comment.getSpec().getSubjectRef().getKind();
            String refName = comment.getSpec().getSubjectRef().getName();


            // 单独处理友链插件评论
            if ("PluginLinks".equals(refName)) {
                BaseBody<CommentData> baseBody = createBaseBody(comment, null);
                baseBody.setHookTime(DateUtil.formatNow());
                baseBody.getData().setPluginData(new PluginData("PluginLinks"));
                baseBody.setEventTypeName(WebhookEventEnum.DELETE_COMMENT.getDescription());
                baseBody.setEventType(WebhookEventEnum.DELETE_COMMENT);
                webhookSender.sendWebhook(baseBody);
                return;
            }

            Mono.defer(() -> fetchObject(refKind, refName))
                .map(object -> {
                    BaseBody<CommentData> baseBody = createBaseBody(comment, object);
                    baseBody.setEventTypeName(WebhookEventEnum.DELETE_COMMENT.getDescription());
                    baseBody.setEventType(WebhookEventEnum.DELETE_COMMENT);
                    webhookSender.sendWebhook(baseBody);
                    return object;
                })
                .subscribe();
        }
    }

    private Mono<Extension> fetchObject(String refKind, String refName) {
        if ("Post".equals(refKind)) {
            return reactiveExtensionClient.fetch(Post.class, refName).cast(Extension.class);
        } else if ("SinglePage".equals(refKind)) {
            return reactiveExtensionClient.fetch(SinglePage.class, refName).cast(Extension.class);
        } else {
            return Mono.empty();
        }
    }

    private BaseBody<CommentData> createBaseBody(Comment comment, Extension object) {
        BaseBody<CommentData> baseBody = new BaseBody<>();
        baseBody.setHookTime(DateUtil.formatNow());

        CommentData commentData = CommentData.convertToCommentData(comment);
        if (object instanceof Post) {
            commentData.setPostData(PostData.convertToPostData((Post) object));
        } else if (object instanceof SinglePage) {
            commentData.setSinglePageData(
                SinglePageData.convertToSinglePageData((SinglePage) object));
        }
        baseBody.setData(commentData);
        return baseBody;
    }
}
