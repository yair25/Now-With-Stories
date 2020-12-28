package com.yair25.storiesview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import androidx.core.view.GestureDetectorCompat;

class StoryView extends ViewGroup implements GestureDetector.OnGestureListener {

    private static final String GESTURE_TAG = "Gestures";

    private GestureDetectorCompat mDetector;
    private GesturesListener mListener;
    private ImageView mImageView;

    public StoryView(Context context) {
        super(context);
        init(context);
    }

    public StoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mDetector = new GestureDetectorCompat(getContext(), this);
        mImageView = new ImageView(context);

        addView(mImageView);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mListener != null && mListener.wasLongPress()) {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                mListener.onLongPressFinished();
                return true;
            }
        }

        if(this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int xStart, int yStart, int xEnd, int yEnd) {
        int width = xEnd - xStart;
        int height = yEnd - yStart;

        for(int index = 0; index < getChildCount(); index++) {
            View child = getChildAt(index);
            if(child != null) {
                child.layout(0 ,0, width, height);
            }
        }
    }

    void setGesturesListener(GesturesListener listener) {
        mListener = listener;
    }

    void setImageUrl(String url) {
        Picasso.get()
                .load(url)
                .resize(1000, 1000)
                .centerInside()
                .into(mImageView);
    }

    //

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(GESTURE_TAG,"onDown");
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d(GESTURE_TAG, "onFling");
        final float xThreshold = getWidth() / 3.0f;
        float deltaX = Math.abs(event2.getX() - event1.getX());
        float deltaY = Math.abs(event2.getY() - event1.getY());

        if(mListener != null) {
            if(deltaY < deltaX) {
                if(xThreshold < deltaX) {
                    if(event1.getX() < event2.getX()) {
                        mListener.onLeftFling();
                    } else {
                        mListener.onRightFling();
                    }
                }
            } else {
                if(event1.getY() < event2.getY()) {
                    mListener.onDownFling();
                }
            }
        }

        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(GESTURE_TAG, "onLongPress");
        if(mListener != null) {
            mListener.onLongPress();
        }
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        Log.d(GESTURE_TAG, "onScroll");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(GESTURE_TAG, "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(GESTURE_TAG, "onSingleTapUp");
        if(event.getX() < getWidth() / 2f) {
            Log.d(GESTURE_TAG,"onDown: Left");
            if(mListener != null) {
                mListener.onLeftClick();
            }
        } else {
            Log.d(GESTURE_TAG,"onDown: right");
            if(mListener != null) {
                mListener.onRightClick();
            }
        }
        return true;
    }

    interface GesturesListener {
        void onLeftClick();
        void onRightClick();
        void onLeftFling();
        void onRightFling();
        void onDownFling();
        void onLongPress();
        void onLongPressFinished();
        boolean wasLongPress();
    }
}
