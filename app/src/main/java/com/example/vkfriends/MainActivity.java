package com.example.vkfriends;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.util.VKUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import checkers.InternetConnectionChecker;
import checkers.Response;
import constants.ValidationConstants;
import validators.UserNameValidator;
import services.VKLogicService;

import constants.ServiceConstants;
import constants.VKResponceErrorConstants;
import constants.UserErrorConstants;


public class MainActivity extends AppCompatActivity implements TextWatcher{
    private String[] scopesAppRequired = new String[]{VKScope.FRIENDS};

    private List<EditText> userScrName;
    private List<TextInputLayout> textInputLayouts;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //VKSdk.login(this,scopesAppRequired);
        Toast.makeText(MainActivity.this,"Добро пожаловать", Toast.LENGTH_LONG).show();

        textInputLayouts = new ArrayList<>();
        textInputLayouts.add((TextInputLayout) findViewById(R.id.userInputLayout_1));
        textInputLayouts.add((TextInputLayout) findViewById(R.id.userInputLayout_2));

        userScrName = new ArrayList<EditText>();
        userScrName.add((EditText) findViewById(R.id.InputScreenName_1));
        userScrName.add((EditText) findViewById(R.id.InputScreenName_2));

        userScrName.get(0).addTextChangedListener(this);
        userScrName.get(1).addTextChangedListener(this);

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Pair<String,TextInputLayout>> temp = new ArrayList<>();
                temp.add(new Pair<>(userScrName.get(0).getText().toString(),textInputLayouts.get(0)));
                temp.add(new Pair<>(userScrName.get(1).getText().toString(),textInputLayouts.get(1)));
                boolean isValidated = validationProcess();
                if(!isValidated || !button.isClickable())
                    return;
                button.setClickable(false);
                new InternetConnectionChecker(new Response() {
                    @Override
                    public void onResponseReceived(boolean internet) {
                        if(!internet) {
                            Toast.makeText(MainActivity.this, UserErrorConstants.NO_INTERNET, Toast.LENGTH_SHORT).show();
                            button.setClickable(true);
                            return;
                        }
                        VKLogicService ids = new VKLogicService(userScrName.get(0).getText().toString(), userScrName.get(1).getText().toString());
                        List<Integer> userIDs = ids.getIDList();
                        if (ids.isAllIDsCorrect(userIDs)) {
                            Intent intent = new Intent(MainActivity.this, MutualFriendsActivity.class);
                            intent.putExtra("userID_1", userIDs.get(0));
                            intent.putExtra("userID_2", userIDs.get(1));
                            startActivity(intent);
                            return;
                        }
                        for(int i=0; i<userIDs.size(); ++i)
                            switch (userIDs.get(i)){
                                case VKResponceErrorConstants.USER_NOT_EXIST:
                                    textInputLayouts.get(i).setError(UserErrorConstants.USER_NOT_EXST);
                                    break;
                                case VKResponceErrorConstants.USER_DELETED_OR_BLOCKED:
                                    textInputLayouts.get(i).setError(UserErrorConstants.USER_DELETED_OR_BLOCKED);
                                    break;
                                default:
                                    textInputLayouts.get(i).setErrorEnabled(false);
                            }
                            button.setClickable(true);
                    }
                });
            }
        });
    }

   @Override
    protected void onPostResume() {
        super.onPostResume();
        button.setClickable(true);
    }

    @Override
    public void afterTextChanged(Editable s) {
        validationProcess();
    }

    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
    public boolean validationProcess() {
        boolean isCorrect =true;
        List<Pair<UserNameValidator, TextInputLayout>> temp = new ArrayList<>();
        temp.add(new Pair<>(new UserNameValidator(userScrName.get(0).getText().toString()), textInputLayouts.get(0)));
        temp.add(new Pair<>(new UserNameValidator(userScrName.get(1).getText().toString()), textInputLayouts.get(1)));
        for (Pair<UserNameValidator, TextInputLayout> user : temp)
            switch (user.first.isValid()) {
                case ValidationConstants.EMPTY_STRING:
                    isCorrect =false;
                    user.second.setError("Пустая строка недопустима.");
                    break;
                case ValidationConstants.SPACES:
                    isCorrect=false;
                    user.second.setError("Пробелы недопустимы");
                    break;
                case ValidationConstants.SUCCESS:
                    user.second.setErrorEnabled(false);
                default:
            }
            return isCorrect;
    }
}