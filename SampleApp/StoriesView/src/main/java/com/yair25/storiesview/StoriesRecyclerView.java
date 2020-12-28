package com.yair25.storiesview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

class StoriesRecyclerView extends RecyclerView {

    private StoryAdapter mAdapter;

    public StoriesRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public StoriesRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StoriesRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(context.getColor(R.color.transparent));
        mAdapter = new StoryAdapter();
        setAdapter(mAdapter);
    }

    void setOrientation(int orientation) {
        if(orientation == LinearLayout.HORIZONTAL) {
            setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }
    }

    public void setStories(List<User> stories) {
        // TODO: update
        if(mAdapter != null) {
            if(stories != null) {
                mAdapter.setStories(stories);
            }
        }
    }
}
