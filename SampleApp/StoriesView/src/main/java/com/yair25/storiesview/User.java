package com.yair25.storiesview;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class User implements Comparable<User>, Parcelable {

    private State mState = State.VIEWED;
    private final String mID;
    private Priority mPriority;
    private boolean mIsMuted;
    private final String mUsername;
    private final String mProfilePic;

    private final Story[] mStories;

    public User(String id, Priority priority, boolean isMuted, String username, String profilePic, Story[] stories) {
        mID = id;
        mPriority = priority;
        mIsMuted = isMuted;
        mUsername = username;
        mProfilePic = profilePic;
        mStories = stories;

        if(mPriority == Priority.MAIN_USER) {
            mIsMuted = false;
        }

        updateState();
    }

    protected User(Parcel in) {
        mID = in.readString();
        mPriority = Priority.values()[in.readInt()];
        mIsMuted = in.readInt() != 0;
        mUsername = in.readString();
        mProfilePic = in.readString();
        mStories = in.createTypedArray(Story.CREATOR);

        if(mPriority == Priority.MAIN_USER) {
            mIsMuted = false;
        }

        updateState();
    }

    void updateState() {
        mState = State.VIEWED;

        for(Story story: mStories) {
            if(story.isCloseFriends() && story.isFresh()) {
                mState = State.CLOSE_FRIENDS;
                return;
            } else if(story.isFresh()) {
                mState = State.NEW;
            }
        }
    }

    public String getId() {
        return mID;
    }

    public Priority getPriority () {
        return mPriority;
    }

    public boolean isMuted() {
        return mIsMuted;
    }

    String getProfilePic() {
        return mProfilePic;
    }

    String getUsername() {
        return mUsername;
    }

    State getState() {
        updateState();
        return mState;
    }

    Story[] getStories() {
        return mStories;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    public void setMuted(boolean isMuted) {
        if(mPriority != Priority.MAIN_USER) {
            mIsMuted = isMuted;
        }
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int index) {
        parcel.writeString(mID);
        parcel.writeInt(mPriority.ordinal());
        parcel.writeString(mUsername);
        parcel.writeString(mProfilePic);
        parcel.writeTypedArray(mStories, 0);
    }

    @Override
    public int compareTo(User other) {
        if(other != null) {
            if(this.mIsMuted || other.mIsMuted) {
                return this.mIsMuted && !other.mIsMuted ? -1: 1;
            }

            if(this.mPriority == Priority.MAIN_USER || other.mPriority == Priority.MAIN_USER) {
                return this.mPriority == Priority.MAIN_USER ? -1 : 1;
            }

            if(this.mState == other.mState) {
                if(this.mPriority != other.mPriority) {
                    return this.mPriority.ordinal() < other.mPriority.ordinal() ? -1 : 1;
                }
                return 0;
            }

            if(this.mState == State.VIEWED || other.mState == State.VIEWED) {
                return this.mState != State.VIEWED ? -1 : 1;
            }

            return this.mPriority.ordinal() < other.mPriority.ordinal() ? -1 : 1;
        }

        return -1;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof User) {
            User other = (User) obj;
            return this.mID.equals(other.mID);
        }
        return false;
    }

    public enum Priority {
        MAIN_USER, BEST_FRIEND, CLOSE_FRIEND, FRIEND, KNOWN, UNKNOWN
    }

    enum State {
        NEW, CLOSE_FRIENDS, VIEWED
    }
}
