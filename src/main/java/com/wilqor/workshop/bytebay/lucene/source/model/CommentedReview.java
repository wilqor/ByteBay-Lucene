package com.wilqor.workshop.bytebay.lucene.source.model;

public class CommentedReview extends SimpleReview {
    public static final String COMMENT_FIELD = "comment";

    String comment;

    public String getComment() {
        return this.comment;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof CommentedReview)) return false;
        final CommentedReview other = (CommentedReview) o;
        if (!other.canEqual((Object) this)) return false;
        if (!super.equals(o)) return false;
        final Object this$comment = this.getComment();
        final Object other$comment = other.getComment();
        if (this$comment == null ? other$comment != null : !this$comment.equals(other$comment)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + super.hashCode();
        final Object $comment = this.getComment();
        result = result * PRIME + ($comment == null ? 43 : $comment.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CommentedReview;
    }

    public String toString() {
        return "CommentedReview(super=" + super.toString() + ", comment=" + this.getComment() + ")";
    }
}
