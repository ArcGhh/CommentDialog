package com.test.commentdialog.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import static com.test.commentdialog.bean.CommentEntity.TYPE_COMMENT_CHILD;

/**
 * @author ganhuanhui
 * 时间：2019/12/11 0011
 * 描述：
 */
public class SecondLevelBean implements MultiItemEntity {

    //二级评论id
    private String id;
    //二级评论头像
    private String headImg;
    //二级评论的用户名
    private String userName;
    //二级评论的用户id
    private String userId;
    //回复评论人的用户名
    private String replyUserName;
    //回复评论人的用户id
    private String replyUserId;
    //评论内容（回复内容）
    private String content;
    //创建时间
    private long createTime;
    //点赞数量
    private long likeCount;
    //是否点赞了  0没有 1点赞
    private int isLike;
    //本条评论是否为回复
    private int isReply;
    //当前评论的总条数（包括这条一级评论）ps:处于未使用状态
    private long totalCount;
    //当前一级评论的位置（下标）
    private int position;
    //当前二级评论所在的位置(下标)
    private int positionCount;
    //当前二级评论所在一级评论条数的位置（下标）
    private int childPosition;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getReplyUserName() {
        return replyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getIsReply() {
        return isReply;
    }

    public void setIsReply(int isReply) {
        this.isReply = isReply;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPositionCount() {
        return positionCount;
    }

    public void setPositionCount(int positionCount) {
        this.positionCount = positionCount;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"headImg\":\"")
                .append(headImg).append('\"');
        sb.append(",\"userName\":\"")
                .append(userName).append('\"');
        sb.append(",\"userId\":\"")
                .append(userId).append('\"');
        sb.append(",\"replyUserName\":\"")
                .append(replyUserName).append('\"');
        sb.append(",\"replyUserId\":\"")
                .append(replyUserId).append('\"');
        sb.append(",\"content\":\"")
                .append(content).append('\"');
        sb.append(",\"createTime\":")
                .append(createTime);
        sb.append(",\"likeCount\":")
                .append(likeCount);
        sb.append(",\"isLike\":")
                .append(isLike);
        sb.append(",\"isReply\":")
                .append(isReply);
        sb.append(",\"totalCount\":")
                .append(totalCount);
        sb.append(",\"position\":")
                .append(position);
        sb.append(",\"positionCount\":")
                .append(positionCount);
        sb.append(",\"childPosition\":")
                .append(childPosition);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals( Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof SecondLevelBean)) return false;
        SecondLevelBean bean = (SecondLevelBean) obj;
        return bean.getId().equals(id);
    }

    @Override
    public int getItemType() {
        return TYPE_COMMENT_CHILD;
    }
}
