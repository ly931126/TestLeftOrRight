package com.android.tv.settings.testleftorright;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

/**
 * android实现左右滑动
 */
public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewFlipper mViewFlipper = null;
    private GestureDetector mDetector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mDetector = new GestureDetector((GestureDetector.OnGestureListener) MainActivity.this);
        mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        mViewFlipper.addView(addImageView(R.drawable.shiguang_first));
        mViewFlipper.addView(addImageView(R.drawable.shiguang_two));
        mViewFlipper.addView(addImageView(R.drawable.shiguang_three));
        mViewFlipper.addView(addImageView(R.drawable.shiguang_four));
        mViewFlipper.addView(addImageView(R.drawable.shiguang_five));
    }

    private View addImageView(int id) {
        ImageView image = new ImageView(this);
        image.setImageResource(id);
        return image;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    /**
     *
     * @param e1 e1为第一次按下的事件
     * @param e2 为用户拖动完离开屏幕时的点
     * @param velocityX 为离开屏幕时的初始速度
     * @param velocityY 为离开屏幕时的初始速度
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > 120) {
            this.mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
            this.mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
            this.mViewFlipper.showNext();
            return true;
        } else if (e1.getX() - e2.getX() < -120) {
            this.mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
            this.mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
            this.mViewFlipper.showPrevious();
            return true;
        }

        return false;
    }
}
