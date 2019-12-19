package com.test.commentdialog.single;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.test.commentdialog.R;
import com.test.commentdialog.bean.FirstLevelBean;
import com.test.commentdialog.widget.VerticalCommentLayout;

/**
 * @author ganhuanhui
 * 时间：2019/12/19 0019
 * 描述：
 */
public class CommentDialogSingleAdapter extends BaseQuickAdapter<FirstLevelBean, BaseViewHolder> {

    private VerticalCommentLayout.CommentItemClickListener mItemClickListener;

    public CommentDialogSingleAdapter(VerticalCommentLayout.CommentItemClickListener mItemClickListener) {
        super(R.layout.item_comment_new);
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FirstLevelBean content) {

//            LinearLayout ll_like = helper.getView(R.id.ll_like);
//            RelativeLayout rl_group = helper.getView(R.id.rl_group);

        RoundedImageView iv_header = helper.getView(R.id.iv_header);
        ImageView iv_like = helper.getView(R.id.iv_like);
        TextView tv_like_count = helper.getView(R.id.tv_like_count);
        TextView tv_user_name = helper.getView(R.id.tv_user_name);
        TextView tv_content = helper.getView(R.id.tv_content);

        helper.addOnClickListener(R.id.rl_group);
        helper.addOnClickListener(R.id.ll_like);
        iv_like.setImageResource(content.getIsLike() == 0 ? R.mipmap.icon_topic_post_item_like : R.mipmap.icon_topic_post_item_like_blue);
        tv_like_count.setText(content.getLikeCount() + "");
        tv_like_count.setVisibility(content.getLikeCount() <= 0 ? View.GONE : View.VISIBLE);

        tv_content.setText(content.getContent());
        tv_user_name.setText(content.getUserName());

        Glide.with(mContext).load(content.getHeadImg()).into(iv_header);

        if (content.getSecondLevelBeans() != null) {
            VerticalCommentLayout commentWidget = helper.getView(R.id.verticalCommentLayout);
            commentWidget.setVisibility(View.VISIBLE);
            int size = content.getSecondLevelBeans().size();
            commentWidget.setTotalCount(size + 10);
            commentWidget.setPosition(helper.getAdapterPosition());
            commentWidget.setOnCommentItemClickListener(mItemClickListener);
            int limit = helper.getAdapterPosition() + 1;
            commentWidget.addCommentsWithLimit(content.getSecondLevelBeans(), size, false);
//                rl_group.setTag(commentWidget);
        }

    }
}
