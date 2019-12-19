package com.test.commentdialog.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.test.commentdialog.CommentApplication;
import com.test.commentdialog.R;
import com.test.commentdialog.bean.SecondLevelBean;
import com.test.commentdialog.util.SizeUtils;

import java.util.List;

/**
 * 自动添加多个view的LinearLayout
 */
public class VerticalCommentLayout extends LinearLayout implements
        ViewGroup.OnHierarchyChangeListener {

    private List<SecondLevelBean> mCommentBeans;

    private LayoutParams mLayoutParams;
    private SimpleWeakObjectPool<View> COMMENT_TEXT_POOL;
    private int mCommentVerticalSpace;
    private CommentItemClickListener onCommentItemClickListener;
    private int totalCount = 0, position;

    public void setOnCommentItemClickListener(CommentItemClickListener listener) {
        this.onCommentItemClickListener = listener;
    }

    public VerticalCommentLayout(Context context) {
        super(context);
        init();
    }

    public VerticalCommentLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalCommentLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        mCommentVerticalSpace = SizeUtils.dp2px(2f);
        COMMENT_TEXT_POOL = new SimpleWeakObjectPool<>();
        setOnHierarchyChangeListener(this);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void addCommentsWithLimit(List<SecondLevelBean> commentBeans, int limit, boolean more) {
        if (commentBeans == null) return;
        this.mCommentBeans = commentBeans;
        int oldCount = getChildCount();
        if (!more && oldCount > 0) {
            removeViewsInLayout(0, oldCount);
        }

        int showCount = limit > commentBeans.size() ? commentBeans.size() : limit;
        for (int i = 0; i < showCount; i++) {
            boolean hasChild = i < oldCount;
            View childView = hasChild ? getChildAt(i) : null;
            SecondLevelBean commentBean = commentBeans.get(i);
            if (childView == null) {
                childView = COMMENT_TEXT_POOL.get();
                if (childView == null) {
                    addViewInLayout(makeCommentItemView(commentBean, i),
                            i, generateMarginLayoutParams(i), true);
                } else {
                    addCommentItemView(childView, commentBean, i);
                }
            } else {
                updateCommentData(childView, commentBean, i);
            }
        }

        if (commentBeans.size() > 0) {
            addViewInLayout(makeMoreView(totalCount > showCount),
                    showCount, generateMarginLayoutParams(showCount), true);
        }

        requestLayout();

    }

    public void addComments(List<SecondLevelBean> commentBeans) {
        if (commentBeans == null) return;
        this.mCommentBeans = commentBeans;
        int oldCount = getChildCount();
        if (oldCount <= 0) {
            return;
        }
        if (oldCount > 0) {
            removeViewsInLayout(oldCount - 1, 1);
        }
        addCommentsWithLimit(commentBeans, commentBeans.size(), true);
    }


    /**
     * 更新指定的position的comment
     */
    public void updateTargetComment(int position, List<SecondLevelBean> commentBeans) {
        int oldCount = getChildCount();
        for (int i = 0; i < oldCount; i++) {
            if (i == position) {
                View childView = getChildAt(i);
                SecondLevelBean bean = commentBeans.get(i);
                if (bean == null || childView == null) continue;
                updateCommentData(childView, bean, i);
                break;
            }
        }
        requestLayout();
    }


    /**
     * 創建Comment item view
     */
    private View makeCommentItemView(SecondLevelBean bean, int index) {
        return makeContentView(bean, index);
    }


    /**
     * 添加需要的Comment View
     */
    private void addCommentItemView(View view, SecondLevelBean bean, int index) {
        View commentView = makeCommentItemView(bean, index);
        addViewInLayout(commentView, index, generateMarginLayoutParams(index), true);
    }

    /**
     * 更新comment list content
     */
    private void updateCommentData(View view, SecondLevelBean bean, int index) {
        bindViewData(view, bean);
    }

    private View makeContentView(SecondLevelBean content, int index) {
        View view = View.inflate(getContext(), R.layout.item_comment_single_child_new, null);
        bindViewData(view, content);
        return view;
    }

    private void bindViewData(View view, final SecondLevelBean content) {
        final RelativeLayout rl_group = view.findViewById(R.id.rl_group);
        LinearLayout ll_like = view.findViewById(R.id.ll_like);

        RoundedImageView iv_header = view.findViewById(R.id.iv_header);
        ImageView iv_like = view.findViewById(R.id.iv_like);
        TextView tv_like_count = view.findViewById(R.id.tv_like_count);
        TextView tv_user_name = view.findViewById(R.id.tv_user_name);
        TextView tv_content = view.findViewById(R.id.tv_content);

        Glide.with(iv_header.getContext()).load(content.getHeadImg()).into(iv_header);
        iv_like.setImageResource(content.getIsLike() == 0 ? R.mipmap.icon_topic_post_item_like : R.mipmap.icon_topic_post_item_like_blue);
        tv_like_count.setText(content.getLikeCount() + "");
        tv_like_count.setVisibility(content.getLikeCount() <= 0 ? View.GONE : View.VISIBLE);

        final TextMovementMethods movementMethods = new TextMovementMethods();
        if (content.getIsReply() == 0) {
            tv_content.setText(content.getContent());
            tv_content.setMovementMethod(null);
        } else {
            SpannableString stringBuilder = makeReplyCommentSpan(content.getReplyUserName(), content.getReplyUserId(), content.getContent());
            tv_content.setText(stringBuilder);
            tv_content.setMovementMethod(movementMethods);

        }
        tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movementMethods.isSpanClick()) return;
                rl_group.performClick();
            }
        });
        tv_user_name.setText(content.getUserName());

        rl_group.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCommentItemClickListener != null)
                    onCommentItemClickListener.onItemClick(v, content, position);
            }
        });

        ll_like.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCommentItemClickListener != null)
                    onCommentItemClickListener.onLikeClick(v, content, position);
            }
        });

    }

    private View makeMoreView(boolean isMore) {
        View view = View.inflate(getContext(), R.layout.item_comment_new_more, null);
        LinearLayout ll_group = view.findViewById(R.id.ll_group);
        if (isMore) {

            ll_group.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCommentItemClickListener != null)
                        onCommentItemClickListener.onMoreClick(v, position);
                }
            });

        }
        view.findViewById(R.id.iv_more).setVisibility(isMore ? View.VISIBLE : View.GONE);
        TextView tvMore = view.findViewById(R.id.tv_more);
        tvMore.setText(isMore ? "展开更多回复" : "没有更多回复了");
        return view;
    }

    private LayoutParams generateMarginLayoutParams(int index) {
        if (mLayoutParams == null) {
            mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (mCommentBeans != null && index > 0) {
            mLayoutParams.topMargin = (int) (mCommentVerticalSpace * 1.2f);
        }
        return mLayoutParams;
    }

    @Override
    public void onChildViewAdded(View parent, View child) {

    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        COMMENT_TEXT_POOL.put(child);
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
                    Toast.makeText(CommentApplication.getApplication(), atSomeone + " id: " + id, Toast.LENGTH_LONG).show();
                }
            }, childStart, childEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    public interface CommentItemClickListener {

        void onMoreClick(View layout, int position);

        void onItemClick(View view,SecondLevelBean bean, int position);

        void onLikeClick(View layout, SecondLevelBean bean, int position);

    }
}
