- 界面效果图（实际上是动态的）
![效果图](https://github.com/ly931126/TestLeftOrRight/blob/master/picture/device-2017-04-06-114242.png)
#### ViewFlipper实现渐显按钮滑动的使用步骤
---
##### 1.首先要在清单文件中添加权限
```
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
```
##### 2.在布局文件中加入你要显示的各个界面图片
```
<ViewFlipper
        android:id="@+id/my_viewFlipper"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
//这是其中的一页图片，可以在ViewFlipper中添加多个
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:gravity="center"
            >

            <ImageView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:gravity="center"
                android:src="@drawable/shiguang_first"
                />
        </LinearLayout>
        </ViewFlipper>
```
#### 3.左右悬浮按钮的初始化
- （1）声明两个按钮，分别表示向左或向右滑动
         private ImageView mBtnLeft = null;
         private ImageView mBtnRight = null;
   （2）初始化悬浮按钮，并创建左右悬浮按钮
 ```
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
     创建左右按钮的方法请参考代码
 ```
####  4.设置按钮渐显效果，要用到Handler传递消息，并在它的handleMessage()方法中设置悬浮按钮的透明度
#### 5.在onTouchEvent方法中，设置当手指按下和移动时悬浮按钮显现，当手指离开时悬浮按钮隐藏
 ``` @Override
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
 ```
#### 6.当界面销毁时，销毁悬浮按钮的窗口
 ``` @Override
            protected void onDestroy() {
                super.onDestroy();
                //在程序退出时销毁窗口
                mManager.removeView(mBtnLeft);
                mManager.removeView(mBtnRight);
            }
 ```
