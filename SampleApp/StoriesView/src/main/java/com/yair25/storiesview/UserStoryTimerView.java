package com.yair25.storiesview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.HashMap;

import androidx.annotation.Nullable;

class UserStoryTimerView extends LinearLayout {

    private TimerListener mListener = null;
    private int mCurrentPos = 0;

    private final Handler mHandler = new Handler();
    private Thread mCurrentThread;

    public UserStoryTimerView(Context context) {
        super(context);
    }

    public UserStoryTimerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UserStoryTimerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mListener = null;
        if(mCurrentThread != null) {
            mCurrentThread.interrupt();
            mCurrentThread = null;
        }
    }

    void setTimerListener(TimerListener timerListener) {
        mListener = timerListener;
    }

    void startTimer() {
        View view = getChildAt(mCurrentPos);
        if(view instanceof ProgressBar) {
            ((ProgressBar) view).setProgress(0, false);
            if(mCurrentThread == null) {
                mCurrentThread = getProgressThread((ProgressBar) view);
            }
            mCurrentThread.start();
        }
    }

    void resumeTimer() {
        View view = getChildAt(mCurrentPos);
        if(view instanceof ProgressBar) {
            mCurrentThread = getProgressThread((ProgressBar) view);
            mCurrentThread.start();
        }
    }

    void pauseTimer() {
        View view = getChildAt(mCurrentPos);
        if(view instanceof ProgressBar) {
            if(mCurrentThread != null) {
                mCurrentThread.interrupt();
            }
            mCurrentThread = getProgressThread((ProgressBar) view);
        }
    }

    void resetTimer() {
        View view = getChildAt(mCurrentPos);
        if(view instanceof ProgressBar) {
            if(mCurrentThread != null) {
                mCurrentThread.interrupt();
            }
            ((ProgressBar) view).setProgress(0, false);
            mCurrentThread = getProgressThread((ProgressBar) view);
        }
    }

    void initTimer(Story[] stories) {
        removeAllViews();
        for(int index = 0; index < stories.length; index++) {
            ProgressBar progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setMax((int) (stories[index].getDuration() / 100));
            progressBar.setProgress(0, false);
            progressBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
            if(index > 0) {
                params.leftMargin = 8;
            }
            progressBar.setLayoutParams(params);
            addView(progressBar);
        }
    }

    void setTimerPosition(int position) {
        mCurrentPos = position;
        for(int index = 0; index < position; index++) {
            View view = getChildAt(index);
            if(view instanceof ProgressBar) {
                ((ProgressBar) view).setProgress(((ProgressBar) view).getMax(), false);
            }
        }
    }

    void prev() {
        final int oldPos = mCurrentPos;

        View view = getChildAt(oldPos);
        if(view instanceof ProgressBar) {
            if(mCurrentThread != null) {
                mCurrentThread.interrupt();
            }
            ((ProgressBar) view).setProgress(0, true);
        }

        mCurrentPos--;
        if(oldPos == 0) {
            mCurrentPos = 0;
            return;
        }

        view = getChildAt(mCurrentPos);
        if(view instanceof ProgressBar) {
            ((ProgressBar) view).setProgress(0, false);
            mCurrentThread = getProgressThread((ProgressBar) view);
            mCurrentThread.start();
        }
    }

    void next() {
        final int oldPos = mCurrentPos;

        View view = getChildAt(oldPos);
        if(view instanceof ProgressBar) {
            if(mCurrentThread != null) {
                mCurrentThread.interrupt();
            }
            ((ProgressBar) view).setProgress(((ProgressBar) view).getMax(), true);
        }

        mCurrentPos++;
        mCurrentPos %= getChildCount();

        if(oldPos < mCurrentPos) {
            view = getChildAt(mCurrentPos);

            if(view instanceof ProgressBar) {
                ((ProgressBar) view).setProgress(0, true);
                mCurrentThread = getProgressThread((ProgressBar) view);
                mCurrentThread.start();
            }
        } else {
            mCurrentPos = oldPos;
        }
    }

    void onPrevUserStory() {
        View view = getChildAt(0);
        if(view instanceof ProgressBar) {
            if(mCurrentThread != null) {
                mCurrentThread.interrupt();
            }
            ((ProgressBar) view).setProgress(0, true);
            mCurrentThread = getProgressThread((ProgressBar) view);
        }
    }

    void onNextUserStory() {
        View view = getChildAt(getChildCount() - 1);
        if(view instanceof ProgressBar) {
            if(mCurrentThread != null) {
                mCurrentThread.interrupt();
            }
            ((ProgressBar) view).setProgress(0, false);
            mCurrentThread = getProgressThread((ProgressBar) view);
        }
    }

    private Thread getProgressThread(final ProgressBar progressBar) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(progressBar.getProgress() < progressBar.getMax()) {
                        Thread.sleep(100);

                        mHandler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress(progressBar.getProgress() + 1, true);
                            }
                         });
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(mListener != null) {
                                mListener.onNextStory();
                            }
                        }
                    });
                } catch (Exception ignore) {
                }
            }
        });
    }

    interface TimerListener {
        void onNextStory();
    }
}
