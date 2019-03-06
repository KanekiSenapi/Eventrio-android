package pl.aogiri.eventrio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    private LinearLayout mainContainer;
    private ProgressBar loading;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private boolean isLoggedIn;
    private String userFBID;
    private String userName;
    private String picture;
    private Map<String, String> user;
    Context CONTEXT;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CONTEXT =  getApplicationContext();
        startMap();

        service = ServiceGenerator.createService(UserInterface.class, "admin", "password");
        sharedPref = CONTEXT.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        singup = findViewById(R.id.signup);
        connectfb = findViewById(R.id.connectfb);
        mainContainer = findViewById(R.id.mainContainer);
        loading = findViewById(R.id.loading);


        accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn){
            reLoggin();
        }else{
            loading.setVisibility(View.GONE);
            mainContainer.setVisibility(View.VISIBLE);
        }



        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CONTEXT, MapsActivity.class);
                startActivity(i);
                finish();
            }
        });


        connectfb.setReadPermissions(Arrays.asList("email", "public_profile"));


        // Callback registration
        callbackManager = CallbackManager.Factory.create();

        connectfb.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        accessToken = loginResult.getAccessToken();
                        saveFBID();
                        //TODO

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(CONTEXT,"Sync error... try again", Toast.LENGTH_LONG).show();

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void reLoggin(){
        saveFBID();
    }

    private void accountCreator(){
        Log.e(TAG, user.toString());
        Call<String> call = service.createAccount(user);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body()==null){
                    Toast.makeText(CONTEXT, "Error with response",Toast.LENGTH_LONG).show();
                }else{
                    Log.e(TAG, "userid : " + response.body());
                    editor.putString(getString(R.string.userData_userid), response.body());
                    editor.commit();
                    startMap();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "On failure accountcreator : " + t.getMessage());
            }
        });

    }
    void saveFBID(){
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                              JSONObject object,
                            GraphResponse response) {
                        Log.e(TAG, response.getRawResponse());
                        try {
                            user = toMap(response.getJSONObject());
                            userFBID = user.get("id") ;
                            userName = user.get("name");
                            picture = user.get("picture");

                            editor.putString(getString(R.string.userData_fbid), userFBID);
                            editor.putString(getString(R.string.userData_username), userName);
                            editor.putString(getString(R.string.userData_picture), picture);
                            getUserId(userFBID);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CONTEXT,"Save fb profile error", Toast.LENGTH_SHORT);
                        }
                        editor.commit();
                        accountCreator();
//                        startMap();
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

    private Map<String,String> toMap(JSONObject body) throws JSONException {
        Map<String, String> newMap = new HashMap<>();
        newMap.put("id", body.getString("id"));
        newMap.put("name", body.getString("name"));
        newMap.put("email", body.getString("email"));
        newMap.put("picture", body.getJSONObject("picture").getJSONObject("data").getString("url"));
        return newMap;

    }

    private void getUserId(String userFBID){
        Toast.makeText(CONTEXT, userFBID, Toast.LENGTH_LONG);
        Call<User> userCall = service.getUserByFbid(userFBID);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e(TAG, response.raw().toString());
                if(response.body() != null) {
                    editor.putString(getString(R.string.userData_userid), response.body().getId());
                    editor.commit();
                    Toast.makeText(CONTEXT, response.body().getId(), Toast.LENGTH_SHORT);
                }else{
                    Toast.makeText(CONTEXT, TAG + " error response", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(CONTEXT, t.getMessage(), Toast.LENGTH_LONG);
            }
        });

    }

}
