package com.wilqor.workshop.bytebay.lucene.source.model;


public class SimpleReview {
    public static final String USER_NAME_FIELD = "user_name";
    public static final String THUMB_FIELD = "thumb";
    public static final String ARTICLE_NAME_FIELD = "article_name";

    protected String userName;
    protected Thumb thumb;
    protected String articleName;

    public SimpleReview() {
    }

    public String getUserName() {
        return this.userName;
    }

    public Thumb getThumb() {
        return this.thumb;
    }

    public String getArticleName() {
        return this.articleName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setThumb(Thumb thumb) {
        this.thumb = thumb;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SimpleReview)) return false;
        final SimpleReview other = (SimpleReview) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$userName = this.getUserName();
        final Object other$userName = other.getUserName();
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) return false;
        final Object this$thumb = this.getThumb();
        final Object other$thumb = other.getThumb();
        if (this$thumb == null ? other$thumb != null : !this$thumb.equals(other$thumb)) return false;
        final Object this$articleName = this.getArticleName();
        final Object other$articleName = other.getArticleName();
        if (this$articleName == null ? other$articleName != null : !this$articleName.equals(other$articleName))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $userName = this.getUserName();
        result = result * PRIME + ($userName == null ? 43 : $userName.hashCode());
        final Object $thumb = this.getThumb();
        result = result * PRIME + ($thumb == null ? 43 : $thumb.hashCode());
        final Object $articleName = this.getArticleName();
        result = result * PRIME + ($articleName == null ? 43 : $articleName.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SimpleReview;
    }

    public String toString() {
        return "SimpleReview(userName=" + this.getUserName() + ", thumb=" + this.getThumb() + ", articleName=" + this.getArticleName() + ")";
    }
}
