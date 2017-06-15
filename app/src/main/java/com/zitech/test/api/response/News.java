package com.zitech.test.api.response;

/**
 * Created by lu on 2016/3/12.
 */
public class News {

    private String thumbnail;//: http://d.ifengimg.com/w132_h94_q75/y2.ifengimg.com/ifengimcp/pic/20160411/ea9338403f811d5b1bcc_size17_w308_h220.jpg,
    private String online;//: 1,
    private String title;//: 神州专车与阿里签署战略合作 阿里回应称没有股权,
    private String source;//: 凤凰科技,
    private String updateTime;//: 2016/04/11 11:55:57,
    private String id;//: http://api.iclient.ifeng.com/ipadtestdoc?aid=108105073,
    private String documentId;//: imcp_108105073,
    private String type;//: doc,
    private String commentsUrl;//: http://tech.ifeng.com/a/20160411/41592788_0.shtml,
    private String comments;//: 12,
    private String commentsall;//: 508,

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCommentsall() {
        return commentsall;
    }

    public void setCommentsall(String commentsall) {
        this.commentsall = commentsall;
    }
}
