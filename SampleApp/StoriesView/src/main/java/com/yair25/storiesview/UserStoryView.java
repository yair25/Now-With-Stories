package com.yair25.storiesview;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

class UserStoryView extends RecyclerView.ViewHolder implements GestureDetector.OnGestureListener{

    private GestureDetectorCompat mDetector;
    private final RelativeLayout mFrame;
    private final ImageView mImageView;
    private final TextView mTextView;

    private GesturesListener mListener;

    UserStoryView(@NonNull View itemView) {
        super(itemView);

        mFrame = itemView.findViewById(R.id.storyFrame);
        mImageView = itemView.findViewById(R.id.storyImgView);
        mTextView = itemView.findViewById(R.id.storyTextView);

        itemView.setContentDescription(itemView.getContext().getString(R.string.story_item_desc));

        mDetector = new GestureDetectorCompat(itemView.getContext(), this);
        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        });
    }

    ImageView getProfilePicImageView() {
        return mImageView;
    }

    TextView getUsernameTextView() {
        return mTextView;
    }

    void setState(User.State state) {
        switch (state) {
            case NEW:
                mFrame.setBackgroundResource(R.drawable.ic_circle_gradient);
                break;
            case CLOSE_FRIENDS:
                mFrame.setBackgroundResource(R.drawable.ic_circle_green);
                break;
            case VIEWED:
                mFrame.setBackgroundResource(R.drawable.ic_circle_grey);
                break;
            default:
        }
    }

    void setProfilePic(final String profilePicUrl) {
        Picasso.get()
                .load(profilePicUrl)
                .resize(1000, 1000)
                .centerInside()
                .transform(new CircleTransform())
                .into(mImageView);
    }

    void setUsername(final String username) {
        mTextView.setText(username);

        itemView.setContentDescription(username + " " + itemView.getResources().getString(R.string.story_desc));
    }

    void setGesturesListener(GesturesListener listener) {
        mListener = listener;
    }

    interface GesturesListener {
        void onSingleTapUp();
        void onLongPress();
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        if(mListener != null) {
            mListener.onLongPress();
        }
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        if(mListener != null) {
            mListener.onSingleTapUp();
        }
        return true;
    }
}
