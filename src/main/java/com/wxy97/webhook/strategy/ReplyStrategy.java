package com.wxy97.webhook.strategy;

import com.wxy97.webhook.bean.BaseBody;
import com.wxy97.webhook.bean.CommentData;
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
import run.halo.app.core.extension.content.Reply;
import run.halo.app.core.extension.content.SinglePage;
import run.halo.app.extension.Extension;
import run.halo.app.extension.ReactiveExtensionClient;

/**
 * Author: wxy97.com
 * Date: 2024/4/19 09:54
 * Description:
 */
@Component
@RequiredArgsConstructor
public class ReplyStrategy implements ExtensionStrategy {

    private final ReactiveExtensionClient reactiveExtensionClient;


    @Override
    public void process(ExtensionChangedEvent event,
        ReactiveExtensionClient reactiveExtensionClient, String webhookUrl) {

        Extension extension = event.getExtension();
        ExtensionChangedEvent.EventType eventType = event.getEventType();
        Reply reply = (Reply) extension;

        // 被回复的原评论
        String commentName = reply.getSpec().getCommentName();

        switch (eventType) {
            case ADDED:
                reactiveExtensionClient.fetch(Comment.class, commentName).subscribe(comment -> {
                    String refName = comment.getSpec().getSubjectRef().getName();
                    String refKind = comment.getSpec().getSubjectRef().getKind();
                    Mono.defer(() -> fetchObject(refKind, refName))
                        .subscribe(object -> {
                            if (object != null) {
                                BaseBody<CommentData> baseBody =
                                    createBaseBody(reply, comment, object);
                                baseBody.setEventTypeName(
                                    WebhookEventEnum.REPLY_COMMENT.getDescription());
                                baseBody.setEventType(WebhookEventEnum.REPLY_COMMENT);
                                WebhookSender.sendWebhook(webhookUrl, baseBody);
                            }
                        });
                });
                break;
            case UPDATED:
                if (reply.getMetadata().getDeletionTimestamp() !=null){
                    reactiveExtensionClient.fetch(Comment.class, commentName).subscribe(comment -> {
                        String refName = comment.getSpec().getSubjectRef().getName();
                        String refKind = comment.getSpec().getSubjectRef().getKind();
                        Mono.defer(() -> fetchObject(refKind, refName))
                            .subscribe(object -> {
                                if (object != null) {
                                    BaseBody<CommentData> baseBody =
                                        createBaseBody(reply, comment, object);
                                    baseBody.setEventTypeName(
                                        WebhookEventEnum.DELETE_REPLY_COMMENT.getDescription());
                                    baseBody.setEventType(WebhookEventEnum.DELETE_REPLY_COMMENT);
                                    WebhookSender.sendWebhook(webhookUrl, baseBody);
                                }
                            });
                    });
                }
                break;
            default:
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

    private BaseBody<CommentData> createBaseBody(Reply reply, Comment comment, Extension object) {
        BaseBody<CommentData> baseBody = new BaseBody<>();
        baseBody.setHookTime(DateUtil.formatNow());

        CommentData commentData = CommentData.convertToCommentData(reply, comment);
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
