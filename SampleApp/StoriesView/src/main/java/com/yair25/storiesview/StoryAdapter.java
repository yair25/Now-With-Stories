package com.yair25.storiesview;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class StoryAdapter extends RecyclerView.Adapter<UserStoryView> {

    static final String EXTRA_USER_STORY_POSITION = "USER_STORY_POSITION";

    StoryAdapter() {
        List<User> userStories = new ArrayList<>();
        Collections.sort(userStories);
        UserStories.createInstance(userStories);
        StoryAdapterCallback.createInstance(getListener());
    }

    StoryAdapter(List<User> stories) {
        Collections.sort(stories);
        UserStories.createInstance(stories);
        StoryAdapterCallback.createInstance(getListener());
    }

    void setStories(List<User> stories) {
        Collections.sort(stories);
        UserStories.createInstance(stories);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserStoryView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_story_item_view, null, false);

        return new UserStoryView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserStoryView userStoryView, final int position) {
        final User userStory = UserStories.getUserStory(position);

        if(userStory != null) {
            userStoryView.setProfilePic(userStory.getProfilePic());
            userStoryView.setUsername(userStory.getUsername());
            userStoryView.setState(userStory.getState());
            userStoryView.setGesturesListener(getGesturesListener(userStoryView, position));
        }
    }

    @Override
    public int getItemCount() {
        return UserStories.size();
    }

    private StoryAdapterListener getListener() {
        return new StoryAdapterListener() {
            @Override
            public void onUpdateData() {
                UserStories.updateUserStoryStates();
                List<User> oldUserStories = new ArrayList<>(UserStories.getStories());
                Collections.sort(UserStories.getStories());
                notifyItemsMoved(oldUserStories, UserStories.getStories());

                for(int index = 0; index < UserStories.size(); index++) {
                    StoryAdapter.this.notifyItemChanged(index);
                }
            }

            private void notifyItemsMoved(List<User> before, List<User> after) {
                int newPos;

                for(int oldPos = 0; oldPos < before.size(); oldPos++) {
                    User item = before.get(oldPos);
                    newPos = after.indexOf(item);

                    if(-1 < newPos && oldPos != newPos) {
                        StoryAdapter.this.notifyItemMoved(oldPos, newPos);
                    }
                }
            }
        };
    }

    private UserStoryView.GesturesListener getGesturesListener(final UserStoryView userStoryView, final int position) {
        return new UserStoryView.GesturesListener() {
            @Override
            public void onSingleTapUp() {
                Intent intent = new Intent(userStoryView.itemView.getContext(), StoryViewerActivity.class);
                intent.putExtra(EXTRA_USER_STORY_POSITION, position);

                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation((Activity) userStoryView.itemView.getContext(),
                                new Pair<View, String>(userStoryView.getProfilePicImageView(), "sharedProfilePicTransition"),
                                new Pair<View, String>(userStoryView.getUsernameTextView(), "sharedUserNameTransition"));

                userStoryView.itemView.getContext().startActivity(intent, options.toBundle());
            }

            @Override
            public void onLongPress() {
                try {
                    User user = UserStories.getUserStory(position);
                    if(StoriesView.mListener != null) {
                        StoriesView.mListener.onUserStoryOptions(user);
                    }
                } catch (Exception ignore) {}
            }
        };
    }

    interface StoryAdapterListener {
        void onUpdateData();
    }
}
