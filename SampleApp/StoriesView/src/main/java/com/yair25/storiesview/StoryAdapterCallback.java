package com.yair25.storiesview;

class StoryAdapterCallback {

    private static StoryAdapter.StoryAdapterListener mInstance = null;

    static void createInstance(StoryAdapter.StoryAdapterListener listener) {
        mInstance = listener;
    }

    static StoryAdapter.StoryAdapterListener getInstance() {
        return mInstance;
    }

    //
}
