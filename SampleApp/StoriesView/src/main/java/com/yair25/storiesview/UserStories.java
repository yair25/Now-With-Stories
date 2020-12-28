package com.yair25.storiesview;

import java.util.ArrayList;
import java.util.List;

class UserStories {

    private static UserStories mInstance;

    static UserStories getInstance() {
        return mInstance;
    }

    static void createInstance(List<User> stories) {
        if(stories != null) {
            mInstance = new UserStories(stories);
        }
    }

    static List<User> getStories() {
        if(mInstance != null) {
            return mInstance.mStories;
        }
        return new ArrayList<>();
    }

    static User getUserStory(int index) {
        if(mInstance != null) {
            return mInstance.getStory(index);
        }
        return null;
    }

    static void updateUserStoryStates() {
        if(mInstance != null) {
            for(int index = 0; index < size(); index++) {
                User userStory = getUserStory(index);
                if(userStory != null) {
                    userStory.updateState();
                }
            }
        }
    }

    static int size() {
        if(mInstance != null) {
            return mInstance.getSize();
        }
        return 0;
    }

    //

    private final List<User> mStories;

    private UserStories(List<User> stories) {
        mStories = stories;
    }

    final User getStory(int index) {
        if(mStories != null && -1 < index && index < mStories.size()) {
            return mStories.get(index);
        }
        return null;
    }

    final int getSize() {
        if(mStories != null) {
            return mStories.size();
        }
        return 0;
    }
}
