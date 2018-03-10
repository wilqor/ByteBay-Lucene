package com.wilqor.workshop.bytebay.lucene.source.model;

public class CommentedReviewWithTimestamp extends CommentedReview {

    public static String TIMESTAMP_FIELD = "timestamp";
    long timestamp;

    public long getTimestamp() {
        return this.timestamp;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof CommentedReviewWithTimestamp)) return false;
        final CommentedReviewWithTimestamp other = (CommentedReviewWithTimestamp) o;
        if (!other.canEqual((Object) this)) return false;
        if (!super.equals(o)) return false;
        if (this.getTimestamp() != other.getTimestamp()) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + super.hashCode();
        final long $timestamp = this.getTimestamp();
        result = result * PRIME + (int) ($timestamp >>> 32 ^ $timestamp);
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CommentedReviewWithTimestamp;
    }

    public String toString() {
        return "CommentedReviewWithTimestamp(super=" + super.toString() + ", timestamp=" + this.getTimestamp() + ")";
    }
}
