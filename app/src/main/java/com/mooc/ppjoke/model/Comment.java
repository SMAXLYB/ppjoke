package com.mooc.ppjoke.model;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;

import java.io.Serializable;

public class Comment implements Serializable {
    public static final int COMMENT_TYPE_VIDEO = 3;
    public static final int COMMENT_TYPE_IMAGE_TEXT = 2;

    public int id;
    public long itemId;
    public long commentId;
    public long userId;
    public int commentType;
    public long createTime;
    public int commentCount;
    public int likeCount;
    public String commentText;
    public String imageUrl;
    public String videoUrl;
    public int width;
    public int height;
    public boolean hasLiked;
    public User author;
    public Ugc ugc;

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof Comment))
            return false;

        Comment newComment = (Comment) obj;
        return likeCount == newComment.likeCount
                && hasLiked == newComment.hasLiked
                && (author != null && author.equals(newComment.author))
                && (ugc != null && ugc.equals(newComment.ugc));
    }
}
