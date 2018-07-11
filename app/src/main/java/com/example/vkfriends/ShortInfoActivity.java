package com.example.vkfriends;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

import checkers.InternetConnectionChecker;
import checkers.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import constants.ServiceConstants;
import services.VKUserInfoService;

public class ShortInfoActivity extends AppCompatActivity {
    ImageView profilePhoto;
    TextView fullProfileLink;
    TextView userName;
    TextView followersCount;
    TextView friendsCount;
    VKUserInfoService userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_info);
        profilePhoto = findViewById(R.id.ProfilePhoto);
        fullProfileLink = findViewById(R.id.FullProfileLink);
        userName = findViewById(R.id.UserName);
        followersCount = findViewById(R.id.FollowersCount);
        friendsCount = findViewById(R.id.FriendsCount);

        new InternetConnectionChecker(new Response() {
            @Override
            public void onResponseReceived(boolean internet) {
                if(!internet)
                    return;
                int id=getIntent().getIntExtra("ID",0);
                userInfo = new VKUserInfoService(id);
                userInfo.updateFields();
                Bitmap bitmap = userInfo.getProfilePhotoBMP();
                userName.setText(userInfo.getFullName());
                profilePhoto.setImageBitmap(Bitmap.createScaledBitmap(bitmap,400,400,true));
                if(userInfo.isDeactivated())
                    return;
                followersCount.setText(userInfo.getFollowersCount()+" "+ "подписчиков");
                friendsCount.setText(userInfo.getFriendsCount() +" " + "друзей");
            }
        });
    }

    public void fullProfile(View v){
            Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(ServiceConstants.VK_PAGE_URL+userInfo.getUserID()));
            startActivity(browse);
    }
}
