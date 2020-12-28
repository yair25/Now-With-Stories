package com.yair25.storiesview;

import android.os.Parcel;
import android.os.Parcelable;

public class Story implements Parcelable {

    private final String mID;
    private final String mUrl;
    private final long mDuration;
    private final long mTimeStamp;
    private StoryState mState;
    private final StoryType mType;

    public Story(String id, String url, long duration, long timeStamp,
                 StoryState state, StoryType type) {
        mID = id;
        mUrl = url;
        mDuration = duration;
        mTimeStamp = timeStamp;
        mState = state;
        mType = type;
    }

    protected Story(Parcel in) {
        mID = in.readString();
        mUrl = in.readString();
        mDuration = in.readLong();
        mTimeStamp = in.readLong();
        mState = StoryState.values()[in.readInt()];
        mType = StoryType.values()[in.readInt()];
    }

    String getID () {
        return mID;
    }

    String getUrl() {
        return mUrl;
    }

    long getDuration() {
        return mDuration;
    }

    long getTimeStamp() {
        return mTimeStamp;
    }

    void setViewed() {
        switch (mState) {
            case NEW:
                mState = StoryState.VIEWED;
                break;
            case NEW_CLOSE_FRIENDS:
                mState = StoryState.VIEWED_CLOSE_FRIENDS;
                break;
        }
    }

    boolean isFresh() {
        return mState == StoryState.NEW || mState == StoryState.NEW_CLOSE_FRIENDS;
    }

    boolean isCloseFriends() {
        return mState == StoryState.NEW_CLOSE_FRIENDS || mState == StoryState.VIEWED_CLOSE_FRIENDS;
    }

    StoryType getMediaType() {
        return mType;
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel in) {
            return new Story(in);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int index) {
        parcel.writeString(mID);
        parcel.writeString(mUrl);
        parcel.writeLong(mDuration);
        parcel.writeLong(mTimeStamp);
        parcel.writeInt(mState.ordinal());
        parcel.writeInt(mType.ordinal());
    }

    public enum StoryState {
        NEW, NEW_CLOSE_FRIENDS, VIEWED, VIEWED_CLOSE_FRIENDS, NONE
    }

    public enum StoryType {
        PIC, VIDEO, POST, OTHER
    }
}
