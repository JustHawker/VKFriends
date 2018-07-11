package services;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import constants.VKResponceErrorConstants;
import constants.ServiceConstants;

public class VKLogicService {
    private List<String> usersList;
    private int i=0; //служебная переменная, необходимая для функционирования getIDArray()
    private List<Integer> resultIDs;
    private JSONArray mutualArray;
    private VKList listMutual;
    public  VKLogicService(){
        usersList = new ArrayList<String>();
    }
    public VKLogicService(String... users){
        usersList = new ArrayList<String>();
        for(String user:users)
            usersList.add(user);
    }

    public List getUsers() {
        return usersList;
    }

    public void setUsers(List<String> usersList) {
        this.usersList = usersList;
    }
    public List<Integer> getIDList(){
        resultIDs = new ArrayList<Integer>();
        for(String user:usersList) {
            VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, user));
            request.executeSyncWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    try {
                        boolean deactivated = response.json.getJSONArray("response").getJSONObject(0).has("deactivated");
                        Integer temp = deactivated ? VKResponceErrorConstants.USER_DELETED_OR_BLOCKED :
                                response.json.getJSONArray("response").getJSONObject(0).getInt("id");
                        resultIDs.add(temp);
                    } catch (JSONException a) {
                        resultIDs.add(VKResponceErrorConstants.USER_NOT_EXIST);
                    }
                }

                @Override
                public void onError(VKError error) {
                    super.onError(error);
                    resultIDs.add(new Integer(VKResponceErrorConstants.USER_NOT_EXIST));
                }
            });
        }
        return resultIDs;
    }
    public boolean isAllIDsCorrect(List<Integer>idList){
        for (Integer id:idList)
            if(id == VKResponceErrorConstants.USER_DELETED_OR_BLOCKED ||
                    id == VKResponceErrorConstants.USER_NOT_EXIST)
                return false;
        return true;
    }
    public VKList getMutualFriendsList(int source_uid,int target_uid){
        mutualArray=null;
        VKRequest requestMutual = VKApi.friends().getMutual(VKParameters.from("source_uid", source_uid, "target_uid", target_uid));
        requestMutual.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONObject responseJSON = response.json;
                    mutualArray = responseJSON.getJSONArray("response");
                }catch (JSONException e){}
            }
            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });
        VKRequest requestFriends = VKApi.friends().get(VKParameters.from(VKApiConst.USER_ID, source_uid,
                                                                         VKApiConst.FIELDS, "first_name,last_name"));
        requestFriends.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList list = (VKList) response.parsedModel;
                listMutual = new VKList();
                try {
                    for (int j = 0; j < mutualArray.length(); ++j)
                        listMutual.add(list.getById(mutualArray.getInt(j)));
                }catch(JSONException a){}
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });
        return listMutual;
    }
}