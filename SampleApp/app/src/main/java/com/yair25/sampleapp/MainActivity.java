package com.yair25.sampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.yair25.storiesview.StoriesView;
import com.yair25.storiesview.Story;
import com.yair25.storiesview.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StoriesView storiesView = findViewById(R.id.storiesView);

        storiesView.setListener(new StoriesView.Listener() {
            @Override
            public void onStoryOptions(User user, Story ID) {
                // TODO: Implement on the SDK
            }

            @Override
            public void onShowStoryOptions() {
                Toast.makeText(MainActivity.this, "Not implemented", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onViewUserProfile(User mUser) {
                Toast.makeText(MainActivity.this, "Not implemented", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onEditCloseFriends() {
                // TODO: Implement on the SDK
            }

            @Override
            public void onSendMessage(String userID, String storyID, String message) {
                Toast.makeText(MainActivity.this, "Message Sent", Toast.LENGTH_LONG).show();
            }
        });

        Story[] stories0 = new Story[] {
                new Story("1587304878428","https://images.unsplash.com/photo-1587304878428-1b533030e0e7?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=934&q=80",
                        5000, System.currentTimeMillis() / 1000, Story.StoryState.NEW, Story.StoryType.PIC),

                new Story("1436262513933", "https://images.unsplash.com/photo-1436262513933-a0b06755c784?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1951&q=80",
                        5000, System.currentTimeMillis() / 1000, Story.StoryState.NEW, Story.StoryType.PIC),

                new Story("rgHhWRnQAeAbCENCAF", "https://media.giphy.com/media/rgHhWRnQAeAbCENCAF/giphy.gif",
                        5000, System.currentTimeMillis() / 1000, Story.StoryState.NEW, Story.StoryType.OTHER)
        };

        Story[] stories1 = new Story[] {
                new Story("1466036692599","https://images.unsplash.com/photo-1466036692599-070d032f4711?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80",
                        5000, System.currentTimeMillis() / 1000, Story.StoryState.NEW, Story.StoryType.PIC),

                new Story("1458682625221", "https://images.unsplash.com/photo-1458682625221-3a45f8a844c7?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1867&q=80",
                        5000, System.currentTimeMillis() / 1000, Story.StoryState.NEW_CLOSE_FRIENDS, Story.StoryType.PIC)
        };

        Story[] stories2 = new Story[] {
                new Story("1446822775955", "https://images.unsplash.com/photo-1446822775955-c34f483b410b?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1950&q=80",
                        5000, System.currentTimeMillis() / 1000, Story.StoryState.NEW, Story.StoryType.PIC)
        };

        Story[] stories3 = new Story[] {
                new Story("1513435268174", "https://images.unsplash.com/photo-1513435268174-838c8948bdfc?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=998&q=80",
                        5000, System.currentTimeMillis() / 1000, Story.StoryState.VIEWED, Story.StoryType.PIC),

                new Story("1493846398037","https://images.unsplash.com/photo-1493846398037-b7451de18579?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=1950&q=80",
                        5000, System.currentTimeMillis() / 1000, Story.StoryState.NEW_CLOSE_FRIENDS, Story.StoryType.PIC)
        };

        Story[] stories4 = new Story[] {
                new Story("1534359525473", "https://images.unsplash.com/photo-1534359525473-c88feeae9d6c?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=976&q=80",
                        5000, System.currentTimeMillis() / 1000, Story.StoryState.VIEWED, Story.StoryType.PIC)
        };

        List<User> userStories = new ArrayList<>();
        userStories.add(new User("051008", User.Priority.MAIN_USER, false, "ynietosa", "https://scontent-dfw5-2.xx.fbcdn.net/v/t1.0-1/s480x480/125246492_3750855228268010_1184526676689401769_o.jpg?_nc_cat=109&ccb=2&_nc_sid=7206a8&_nc_ohc=2X9cpoCdw_sAX97zeNv&_nc_ht=scontent-dfw5-2.xx&tp=7&oh=a84938fbe20fe3b8e16ce43414f51ab4&oe=5FEB6BFF",
                stories0));
        userStories.add(new User("2322", User.Priority.BEST_FRIEND, false, "tfrank", "https://images.unsplash.com/photo-1552374196-c4e7ffc6e126?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=934&q=80" ,
                stories1));
        userStories.add(new User("94963", User.Priority.BEST_FRIEND, false, "rguz123", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=934&q=80" ,
                stories2));
        userStories.add(new User("123453342", User.Priority.FRIEND, false, "ter343", "https://images.unsplash.com/photo-1508002366005-75a695ee2d17?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=917&q=80" ,
                stories3));
        userStories.add(new User("098", User.Priority.BEST_FRIEND, false, "srg324", "https://images.unsplash.com/photo-1495490140452-5a226aef25d4?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1950&q=80" ,
                stories4));

        storiesView.setStories(userStories);
    }
}
