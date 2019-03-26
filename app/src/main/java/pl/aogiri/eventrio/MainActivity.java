package pl.aogiri.eventrio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.Arrays;

import pl.aogiri.eventrio.user.User;
import pl.aogiri.eventrio.user.UserInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity.java";
    private UserInterface service;

    private Button singup;
    private LoginButton connectfb;
    private ProgressBar loading;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private boolean isLoggedAtFB;
    Context CONTEXT;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CONTEXT =  getApplicationContext();

        service = ServiceGenerator.createService(UserInterface.class, getString(R.string.api_login), getString(R.string.api_password));
        sharedPref = CONTEXT.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        singup = findViewById(R.id.signup);
        connectfb = findViewById(R.id.connectfb);
        loading = findViewById(R.id.loading);
    }

    @Override
    protected void onStart() {
        super.onStart();
        accessToken = AccessToken.getCurrentAccessToken();
        isLoggedAtFB = accessToken != null && !accessToken.isExpired();

        if(isLoggedAtFB){
            Log.e(TAG,"IsLoggedAtFb true");
            checkUserExist();
        }else{
            Log.e(TAG,"Not logged in");
            connectfb.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);

            connectfb.setReadPermissions(Arrays.asList("email", "public_profile"));
            callbackManager = CallbackManager.Factory.create();
            connectfb.registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            accessToken = loginResult.getAccessToken();
                            checkUserExist();
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(CONTEXT,"Canceled... try again", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Toast.makeText(CONTEXT,"Sync error... try again", Toast.LENGTH_LONG).show();

                        }
                    });
        }
    }


    private boolean checkUserExist(){
        Call<User> userCall = service.getUserByFbid(accessToken.getUserId());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                switch (response.code()) {
                    case 204:
                        getDateFromFB();
                        break;
                    case 200:
                        Log.e(TAG, "Want login with my account");
                        User user = response.body();
                        saveToMemory(user);
                        startMap();
                        break;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
        return false;
    }

    private void createAccount(Object userOBJ){
        Call<User> userCall = service.createUserByFb(userOBJ);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                saveToMemory(response.body());
                startMap();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, t.getMessage());

            }
        });

    }

    private void saveToMemory(User user){
        editor.putString(getString(R.string.userData_userid),user.getId());
        editor.putString(getString(R.string.userData_fbid), user.getFbid());
        editor.putString(getString(R.string.userData_username), user.getPseudonym());
        editor.putString(getString(R.string.userData_picture), user.getPicture());
        editor.commit();
    }

    void getDateFromFB(){
        final GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                              JSONObject object,
                            GraphResponse response) {
                        Log.e(TAG,response.getRawResponse());
                        JsonParser parser = new JsonParser();
                        JsonElement mJson =  parser.parse(response.getRawResponse());

                        createAccount(new Gson().fromJson(mJson, Object.class));

                    }

                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void startMap(){
        Intent i = new Intent(CONTEXT, MapsActivity.class);
        CONTEXT.startActivity(i);
        finish();
    }



}
