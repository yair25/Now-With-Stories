package com.yair25.storiesview;

class UserStoriesManager {

    private final Story[] mStories;
    private final StoryView mStoryView;
    private final UserStoryTimerView mTimerView;
    private final StoryListener mListener;

    private int mPosition = 0;

    UserStoriesManager(Story[] stories,
                       StoryView storyView,
                       UserStoryTimerView timerView,
                       StoryListener listener) {
        mStoryView = storyView;
        mStories = stories;
        mTimerView = timerView;
        mListener = listener;

        mStoryView.setGesturesListener(getGestureListener());
        mTimerView.setTimerListener(getTimerListener());
        mTimerView.initTimer(stories);

        setFreshStory();
    }

    int getPosition() {
        return mPosition;
    }

    void onStartTimer() {
        mTimerView.startTimer();
        mStories[mPosition].setViewed();
    }

    void pause() {
        mStoryView.setGesturesListener(getPausedGestureListener());
        mTimerView.pauseTimer();
    }

    void resume() {
        mStoryView.setGesturesListener(getGestureListener());
        mTimerView.resumeTimer();
    }

    private void setFreshStory() {
        if(mStories != null && mStories.length > 0) {
            for(int index = 0; index < mStories.length; index++) {
                Story story = mStories[index];
                if(story.isFresh()) {
                    mPosition = index;
                    break;
                }
            }

            Story story = mStories[mPosition];
            mStoryView.setImageUrl(story.getUrl());
            mTimerView.setTimerPosition(mPosition);
            if(mListener != null) {
                mListener.setStoryUI(story);
            }
        }
    }

    private void viewPrevStory() {
        final int oldPos = mPosition;
        mPosition--;

        if(oldPos == 0) {
            mPosition = 0;
            mTimerView.onPrevUserStory();
            if(mListener != null) {
                mListener.onPrevUserStory();
            }
            return;
        }

        Story story = mStories[mPosition];
        mStoryView.setImageUrl(story.getUrl());
        mTimerView.prev();
        if(mListener != null) {
            mListener.setStoryUI(story);
        }
        story.setViewed();
    }

    private void viewNextStory() {
        final int oldPos = mPosition;
        mPosition++;

        mPosition %= mStories.length;

        if(mPosition <= oldPos) {
            mPosition = oldPos;
            mTimerView.onNextUserStory();
            if(mListener != null) {
                mListener.onUserStoryViewed();
            }
            return;
        }

        Story story = mStories[mPosition];
        mStoryView.setImageUrl(story.getUrl());
        mTimerView.next();
        if(mListener != null) {
            mListener.setStoryUI(story);
        }
        story.setViewed();
    }

    private StoryView.GesturesListener getGestureListener() {
        return new StoryView.GesturesListener() {

            private boolean isPaused = false;

            @Override
            public void onLeftClick() {
                viewPrevStory();
            }

            @Override
            public void onRightClick() {
                viewNextStory();
            }

            @Override
            public void onLeftFling() {
                if (mListener != null) {
                    mTimerView.resetTimer();
                    mListener.onPrevUserStory();
                }
            }

            @Override
            public void onRightFling() {
                if (mListener != null) {
                    mTimerView.resetTimer();
                    mListener.onNextUserStory();
                }
            }

            @Override
            public void onDownFling() {
                if (mListener != null) {
                    mListener.onCloseUserStories();
                }
            }

            @Override
            public void onLongPress() {
                isPaused = true;
                mTimerView.pauseTimer();
            }

            @Override
            public void onLongPressFinished() {
                if (isPaused) {
                    isPaused = false;
                    mTimerView.resumeTimer();
                }
            }

            @Override
            public boolean wasLongPress() {
                return isPaused;
            }
        };
    }

    private StoryView.GesturesListener getPausedGestureListener() {
        return new StoryView.GesturesListener() {
            @Override
            public void onLeftClick() {
                if(mListener != null) {
                    mListener.onClearFocus();
                }
                mStoryView.requestFocus();
            }

            @Override
            public void onRightClick() {
                if(mListener != null) {
                    mListener.onClearFocus();
                }
                mStoryView.requestFocus();
            }

            @Override
            public void onLeftFling() {

            }

            @Override
            public void onRightFling() {

            }

            @Override
            public void onDownFling() {
                if(mListener != null) {
                    mListener.onClearFocus();
                }
                mStoryView.requestFocus();
            }

            @Override
            public void onLongPress() {

            }

            @Override
            public void onLongPressFinished() {

            }

            @Override
            public boolean wasLongPress() {
                return false;
            }
        };
    }

    private UserStoryTimerView.TimerListener getTimerListener() {
        return new UserStoryTimerView.TimerListener() {
            @Override
            public void onNextStory() {
                viewNextStory();
            }
        };
    }

    interface StoryListener {
        void setStoryUI(Story story);
        void onPrevUserStory();
        void onNextUserStory();
        void onUserStoryViewed();
        void onCloseUserStories();
        void onClearFocus();
    }
}
