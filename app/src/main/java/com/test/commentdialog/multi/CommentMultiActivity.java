package com.test.commentdialog.multi;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.test.commentdialog.R;
import com.test.commentdialog.bean.CommentEntity;
import com.test.commentdialog.bean.CommentMoreBean;
import com.test.commentdialog.bean.FirstLevelBean;
import com.test.commentdialog.bean.SecondLevelBean;
import com.test.commentdialog.dialog.InputTextMsgDialog;
import com.test.commentdialog.listener.SoftKeyBoardListener;
import com.test.commentdialog.util.RecyclerViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ganhuanhui
 * 时间：2019/11/27 0027
 * 描述：仿抖音评论dialog 使用design的BottomSheetDialog
 */
public class CommentMultiActivity extends AppCompatActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    private List<MultiItemEntity> data = new ArrayList<>();
    private List<FirstLevelBean> datas = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private InputTextMsgDialog inputTextMsgDialog;
    private float slideOffset = 0;
    private String content = "我听见你的声音，有种特别的感觉。让我不断想，不敢再忘记你。如果真的有一天，爱情理想会实现，我会加倍努力好好对你，永远不改变";
    private CommentDialogMutiAdapter bottomSheetAdapter;
    private RecyclerView rv_dialog_lists;
    private long totalCount = 22;
    private int offsetY;
    private int positionCount = 0;
    private RecyclerViewUtil mRecyclerViewUtil;
    private SoftKeyBoardListener mKeyBoardListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_multi);
        mRecyclerViewUtil = new RecyclerViewUtil();
        initData();
        dataSort(0);
        showSheetDialog();

    }

    private void initRefresh() {
        datas.clear();
        initData();
        dataSort(0);
        bottomSheetAdapter.setNewData(data);
    }

    //原始数据 一般是从服务器接口请求过来的
    private void initData() {
        int size = 10;
        for (int i = 0; i < size; i++) {
            FirstLevelBean firstLevelBean = new FirstLevelBean();
            firstLevelBean.setContent("第" + (i + 1) + "人评论内容" + (i % 3 == 0 ? content + (i + 1) + "次" : ""));
            firstLevelBean.setCreateTime(System.currentTimeMillis());
            firstLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3370302115,85956606&fm=26&gp=0.jpg");
            firstLevelBean.setId(i + "");
            firstLevelBean.setIsLike(0);
            firstLevelBean.setLikeCount(i);
            firstLevelBean.setUserName("星梦缘" + (i + 1));
            firstLevelBean.setTotalCount(i + size);

            List<SecondLevelBean> beans = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                SecondLevelBean secondLevelBean = new SecondLevelBean();
                secondLevelBean.setContent("一级第" + (i + 1) + "人 二级第" + (j + 1) + "人评论内容" + (j % 3 == 0 ? content + (j + 1) + "次" : ""));
                secondLevelBean.setCreateTime(System.currentTimeMillis());
                secondLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
                secondLevelBean.setId(j + "");
                secondLevelBean.setIsLike(0);
                secondLevelBean.setLikeCount(j);
                secondLevelBean.setUserName("星梦缘" + (i + 1) + "  " + (j + 1));
                secondLevelBean.setIsReply(j % 5 == 0 ? 1 : 0);
                secondLevelBean.setReplyUserName(j % 5 == 0 ? "闭嘴家族" + j : "");
                secondLevelBean.setTotalCount(firstLevelBean.getTotalCount());
                beans.add(secondLevelBean);
                firstLevelBean.setSecondLevelBeans(beans);
            }
            datas.add(firstLevelBean);
        }
    }

    /**
     * 对数据重新进行排列
     * 目的是为了让一级评论和二级评论同为item
     * 解决滑动卡顿问题
     *
     * @param position
     */
    private void dataSort(int position) {
        if (datas.isEmpty()) {
            data.add(new MultiItemEntity() {
                @Override
                public int getItemType() {
                    return CommentEntity.TYPE_COMMENT_EMPTY;
                }
            });
            return;
        }

        if (position <= 0) data.clear();
        int posCount = data.size();
        int count = datas.size();
        for (int i = 0; i < count; i++) {
            if (i < position) continue;

            //一级评论
            FirstLevelBean firstLevelBean = datas.get(i);
            if (firstLevelBean == null) continue;
            firstLevelBean.setPosition(i);
            posCount += 2;
            List<SecondLevelBean> secondLevelBeans = firstLevelBean.getSecondLevelBeans();
            if (secondLevelBeans == null || secondLevelBeans.isEmpty()) {
                firstLevelBean.setPositionCount(posCount);
                data.add(firstLevelBean);
                continue;
            }
            int beanSize = secondLevelBeans.size();
            posCount += beanSize;
            firstLevelBean.setPositionCount(posCount);
            data.add(firstLevelBean);

            //二级评论
            for (int j = 0; j < beanSize; j++) {
                SecondLevelBean secondLevelBean = secondLevelBeans.get(j);
                secondLevelBean.setChildPosition(j);
                secondLevelBean.setPosition(i);
                secondLevelBean.setPositionCount(posCount);
                data.add(secondLevelBean);
            }

            //展示更多的item
            if (beanSize <= 18) {
                CommentMoreBean moreBean = new CommentMoreBean();
                moreBean.setPosition(i);
                moreBean.setPositionCount(posCount);
                moreBean.setTotalCount(firstLevelBean.getTotalCount());
                data.add(moreBean);
            }

        }
    }

    public void show(View view) {
        bottomSheetAdapter.notifyDataSetChanged();
        slideOffset = 0;
        bottomSheetDialog.show();
    }


    private void showSheetDialog() {
        if (bottomSheetDialog != null) {
            return;
        }

        //view
        View view = View.inflate(this, R.layout.dialog_bottomsheet, null);
        ImageView iv_dialog_close = (ImageView) view.findViewById(R.id.dialog_bottomsheet_iv_close);
        rv_dialog_lists = (RecyclerView) view.findViewById(R.id.dialog_bottomsheet_rv_lists);
        RelativeLayout rl_comment = view.findViewById(R.id.rl_comment);
        iv_dialog_close.setOnClickListener(v -> bottomSheetDialog.dismiss());
        rl_comment.setOnClickListener(v -> {
            //添加二级评论
            initInputTextMsgDialog(null, false, null, -1);
        });

        //adapter
        bottomSheetAdapter = new CommentDialogMutiAdapter(data);
        rv_dialog_lists.setHasFixedSize(true);
        rv_dialog_lists.setLayoutManager(new LinearLayoutManager(this));
        closeDefaultAnimator(rv_dialog_lists);
        bottomSheetAdapter.setOnLoadMoreListener(this, rv_dialog_lists);
        rv_dialog_lists.setAdapter(bottomSheetAdapter);

        //dialog
        bottomSheetDialog = new BottomSheetDialog(this, R.style.dialog);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        BottomSheetBehavior mDialogBehavior = BottomSheetBehavior.from((View) view.getParent());
        mDialogBehavior.setPeekHeight(getWindowHeight());
        //dialog滑动监听
        mDialogBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mDialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    if (slideOffset <= -0.28) {
                        //当向下滑动时 值为负数
                        bottomSheetDialog.dismiss();
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                CommentMultiActivity.this.slideOffset = slideOffset;//记录滑动值
            }
        });

        initListener();
    }

    private void initListener() {
        // 点击事件
        bottomSheetAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view1, int position) {
                switch ((int) view1.getTag()) {
                    case CommentEntity.TYPE_COMMENT_PARENT:
                        if (view1.getId() == R.id.rl_group) {
                            //添加二级评论
                            CommentMultiActivity.this.initInputTextMsgDialog((View) view1.getParent(), false, bottomSheetAdapter.getData().get(position), position);
                        } else if (view1.getId() == R.id.ll_like) {
                            //一级评论点赞 项目中还得通知服务器 成功才可以修改
                            FirstLevelBean bean = (FirstLevelBean) bottomSheetAdapter.getData().get(position);
                            bean.setLikeCount(bean.getLikeCount() + (bean.getIsLike() == 0 ? 1 : -1));
                            bean.setIsLike(bean.getIsLike() == 0 ? 1 : 0);
                            datas.set(bean.getPosition(), bean);
                            CommentMultiActivity.this.dataSort(0);
                            bottomSheetAdapter.notifyDataSetChanged();
                        }
                        break;
                    case CommentEntity.TYPE_COMMENT_CHILD:

                        if (view1.getId() == R.id.rl_group) {
                            //添加二级评论（回复）
                            CommentMultiActivity.this.initInputTextMsgDialog(view1, true, bottomSheetAdapter.getData().get(position), position);
                        } else if (view1.getId() == R.id.ll_like) {
                            //二级评论点赞 项目中还得通知服务器 成功才可以修改
                            SecondLevelBean bean = (SecondLevelBean) bottomSheetAdapter.getData().get(position);
                            bean.setLikeCount(bean.getLikeCount() + (bean.getIsLike() == 0 ? 1 : -1));
                            bean.setIsLike(bean.getIsLike() == 0 ? 1 : 0);

                            List<SecondLevelBean> secondLevelBeans = datas.get((int) bean.getPosition()).getSecondLevelBeans();
                            secondLevelBeans.set(bean.getChildPosition(), bean);
//                            CommentMultiActivity.this.dataSort(0);
                            bottomSheetAdapter.notifyDataSetChanged();
                        }

                        break;
                    case CommentEntity.TYPE_COMMENT_MORE:
                        //在项目中是从服务器获取数据，其实就是二级评论分页获取
                        CommentMoreBean moreBean = (CommentMoreBean) bottomSheetAdapter.getData().get(position);
                        SecondLevelBean secondLevelBean = new SecondLevelBean();
                        secondLevelBean.setContent("more comment" + 1);
                        secondLevelBean.setCreateTime(System.currentTimeMillis());
                        secondLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
                        secondLevelBean.setId(1 + "");
                        secondLevelBean.setIsLike(0);
                        secondLevelBean.setLikeCount(0);
                        secondLevelBean.setUserName("星梦缘" + 1);
                        secondLevelBean.setIsReply(0);
                        secondLevelBean.setReplyUserName("闭嘴家族" + 1);
                        secondLevelBean.setTotalCount(moreBean.getTotalCount() + 1);

                        datas.get((int) moreBean.getPosition()).getSecondLevelBeans().add(secondLevelBean);
                        CommentMultiActivity.this.dataSort(0);
                        bottomSheetAdapter.notifyDataSetChanged();

                        break;
                    case CommentEntity.TYPE_COMMENT_EMPTY:
                        CommentMultiActivity.this.initRefresh();
                        break;

                }

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

    private void initInputTextMsgDialog(View view, final boolean isReply, final MultiItemEntity item, final int position) {
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
                    addComment(isReply, item, position, msg);
                }

                @Override
                public void dismiss() {
                    //item滑动到原位
                    scrollLocation(-offsetY);
                }
            });
        }
        showInputTextMsgDialog();
    }

    //添加评论
    private void addComment(boolean isReply, MultiItemEntity item, final int position, String msg) {
        final String userName = "hui";
        if (position >= 0) {
            //添加二级评论
            int pos = 0;
            String replyUserName = "未知";
            if (item instanceof FirstLevelBean) {
                FirstLevelBean firstLevelBean = (FirstLevelBean) item;
                positionCount = (int) (firstLevelBean.getPositionCount() + 1);
                pos = (int) firstLevelBean.getPosition();
                replyUserName = firstLevelBean.getUserName();
            } else if (item instanceof SecondLevelBean) {
                SecondLevelBean secondLevelBean = (SecondLevelBean) item;
                positionCount = (int) (secondLevelBean.getPositionCount() + 1);
                pos = (int) secondLevelBean.getPosition();
                replyUserName = secondLevelBean.getUserName();
            }

            SecondLevelBean secondLevelBean = new SecondLevelBean();
            secondLevelBean.setReplyUserName(replyUserName);
            secondLevelBean.setIsReply(isReply ? 1 : 0);
            secondLevelBean.setContent(msg);
            secondLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3370302115,85956606&fm=26&gp=0.jpg");
            secondLevelBean.setCreateTime(System.currentTimeMillis());
            secondLevelBean.setIsLike(0);
            secondLevelBean.setUserName(userName);
            secondLevelBean.setId("");
            secondLevelBean.setPosition(positionCount);

            datas.get(pos).getSecondLevelBeans().add(secondLevelBean);
            CommentMultiActivity.this.dataSort(0);
            bottomSheetAdapter.notifyDataSetChanged();
            rv_dialog_lists.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((LinearLayoutManager) rv_dialog_lists.getLayoutManager())
                            .scrollToPositionWithOffset(positionCount >= data.size() - 1 ? data.size() - 1
                                    : positionCount, positionCount >= data.size() - 1 ? Integer.MIN_VALUE : rv_dialog_lists.getHeight());
                }
            }, 100);

        } else {
            //添加一级评论
            FirstLevelBean firstLevelBean = new FirstLevelBean();
            firstLevelBean.setUserName(userName);
            firstLevelBean.setId(bottomSheetAdapter.getItemCount() + 1 + "");
            firstLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
            firstLevelBean.setCreateTime(System.currentTimeMillis());
            firstLevelBean.setContent(msg);
            firstLevelBean.setLikeCount(0);
            firstLevelBean.setSecondLevelBeans(new ArrayList<SecondLevelBean>());
            datas.add(0, firstLevelBean);
            CommentMultiActivity.this.dataSort(0);
            bottomSheetAdapter.notifyDataSetChanged();
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

    @Override
    public void onLoadMoreRequested() {
        if (datas.size() >= totalCount) {
            bottomSheetAdapter.loadMoreEnd(false);
            return;
        }
        FirstLevelBean firstLevelBean = new FirstLevelBean();
        firstLevelBean.setUserName("hui");
        firstLevelBean.setId((datas.size() + 1) + "");
        firstLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
        firstLevelBean.setCreateTime(System.currentTimeMillis());
        firstLevelBean.setContent("add loadmore comment");
        firstLevelBean.setLikeCount(0);
        firstLevelBean.setSecondLevelBeans(new ArrayList<SecondLevelBean>());
        datas.add(firstLevelBean);
        dataSort(datas.size() - 1);
        bottomSheetAdapter.notifyDataSetChanged();
        bottomSheetAdapter.loadMoreComplete();

    }

    // item滑动
    public void scrollLocation(int offsetY) {
        try {
            rv_dialog_lists.smoothScrollBy(0, offsetY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭默认局部刷新动画
     */
    public void closeDefaultAnimator(RecyclerView mRvCustomer) {
        if (null == mRvCustomer) return;
        mRvCustomer.getItemAnimator().setAddDuration(0);
        mRvCustomer.getItemAnimator().setChangeDuration(0);
        mRvCustomer.getItemAnimator().setMoveDuration(0);
        mRvCustomer.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) mRvCustomer.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    protected void onDestroy() {
        if (mRecyclerViewUtil != null){
            mRecyclerViewUtil.destroy();
            mRecyclerViewUtil = null;
        }
        if (mKeyBoardListener != null) {
            mKeyBoardListener.setOnSoftKeyBoardChangeListener(null);
            mKeyBoardListener = null;
        }
        bottomSheetAdapter = null;
        super.onDestroy();
    }


}
