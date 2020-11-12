package com.test.commentdialog.single;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.test.commentdialog.R;
import com.test.commentdialog.bean.CommentEntity;
import com.test.commentdialog.bean.CommentMoreBean;
import com.test.commentdialog.bean.FirstLevelBean;
import com.test.commentdialog.bean.SecondLevelBean;
import com.test.commentdialog.dialog.InputTextMsgDialog;
import com.test.commentdialog.listener.SoftKeyBoardListener;
import com.test.commentdialog.multi.CommentMultiActivity;
import com.test.commentdialog.util.RecyclerViewUtil;
import com.test.commentdialog.widget.VerticalCommentLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ganhuanhui
 * 时间：2019/11/27 0027
 * 描述：仿抖音评论dialog 使用design的BottomSheetDialog
 */
public class CommentSingleActivity extends AppCompatActivity implements VerticalCommentLayout.CommentItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private List<FirstLevelBean> data = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private InputTextMsgDialog inputTextMsgDialog;
    private float slideOffset = 0;
    private String content = "我听见你的声音，有种特别的感觉。让我不断想，不敢再忘记你。如果真的有一天，爱情理想会实现，我会加倍努力好好对你，永远不改变";
    private CommentDialogSingleAdapter bottomSheetAdapter;
    private RecyclerView rv_dialog_lists;
    private long totalCount = 30;//总条数不得超过它
    private int offsetY;
    private RecyclerViewUtil mRecyclerViewUtil;
    private SoftKeyBoardListener mKeyBoardListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_multi);
        mRecyclerViewUtil = new RecyclerViewUtil();
        initData();
        showSheetDialog();

    }

    //初始化数据 在项目中是从服务器获取数据
    private void initData() {
        for (int i = 0; i < 10; i++) {
            FirstLevelBean firstLevelBean = new FirstLevelBean();
            firstLevelBean.setContent("第" + (i + 1) + "人评论内容" + (i % 3 == 0 ? content + (i + 1) + "次" : ""));
            firstLevelBean.setCreateTime(System.currentTimeMillis());
            firstLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3370302115,85956606&fm=26&gp=0.jpg");
            firstLevelBean.setId(i + "");
            firstLevelBean.setUserId("UserId" + i);
            firstLevelBean.setIsLike(0);
            firstLevelBean.setPosition(i);
            firstLevelBean.setLikeCount(i);
            firstLevelBean.setUserName("星梦缘" + (i + 1));

            List<SecondLevelBean> beanList = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                SecondLevelBean secondLevelBean = new SecondLevelBean();
                secondLevelBean.setContent("一级第" + (i + 1) + "人 二级第" + (j + 1) + "人评论内容" + (j % 3 == 0 ? content + (j + 1) + "次" : ""));
                secondLevelBean.setCreateTime(System.currentTimeMillis());
                secondLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
                secondLevelBean.setId(j + "");
                firstLevelBean.setUserId("ChildUserId" + i);
                secondLevelBean.setIsLike(0);
                secondLevelBean.setLikeCount(j);
                secondLevelBean.setUserName("星梦缘" + (i + 1) + "  " + (j + 1));
                secondLevelBean.setIsReply(j % 5 == 0 ? 1 : 0);
                secondLevelBean.setReplyUserName(j % 5 == 0 ? "闭嘴家族" + j : "");
                secondLevelBean.setPosition(i);
                secondLevelBean.setChildPosition(j);
                beanList.add(secondLevelBean);
            }
            firstLevelBean.setSecondLevelBeans(beanList);
            data.add(firstLevelBean);
        }
    }

    /**
     * 重新排列数据
     * 未解决滑动卡顿问题
     */
    private void sort() {
        int size = data.size();
        for (int i = 0; i < size; i++) {
            FirstLevelBean firstLevelBean = data.get(i);
            firstLevelBean.setPosition(i);

            List<SecondLevelBean> secondLevelBeans = firstLevelBean.getSecondLevelBeans();
            if (secondLevelBeans == null || secondLevelBeans.isEmpty()) continue;
            int count = secondLevelBeans.size();
            for (int j = 0; j < count; j++) {
                SecondLevelBean secondLevelBean = secondLevelBeans.get(j);
                secondLevelBean.setPosition(i);
                secondLevelBean.setChildPosition(j);
            }
        }

        bottomSheetAdapter.notifyDataSetChanged();

    }

    public void show(View view) {
        slideOffset = 0;
        bottomSheetDialog.show();
    }

    private void showSheetDialog() {
        if (bottomSheetDialog != null) {
            bottomSheetDialog.show();
            return;
        }
        View view = View.inflate(this, R.layout.dialog_bottomsheet, null);
        ImageView iv_dialog_close = (ImageView) view.findViewById(R.id.dialog_bottomsheet_iv_close);
        rv_dialog_lists = (RecyclerView) view.findViewById(R.id.dialog_bottomsheet_rv_lists);
        RelativeLayout rl_comment = view.findViewById(R.id.rl_comment);

        iv_dialog_close.setOnClickListener(v -> bottomSheetDialog.dismiss());

        rl_comment.setOnClickListener(v -> {
            CommentSingleActivity.this.initInputTextMsgDialog(null, false, null, -1);
        });

        bottomSheetAdapter = new CommentDialogSingleAdapter(this);
        bottomSheetAdapter.setNewData(data);
        rv_dialog_lists.setHasFixedSize(true);
        rv_dialog_lists.setLayoutManager(new LinearLayoutManager(this));
        rv_dialog_lists.setItemAnimator(new DefaultItemAnimator());
        bottomSheetAdapter.setLoadMoreView(new SimpleLoadMoreView());
        bottomSheetAdapter.setOnLoadMoreListener(this, rv_dialog_lists);
        rv_dialog_lists.setAdapter(bottomSheetAdapter);

        initListener();

        bottomSheetDialog = new BottomSheetDialog(this, R.style.dialog);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        final BottomSheetBehavior mDialogBehavior = BottomSheetBehavior.from((View) view.getParent());
        mDialogBehavior.setPeekHeight(getWindowHeight());
        mDialogBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                    bottomSheetDialog.dismiss();
                    mDialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    if (slideOffset <= -0.28) {
                        bottomSheetDialog.dismiss();
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                CommentSingleActivity.this.slideOffset = slideOffset;

            }
        });
    }

    private void initListener() {
        // 点击事件
        bottomSheetAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            FirstLevelBean firstLevelBean = bottomSheetAdapter.getData().get(position);
            if (firstLevelBean == null) return;
            if (view1.getId() == R.id.ll_like) {
                //一级评论点赞 项目中还得通知服务器 成功才可以修改
                firstLevelBean.setLikeCount(firstLevelBean.getLikeCount() + (firstLevelBean.getIsLike() == 0 ? 1 : -1));
                firstLevelBean.setIsLike(firstLevelBean.getIsLike() == 0 ? 1 : 0);
                data.set(position, firstLevelBean);
                bottomSheetAdapter.notifyItemChanged(firstLevelBean.getPosition());
            } else if (view1.getId() == R.id.rl_group) {
                //添加二级评论
                CommentSingleActivity.this.initInputTextMsgDialog((View) view1.getParent(), false, firstLevelBean.getHeadImg(), position);
            }
        });
        //滚动事件
        if (mRecyclerViewUtil != null) mRecyclerViewUtil.initScrollListener(rv_dialog_lists);
        mKeyBoardListener = new SoftKeyBoardListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override
            public void keyBoardHide(int height) {
                dismissInputDialog();
            }
        });
    }

    private void initInputTextMsgDialog(View view, final boolean isReply, final String headImg, final int position) {
        dismissInputDialog();
        if (view != null) {
            offsetY = view.getTop();
            scrollLocation(offsetY);
        }
        if (inputTextMsgDialog == null) {
            inputTextMsgDialog = new InputTextMsgDialog(this, R.style.dialog);
            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    addComment(isReply,headImg,position,msg);
                }

                @Override
                public void dismiss() {
                    scrollLocation(-offsetY);
                }
            });
        }
        showInputTextMsgDialog();
    }

    private void addComment(boolean isReply, String headImg, final int position, String msg) {
        if (position >= 0) {
            //添加二级评论
            SecondLevelBean secondLevelBean = new SecondLevelBean();
            FirstLevelBean firstLevelBean = bottomSheetAdapter.getData().get(position);
            secondLevelBean.setReplyUserName("replyUserName");
            secondLevelBean.setIsReply(isReply ? 1 : 0);
            secondLevelBean.setContent(msg);
            secondLevelBean.setHeadImg(headImg);
            secondLevelBean.setCreateTime(System.currentTimeMillis());
            secondLevelBean.setIsLike(0);
            secondLevelBean.setUserName("userName");
            secondLevelBean.setId(firstLevelBean.getSecondLevelBeans() + "");
            firstLevelBean.getSecondLevelBeans().add(secondLevelBean);

            data.set(firstLevelBean.getPosition(), firstLevelBean);
            bottomSheetAdapter.notifyDataSetChanged();
            rv_dialog_lists.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((LinearLayoutManager) rv_dialog_lists.getLayoutManager())
                            .scrollToPositionWithOffset(position == data.size() - 1 ? position
                                    : position + 1, position == data.size() - 1 ? Integer.MIN_VALUE : rv_dialog_lists.getHeight() / 2);
                }
            }, 100);

        } else {
            //添加一级评论
            FirstLevelBean firstLevelBean = new FirstLevelBean();
            firstLevelBean.setUserName("赵丽颖");
            firstLevelBean.setId(bottomSheetAdapter.getItemCount() + 1 + "");
            firstLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
            firstLevelBean.setCreateTime(System.currentTimeMillis());
            firstLevelBean.setContent(msg);
            firstLevelBean.setLikeCount(0);
            firstLevelBean.setSecondLevelBeans(new ArrayList<SecondLevelBean>());
            data.add(0, firstLevelBean);
            sort();
            rv_dialog_lists.scrollToPosition(0);
        }
    }

    private void dismissInputDialog() {
        if (inputTextMsgDialog != null) {
            if (inputTextMsgDialog.isShowing()) inputTextMsgDialog.dismiss();
            inputTextMsgDialog.cancel();
            inputTextMsgDialog = null;
        }
    }

    private void showInputTextMsgDialog() {
        inputTextMsgDialog.show();
    }

    private int getWindowHeight() {
        Resources res = getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    //在项目中是从服务器获取数据，其实就是二级评论分页获取
    @Override
    public void onMoreClick(View layout, int position) {
        FirstLevelBean firstLevelBean = data.get(position);
        List<SecondLevelBean> beans = firstLevelBean.getSecondLevelBeans();
        int size = beans.size();
        for (int i = size; i < size + 2; i++) {
            SecondLevelBean secondLevelBean = new SecondLevelBean();
            secondLevelBean.setContent("一级第" + (position + 1) + "人 二级第" + (i + 1) + "人评论内容" + (i % 3 == 0 ? content + (i + 1) + "次" : ""));
            secondLevelBean.setCreateTime(System.currentTimeMillis());
            secondLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
            secondLevelBean.setId(i + "");
            secondLevelBean.setIsLike(i % 2 == 0 ? 1 : 0);
            secondLevelBean.setLikeCount(i);
            secondLevelBean.setUserName("星梦缘" + (i + 1) + "  " + (i + 1));
            secondLevelBean.setIsReply(i % 5 == 0 ? 1 : 0);
            secondLevelBean.setReplyUserName(i % 5 == 0 ? "闭嘴家族" + (i + 1) : "");
            beans.add(secondLevelBean);
        }
        sort();
    }

    //添加二级评论（回复某人）
    @Override
    public void onItemClick(View layout, SecondLevelBean bean, int position) {
        initInputTextMsgDialog(layout, true, bean.getHeadImg(), position);
    }

    //二级评论点赞 本地数据更新喜欢状态
    // 在项目中是还需要通知服务器成功才可以修改本地数据
    @Override
    public void onLikeClick(View layout, SecondLevelBean bean, int position) {
        bean.setLikeCount(bean.getLikeCount() + (bean.getIsLike() == 1 ? -1 : 1));
        bean.setIsLike(bean.getIsLike() == 1 ? 0 : 1);
        data.get(bean.getPosition()).getSecondLevelBeans().set(bean.getChildPosition(), bean);
        bottomSheetAdapter.notifyItemChanged(bean.getPosition());
    }

    //在项目中是从服务器获取数据，其实就是一级评论分页获取
    @Override
    public void onLoadMoreRequested() {
        if (data.size() >= totalCount) {
            bottomSheetAdapter.loadMoreEnd(false);
            return;
        }
        //加载更多
        for (int i = 0; i < 10; i++) {
            FirstLevelBean firstLevelBean = new FirstLevelBean();
            firstLevelBean.setUserName("赵丽颖 add more" + i);
            firstLevelBean.setId(bottomSheetAdapter.getItemCount() + (i + 1) + "");
            firstLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
            firstLevelBean.setCreateTime(System.currentTimeMillis());
            firstLevelBean.setContent("add more" + i);
            firstLevelBean.setLikeCount(0);
            firstLevelBean.setSecondLevelBeans(new ArrayList<SecondLevelBean>());
            data.add(firstLevelBean);
        }
        sort();
        bottomSheetAdapter.loadMoreComplete();
    }

    // item滑动到原位
    public void scrollLocation(int offsetY) {
        try {
            rv_dialog_lists.smoothScrollBy(0, offsetY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRecyclerViewUtil != null){
            mRecyclerViewUtil.destroy();
            mRecyclerViewUtil = null;
        }
        if (mKeyBoardListener != null) {
            mKeyBoardListener.setOnSoftKeyBoardChangeListener(null);
            mKeyBoardListener = null;
        }
        bottomSheetAdapter = null;
    }
}
