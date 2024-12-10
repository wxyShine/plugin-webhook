package com.wxy97.webhook.bean;

import com.wxy97.webhook.util.DateUtil;
import java.util.Map;
import lombok.Data;
import run.halo.app.core.extension.content.Comment;
import run.halo.app.core.extension.content.Reply;

/**
 * Author: wxy97.com
 * Date: 2024/4/19 10:00
 * Description:
 */
@Data
public class CommentData {

    private Owner owner;

    private String content;

    private String createTime;

    private Boolean approved;

    private String refKind;

    private PostData postData;

    private SinglePageData singlePageData;

    private PluginData pluginData;

    private CommentData replyComment;


    @Data
    public static class Owner {
        private String kind;
        private String name;
        private String displayName;
        private Map<String, String> annotations;
    }

    public static CommentData convertToCommentData(Comment comment) {
        CommentData commentData = new CommentData();
        commentData.setContent(comment.getSpec().getContent());
        commentData.setCreateTime(DateUtil.format(comment.getSpec().getCreationTime()));
        commentData.setApproved(comment.getSpec().getApproved());
        commentData.setRefKind(comment.getSpec().getSubjectRef().getKind());

        Comment.CommentOwner commentOwner = comment.getSpec().getOwner();
        Owner o = new Owner();
        o.setKind(commentOwner.getKind());
        o.setName(commentOwner.getName());
        o.setDisplayName(commentOwner.getDisplayName());
        Map<String, String> annotations = commentOwner.getAnnotations();
        o.setAnnotations(annotations);
        commentData.setOwner(o);
        return commentData;
    }

    public static CommentData convertToCommentData(Reply reply, Comment comment) {
        CommentData commentData = convertToCommentData(comment);

        CommentData replyComment = new CommentData();
        Comment.CommentOwner commentOwner = reply.getSpec().getOwner();
        Owner o = new Owner();
        o.setKind(commentOwner.getKind());
        o.setName(commentOwner.getName());
        o.setDisplayName(commentOwner.getDisplayName());
        Map<String, String> annotations = commentOwner.getAnnotations();
        o.setAnnotations(annotations);
        replyComment.setOwner(o);
        replyComment.setContent(reply.getSpec().getContent());
        replyComment.setCreateTime(DateUtil.format(reply.getSpec().getCreationTime()));
        replyComment.setApproved(reply.getSpec().getApproved());

        commentData.setReplyComment(replyComment);
        return commentData;
    }

}
