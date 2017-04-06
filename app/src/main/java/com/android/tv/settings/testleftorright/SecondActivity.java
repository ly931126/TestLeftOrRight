package com.android.tv.settings.testleftorright;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ViewFlipper;

/**
 * android实现带渐显按钮的左右滑动 注：自然状态下按钮不可见，触摸屏幕按钮显现
 */
public class SecondActivity extends AppCompatActivity {
    private static final String TAG = SecondActivity.class.getSimpleName();
    // 声明两个按钮，分别表示向左或向右滑动
    private ImageView mBtnLeft = null;
    private ImageView mBtnRight = null;
    private WindowManager mManager = null;
    private WindowManager.LayoutParams mParams = null;
    private ViewFlipper mFlipper = null;
    // ImageView的alpha的值
    private int mAlpha = 0;
    // 判断渐显按钮是否隐藏
    private boolean mIsHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mFlipper = (ViewFlipper) findViewById(R.id.my_viewFlipper);
        // 初始化悬浮按钮
        initImageViewSuspend();
    }

    private void initImageViewSuspend() {
        // 获取WindowManager
        mManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // 设置LayoutParams相关参数
        mParams = new WindowManager.LayoutParams();
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        // 设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        // 设置window flag参数
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 设置x y初始值
        mParams.x = 0;
        mParams.y = 0;
        // 设置窗口长宽
        mParams.width = 50;
        mParams.height = 50;
        // 创建左右按钮
        createLeftButtonView();
        createRightButtonView();
    }

    private void createRightButtonView() {
        mBtnLeft = new ImageView(this);
        mBtnLeft.setImageResource(R.drawable.left_button);
        mBtnLeft.setAlpha(0);
        // 上一个图像
        mBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlipper.setInAnimation(SecondActivity.this, R.anim.push_left_in);
                mFlipper.setOutAnimation(SecondActivity.this, R.anim.push_left_out);
                mFlipper.showPrevious();
            }
        });
        // 调整窗口，
        mParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        // 显示图像
        mManager.addView(mBtnLeft, mParams);

    }

    private void createLeftButtonView() {
        mBtnRight = new ImageView(this);
        mBtnRight.setImageResource(R.drawable.right_btn);
        mBtnRight.setAlpha(0);
        mBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlipper.setInAnimation(SecondActivity.this, R.anim.push_right_in);
                mFlipper.setOutAnimation(SecondActivity.this, R.anim.push_right_out);
                mFlipper.showNext();
            }
        });
        // 调整窗口，
        mParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        // 显示图像
        mManager.addView(mBtnRight, mParams);
    }

    /*
     * 设置按钮渐显效果
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1 && mAlpha < 255) {
                // 通过设置不透明度设置按钮的渐显效果
                mAlpha += 50;
                if (mAlpha > 255)
                    mAlpha = 255;
                mBtnLeft.setAlpha(mAlpha);
                mBtnLeft.invalidate();
                mBtnRight.setAlpha(mAlpha);
                mBtnRight.invalidate();
                if (!mIsHide && mAlpha < 255)
                    mHandler.sendEmptyMessageDelayed(1, 100);
            } else if (msg.what == 0 && mAlpha > 0) {
                mAlpha -= 10;
                if (mAlpha < 0)
                    mAlpha = 0;
                mBtnLeft.setAlpha(mAlpha);
                mBtnLeft.invalidate();
                mBtnRight.setAlpha(mAlpha);
                mBtnRight.invalidate();
                if (mIsHide && mAlpha > 0)
                    mHandler.sendEmptyMessageDelayed(0, 800);
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                showImageButtonView();
                break;
            case MotionEvent.ACTION_UP:
                hideImageButtonView();
                break;
        }
        return true;
    }

    private void hideImageButtonView() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    mIsHide = true;
                    mHandler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void showImageButtonView() {
        mIsHide = false;
        mHandler.sendEmptyMessage(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在程序退出时销毁窗口
        mManager.removeView(mBtnLeft);
        mManager.removeView(mBtnRight);
    }
}
