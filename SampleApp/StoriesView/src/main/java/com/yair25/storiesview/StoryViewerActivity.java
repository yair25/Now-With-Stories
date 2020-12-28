package com.yair25.storiesview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

public class StoryViewerActivity extends AppCompatActivity {

    private int mUserStoryPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_viewer);

        Intent intent = getIntent();
        mUserStoryPos = intent.getIntExtra(StoryAdapter.EXTRA_USER_STORY_POSITION, 0);

        initUserStoryFragments();
    }

    @Override
    public void finish() {
        super.finish();
        StoryAdapter.StoryAdapterListener listener = StoryAdapterCallback.getInstance();
        if(listener != null) {
            listener.onUpdateData();
        }
    }

    private void initUserStoryFragments() {
        for(int index = 0; index < UserStories.getInstance().getSize(); index++) {
            User userStory = UserStories.getInstance().getStory(index);

            final UserStoryFragment fragment = new UserStoryFragment(userStory, getListener());
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.storyFrameLayout, fragment);
            if(index == mUserStoryPos) {
                transaction.runOnCommit(new Runnable() {
                    @Override
                    public void run() {
                        fragment.onShow();
                    }
                });
                transaction.commit();
                return;
            }
            transaction.commit();
        }
    }

    private void setFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        final Fragment fragment = fragments.get(mUserStoryPos);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.show(fragment);
        transaction.runOnCommit(new Runnable() {
            @Override
            public void run() {
                if(fragment instanceof UserStoryFragment) {
                    ((UserStoryFragment)fragment).onShow();
                }
            }
        });
        transaction.commit();
    }

    private void setPrevFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        final Fragment removeFragment = fragments.get(fragments.size() - 1);
        final Fragment fragment = fragments.get(mUserStoryPos);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        transaction.remove(removeFragment);
        transaction.runOnCommit(new Runnable() {
            @Override
            public void run() {
                if(fragment instanceof UserStoryFragment) {
                    ((UserStoryFragment)fragment).onShow();
                }
            }
        });
        transaction.commit();
    }

    private void setNextFragment() {
        User userStory = UserStories.getInstance().getStory(mUserStoryPos);

        final UserStoryFragment nextFragment = new UserStoryFragment(userStory, getListener());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        transaction.add(R.id.storyFrameLayout, nextFragment);
        transaction.runOnCommit(new Runnable() {
            @Override
            public void run() {
                nextFragment.onShow();
            }
        });
        transaction.commit();
    }

    private UserStoryFragment.Listener getListener() {
        return new UserStoryFragment.Listener() {
            @Override
            public void onPrevUserStory() {
                mUserStoryPos--;

                if(mUserStoryPos < 0) {
                    mUserStoryPos = 0;
                    setFragment();
                    return;
                }

                setPrevFragment();
            }

            @Override
            public void onNextUserStory() {
                mUserStoryPos++;
                mUserStoryPos %= UserStories.size();

                if(mUserStoryPos == 0) {
                    finish();
                    return;
                }

                setNextFragment();
            }

            @Override
            public void onCloseUserStories() {
                finish();
            }
        };
    }
}