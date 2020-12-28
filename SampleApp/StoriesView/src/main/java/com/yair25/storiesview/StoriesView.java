package com.yair25.storiesview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.List;

public class StoriesView extends LinearLayout {

    static Listener mListener = null;

    private StoriesRecyclerView mRecyclerView;

    public StoriesView(Context context) {
        super(context);
        init(context);
    }

    public StoriesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StoriesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mRecyclerView = new StoriesRecyclerView(context);
        mRecyclerView.setOrientation(getOrientation());
        addView(mRecyclerView);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if(mListener != null) {
            mListener = null;
        }
    }

    public void setStories(List<User> stories) {
        if(mRecyclerView != null) {
            mRecyclerView.setStories(stories);
        }
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public interface Listener {
        void onStoryOptions(User user, Story ID);
        void onShowStoryOptions();
        void onViewUserProfile(User mUser);
        void onEditCloseFriends();
        void onSendMessage(String userID, String storyID, String message);
    }
}
