package com.example.vkfriends;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;

import services.VKLogicService;

public class MutualFriendsActivity extends AppCompatActivity {
    private  ListView mutFiriendsListView;
    private TextView noMutualTextView;
    private JSONArray arr=null;
    private VKList mutualFriendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutual_friends);

        int source_uid = getIntent().getIntExtra("userID_1", 0);
        int target_uid = getIntent().getIntExtra("userID_2", 0);

        mutFiriendsListView = findViewById(R.id.list);

        noMutualTextView = findViewById(R.id.noMutualText);

        noMutualTextView.setVisibility(View.GONE);
        VKLogicService service = new VKLogicService();
        mutualFriendsList = service.getMutualFriendsList(source_uid,target_uid);
        if(mutualFriendsList.isEmpty())
            noMutualTextView.setVisibility(View.VISIBLE);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MutualFriendsActivity.this,
                                                                            android.R.layout.simple_expandable_list_item_1,
                                                                            mutualFriendsList);
        mutFiriendsListView.setAdapter(arrayAdapter);
        mutFiriendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    int redirectID = mutualFriendsList.get(position).fields.getInt("id");
                    Intent intent = new Intent(MutualFriendsActivity.this,ShortInfoActivity.class);
                    intent.putExtra("ID",redirectID);
                    startActivity(intent);
                }catch (JSONException a) {}
            }
        });
    }
}