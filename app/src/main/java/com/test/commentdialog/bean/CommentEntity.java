package com.test.commentdialog.bean;

import java.util.List;

/**
 * @author ganhuanhui
 * 时间：2019/12/11
 * 描述：
 */
public class CommentEntity {

    public static final int TYPE_COMMENT_PARENT = 1;
    public static final int TYPE_COMMENT_CHILD = 2;
    public static final int TYPE_COMMENT_MORE = 3;
    public static final int TYPE_COMMENT_EMPTY = 4;
    public static final int TYPE_COMMENT_OTHER = 5;

    private List<FirstLevelBean> firstLevelBeans;
    private long totalCount;

    public List<FirstLevelBean> getFirstLevelBeans() {
        return firstLevelBeans;
    }

    public void setFirstLevelBeans(List<FirstLevelBean> firstLevelBeans) {
        this.firstLevelBeans = firstLevelBeans;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"firstLevelBeans\":")
                .append(firstLevelBeans);
        sb.append(",\"totalCount\":")
                .append(totalCount);
        sb.append('}');
        return sb.toString();
    }
}
