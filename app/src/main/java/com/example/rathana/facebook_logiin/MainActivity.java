package com.example.rathana.facebook_logiin;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    LoginButton btnFbLogin;
    CallbackManager callbackManager;
    AccessToken accessToken= AccessToken.getCurrentAccessToken();
    Boolean isLogin = accessToken!=null && !accessToken.isExpired();
    Button btnFbCustomLogin;
    public  static  final String[] permissions={"email"};

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);

        btnFbCustomLogin=findViewById(R.id.btnCustomFbLogin);
        callbackManager=CallbackManager.Factory.create();
        facebookLogin();

    }

    private void facebookLogin() {
         btnFbLogin=findViewById(R.id.login_button);
         btnFbLogin.setReadPermissions(Arrays.asList(permissions));
         btnFbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
             @Override
             public void onSuccess(LoginResult loginResult) {

                 Log.e(TAG, "onSuccess: "+ loginResult.getAccessToken().getUserId());
                 GraphRequest request= GraphRequest.newMeRequest(
                         loginResult.getAccessToken(),
                         new GraphRequest.GraphJSONObjectCallback() {
                             @Override
                             public void onCompleted(JSONObject object, GraphResponse response) {
                                 //user info
                                 try {
                                     String firstName=object.optString("first_name");
                                     String lastName =object.optString("last_name");
                                     String id=object.optString("id");
                                     String email=object.optString("email");
                                     JSONObject picture =object.getJSONObject("picture").getJSONObject("data");
                                     String profile=picture.optString("url");

                                     String dob=object.optString("birthday");
                                     User user =new User(
                                       id,lastName,firstName,email,profile,dob
                                     );

                                     Log.e(TAG, "onCompleted: "+user.toString() );
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }

                             }
                         }
                 );

                 Bundle parameters= new Bundle();
                 parameters.putString("fields", "id,first_name,last_name,email,birthday,picture.type(normal)");
                 request.setParameters(parameters);
                 request.executeAsync();

             }

             @Override
             public void onCancel() {
                 Log.e(TAG, "onCancel: ");
             }

             @Override
             public void onError(FacebookException error) {
                 Log.e(TAG, "onError: "+ error.toString());
             }
         });
    }



    public void onFacebookLogin(View view){


        Log.e(TAG, "onFacebookLogin: " );
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(this,Arrays.asList(permissions));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "onSuccess: "+ loginResult.getAccessToken().getUserId());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);

        btnFbCustomLogin.setText("Logout");

    }
}
