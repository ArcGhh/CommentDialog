package com.test.commentdialog.multi;

import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.test.commentdialog.R;
import com.test.commentdialog.bean.CommentEntity;
import com.test.commentdialog.bean.FirstLevelBean;
import com.test.commentdialog.bean.SecondLevelBean;
import com.test.commentdialog.util.TimeUtils;
import com.test.commentdialog.widget.TextClickSpans;
import com.test.commentdialog.widget.TextMovementMethods;

import java.util.List;

/**
 * @author ganhuanhui
 * 时间：2019/12/13 0013
 * 描述：
 */
public class CommentDialogMutiAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public CommentDialogMutiAdapter(List list) {
        super(list);
        addItemType(CommentEntity.TYPE_COMMENT_PARENT, R.layout.item_comment_new);
        addItemType(CommentEntity.TYPE_COMMENT_CHILD, R.layout.item_comment_child_new);
        addItemType(CommentEntity.TYPE_COMMENT_MORE, R.layout.item_comment_new_more);
        addItemType(CommentEntity.TYPE_COMMENT_EMPTY, R.layout.item_comment_empty);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case CommentEntity.TYPE_COMMENT_PARENT:
                bindCommentParent(helper, (FirstLevelBean) item);
                break;
            case CommentEntity.TYPE_COMMENT_CHILD:
                bindCommentChild(helper, (SecondLevelBean) item);
                break;
            case CommentEntity.TYPE_COMMENT_MORE:
                bindCommonMore(helper, item);
                break;
            case CommentEntity.TYPE_COMMENT_EMPTY:
                bindEmpty(helper, item);
                break;
        }
    }

    private void bindCommentParent(BaseViewHolder helper, FirstLevelBean item) {
        LinearLayout ll_like = helper.getView(R.id.ll_like);
        RelativeLayout rl_group = helper.getView(R.id.rl_group);
        RoundedImageView iv_header = helper.getView(R.id.iv_header);
        ImageView iv_like = helper.getView(R.id.iv_like);
        TextView tv_like_count = helper.getView(R.id.tv_like_count);
        TextView tv_content = helper.getView(R.id.tv_content);

        ll_like.setOnClickListener(null);
        ll_like.setTag(item.getItemType());
        helper.addOnClickListener(R.id.ll_like);

        rl_group.setTag(item.getItemType());
        helper.addOnClickListener(R.id.rl_group);
        iv_like.setImageResource(item.getIsLike() == 0 ? R.mipmap.icon_topic_post_item_like : R.mipmap.icon_topic_post_item_like_blue);
        tv_like_count.setText(item.getLikeCount() + "");
        tv_like_count.setVisibility(item.getLikeCount() <= 0 ? View.GONE : View.VISIBLE);

        String time = TimeUtils.getRecentTimeSpanByNow(item.getCreateTime());
        helper.setText(R.id.tv_time, time);
        helper.setText(R.id.tv_user_name, TextUtils.isEmpty(item.getUserName()) ? " " : item.getUserName());

        String contents = TextUtils.isEmpty(item.getContent()) ? " " : item.getContent();
        tv_content.setText(contents);

        Glide.with(mContext).load(item.getHeadImg()).into(iv_header);

    }

    private void bindCommentChild(final BaseViewHolder helper, SecondLevelBean item) {
        LinearLayout ll_like = helper.getView(R.id.ll_like);
        RelativeLayout rl_group = helper.getView(R.id.rl_group);
        RoundedImageView iv_header = helper.getView(R.id.iv_header);
        ImageView iv_like = helper.getView(R.id.iv_like);
        TextView tv_like_count = helper.getView(R.id.tv_like_count);
        TextView tv_content = helper.getView(R.id.tv_content);

        ll_like.setOnClickListener(null);
        ll_like.setTag(item.getItemType());
        helper.addOnClickListener(R.id.ll_like);

        rl_group.setTag(item.getItemType());
        helper.addOnClickListener(R.id.rl_group);
        Glide.with(mContext).load(item.getHeadImg()).into(iv_header);
        iv_like.setImageResource(item.getIsLike() == 0 ? R.mipmap.icon_topic_post_item_like : R.mipmap.icon_topic_post_item_like_blue);
        tv_like_count.setText(item.getLikeCount() + "");
        tv_like_count.setVisibility(item.getLikeCount() <= 0 ? View.GONE : View.VISIBLE);

        final TextMovementMethods movementMethods = new TextMovementMethods();
        if (item.getIsReply() == 0) {
            tv_content.setText(item.getContent());
            tv_content.setMovementMethod(null);
        } else {
            SpannableString stringBuilder = makeReplyCommentSpan(item.getReplyUserName(), item.getReplyUserId(), item.getContent());
            tv_content.setText(stringBuilder);
            tv_content.setMovementMethod(movementMethods);

        }
        tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movementMethods.isSpanClick()) return;
                helper.itemView.performClick();
            }
        });


        String time = TimeUtils.getRecentTimeSpanByNow(item.getCreateTime());
        helper.setText(R.id.tv_time, time);
        helper.setText(R.id.tv_user_name, TextUtils.isEmpty(item.getUserName()) ? " " : item.getUserName());


    }

    private void bindCommonMore(BaseViewHolder helper, MultiItemEntity item) {
        LinearLayout ll = helper.getView(R.id.ll_group);
        ll.setTag(item.getItemType());
        helper.addOnClickListener(R.id.ll_group);
    }

    private void bindEmpty(BaseViewHolder helper, MultiItemEntity item) {

    }

    public SpannableString makeReplyCommentSpan(final String atSomeone, final String id, String commentContent) {
        String richText = String.format("回复 %s : %s", atSomeone, commentContent);
        SpannableString builder = new SpannableString(richText);
        if (!TextUtils.isEmpty(atSomeone)) {
            int childStart = 2;
            int childEnd = childStart + atSomeone.length() + 1;
            builder.setSpan(new TextClickSpans() {

                @Override
                public void onClick(@NonNull View widget) {
                    Toast.makeText(mContext, atSomeone + " id: " + id, Toast.LENGTH_LONG).show();
                }
            }, childStart, childEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

}
