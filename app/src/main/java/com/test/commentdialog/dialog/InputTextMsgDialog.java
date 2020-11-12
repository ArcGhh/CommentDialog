package com.test.commentdialog.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.commentdialog.CommentApplication;
import com.test.commentdialog.R;

/**
 * @author ganhuanhui
 * 时间：2019/12/11 0011
 * 描述：https://blog.csdn.net/qq_32518491/article/details/85000421
 */
public class InputTextMsgDialog extends AppCompatDialog {
    private Context mContext;
    private InputMethodManager imm;
    private EditText messageTextView;
    private RelativeLayout rlDlg;
    private int mLastDiff = 0;
    private TextView tvNumber;
    private int maxNumber = 100;

    public interface OnTextSendListener {

        void onTextSend(String msg);

        void dismiss();
    }

    private OnTextSendListener mOnTextSendListener;

    public InputTextMsgDialog(@NonNull Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        init();
        setLayout();
    }

    /**
     * 最大输入字数  默认100
     */
    @SuppressLint("SetTextI18n")
    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
        tvNumber.setText("0/" + maxNumber);
    }

    /**
     * 设置输入提示文字
     */
    public void setHint(String text) {
        messageTextView.setHint(text);
    }

    private void init() {
        setContentView(R.layout.dialog_input_text_msg);
        messageTextView = (EditText) findViewById(R.id.et_input_message);
        tvNumber = (TextView) findViewById(R.id.tv_test);
        final LinearLayout rldlgview = (LinearLayout) findViewById(R.id.rl_inputdlg_view);
        messageTextView.requestFocus();
        messageTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvNumber.setText(editable.length() + "/" + maxNumber);
            }
        });

        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        ImageView iv_confirm = findViewById(R.id.iv_confirm);
        iv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = messageTextView.getText().toString().trim();
                if (msg.length() > maxNumber) {
                    Toast.makeText(mContext, "超过最大字数限制" + maxNumber, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!TextUtils.isEmpty(msg)) {
                    mOnTextSendListener.onTextSend(msg);
                    imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                    messageTextView.setText("");
                    InputTextMsgDialog.this.dismiss();
                } else {
                    Toast.makeText(CommentApplication.getApplication(), "请输入文字", Toast.LENGTH_LONG).show();
//                Toast.makeText(mContext, "", Toast.LENGTH_LONG).show();
                }
                messageTextView.setText(null);
            }
        });

        messageTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case KeyEvent.KEYCODE_ENDCALL:
                    case KeyEvent.KEYCODE_ENTER:
                        if (messageTextView.getText().length() > maxNumber) {
                            Toast.makeText(mContext, "超过最大字数限制", Toast.LENGTH_LONG).show();
                            return true;
                        }
                        if (messageTextView.getText().length() > 0) {
                            imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                            InputTextMsgDialog.this.dismiss();
                        } else {
                            Toast.makeText(mContext, "请输入文字", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    case KeyEvent.KEYCODE_BACK:
                        InputTextMsgDialog.this.dismiss();
                        return false;
                    default:
                        return false;
                }
            }
        });

        messageTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("My test", "onKey " + keyEvent.getCharacters());
                return false;
            }
        });

        rldlgview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                Rect r = new Rect();
                //获取当前界面可视部分
                InputTextMsgDialog.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = InputTextMsgDialog.this.getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;

                if (heightDifference <= 0 && mLastDiff > 0) {
                    InputTextMsgDialog.this.dismiss();
                }
                mLastDiff = heightDifference;
            }
        });

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
                    InputTextMsgDialog.this.dismiss();
                }
                return false;
            }
        });
    }

    private void setLayout() {
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(p);
        setCancelable(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


    public void setmOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        //dismiss之前重置mLastDiff值避免下次无法打开
        mLastDiff = 0;
        if (mOnTextSendListener!=null) mOnTextSendListener.dismiss();

    }

    @Override
    public void show() {
        super.show();
    }
}
