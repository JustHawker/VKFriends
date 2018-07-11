package services;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class VKUserInfoService {
    private int userID;
    private String fullName;
    private Bitmap profilePhotoBMP;
    private int friendsCount=0;
    private int followersCount=0;
    private boolean deactivated;
    public void setUserID() {
        this.userID = 0;
       // updateFields();
    }
    public void setUserID(int userID) {
        this.userID = userID;
       // updateFields();
    }

    public int getUserID() {
        return userID;
    }

    public String getFullName() {
        return fullName;
    }

    public Bitmap getProfilePhotoBMP() {
        return profilePhotoBMP;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public boolean isDeactivated() {
        return deactivated;
    }

    public VKUserInfoService(int userID){
        this.userID = userID;
        updateFields();
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateFields(){
        VKRequest moreInfoRequest = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS,userID,VKApiConst.FIELDS,"photo_max,counters"));
        moreInfoRequest.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONObject responseObject = response.json.getJSONArray("response").getJSONObject(0);
                    fullName = responseObject.getString("first_name") + " " + responseObject.getString("last_name");
                    profilePhotoBMP = getBitmapFromURL(responseObject.getString("photo_max"));
                    deactivated = responseObject.has("deactivated");
                    if(deactivated)
                        return;
                    friendsCount = responseObject.getJSONObject("counters").getInt("friends");
                    followersCount = responseObject.getJSONObject("counters").getInt("followers");
                } catch (JSONException e) { }
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });
    }
}
