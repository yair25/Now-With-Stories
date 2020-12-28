package com.yair25.storiesview;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class UserStoryFragment extends Fragment {

    private View mRootView;
    private User mUser;
    private Listener mListener;
    private UserStoriesManager mManager;

    private EditText mMessageET;

    public UserStoryFragment() {
        // Required empty public constructor
    }

    UserStoryFragment(User user, Listener listener) {
        mUser = user;
        mListener = listener;
    }

    public static UserStoryFragment newInstance() {
        return new UserStoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_user_story, container, false);
        initUI();
        return mRootView;
    }


    private void initUI() {
        UserStoryTimerView mStoryTimerView = mRootView.findViewById(R.id.storyTimerView);
        StoryView mStoryView = mRootView.findViewById(R.id.storyView);
        mMessageET = mRootView.findViewById(R.id.messageET);

        setUIListeners();


        setUserUI(mUser);
        mManager = new UserStoriesManager(mUser.getStories(), mStoryView, mStoryTimerView, getListener());
    }

    private void setUIListeners() {
        View mUserView = mRootView.findViewById(R.id.storyUserView);

        mMessageET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND) {
                    final String message = mMessageET.getText().toString();
                    if(!TextUtils.isEmpty(message)) {
                        if(StoriesView.mListener != null) {
                            Story currentStory = mUser.getStories()[mManager.getPosition()];
                            StoriesView.mListener.onSendMessage(mUser.getId(), currentStory.getID(), message);
                        }
                        clearEditText();
                    }
                    return true;
                }

                return false;
            }
        });

        mMessageET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    mManager.pause();
                } else {
                    clearEditText();
                    // TODO: Remove
                    mManager.resume();
                }
            }
        });

        // TODO: Stop timer, going to another screen...

        mUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(StoriesView.mListener != null) {
                        StoriesView.mListener.onViewUserProfile(mUser);
                    }
                } catch (Exception ignore) { }
            }
        });
    }

    private void setUserUI(User userStory) {
        ImageView profileImageView = mRootView.findViewById(R.id.storyProfilePicIV);
        TextView usernameTextView = mRootView.findViewById(R.id.storyUsernameTV);

        Picasso.get()
                .load(userStory.getProfilePic())
                .resize(1000, 1000)
                .centerInside()
                .transform(new CircleTransform())
                .into(profileImageView);

        usernameTextView.setText(userStory.getUsername());
    }

    public void onShow() {
        mManager.onStartTimer();
    }

    private void clearEditText() {
        mMessageET.clearFocus();
        mMessageET.setText("");
        if(getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null) {
                imm.hideSoftInputFromWindow(mMessageET.getWindowToken(), 0);
            }
        }
    }

    private UserStoriesManager.StoryListener getListener() {
        return new UserStoriesManager.StoryListener() {

            @Override
            public void setStoryUI(Story story) {
                clearEditText();
                setTimestamp(story.getTimeStamp());
                updateCloseFriendsLabel(story.isCloseFriends());
            }

            private void setTimestamp(long timeStamp) {
                long deltaTime = (System.currentTimeMillis() / 1000) - timeStamp;
                TextView timeTextView = mRootView.findViewById(R.id.storyTimeV);

                if(deltaTime / (60 * 60) > 0) {
                    // Hours
                    timeTextView.setText(deltaTime / (60 * 60) + "h");
                } else if(deltaTime / 60 > 0) {
                    // Mins
                    timeTextView.setText((deltaTime / 60 ) + "m");
                } else {
                    // Sec
                    timeTextView.setText(deltaTime + "s");
                }
            }

            private void updateCloseFriendsLabel(boolean isCloseFriends) {
                if(isCloseFriends) {
                    mRootView.findViewById(R.id.storyCloseFriendsLabel).setVisibility(View.VISIBLE);
                } else {
                    mRootView.findViewById(R.id.storyCloseFriendsLabel).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPrevUserStory() {
                clearEditText();
                if(mUser != null) {
                    mUser.updateState();
                }

                if(mListener != null) {
                    mListener.onPrevUserStory();
                }
            }

            @Override
            public void onNextUserStory() {
                clearEditText();
                if(mUser != null) {
                    mUser.updateState();
                }

                if(mListener != null) {
                    mListener.onNextUserStory();
                }
            }

            @Override
            public void onUserStoryViewed() {
                onNextUserStory();
            }

            @Override
            public void onCloseUserStories() {
                clearEditText();
                if(mUser != null) {
                    mUser.updateState();
                }

                if(mListener != null) {
                    mListener.onCloseUserStories();
                }
            }

            @Override
            public void onClearFocus() {
                mMessageET.clearFocus();
            }
        };
    }

    interface Listener {
        void onPrevUserStory();
        void onNextUserStory();
        void onCloseUserStories();
    }
}