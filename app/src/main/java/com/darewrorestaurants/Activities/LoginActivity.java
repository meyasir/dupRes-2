package com.darewrorestaurants.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.darewrorestaurants.R;
import com.darewrorestaurants.Utilities.Constants;
import com.darewrorestaurants.Utilities.Helper;
import com.darewrorestaurants.Utilities.MySharedPreferences;
import com.darewrorestaurants.Utilities.WebService;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    WebService webService;
    LinearLayout linlaHeaderProgress,snackbar;
    EditText etUsername,etPassword;
    Button btnLogin;
    Runnable mRunnable;
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        linlaHeaderProgress = (LinearLayout) findViewById(R.id.progress);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        snackbar = (LinearLayout) findViewById(R.id.snackbar);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginClick(view);
            }
        });
        snackbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSnackbarClick(view);
            }
        });
        webService = new WebService(getApplicationContext());
        mHandler = new Handler();
        mRunnable = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                snackbar.setVisibility(View.GONE); //This will remove the View. and free s the space occupied by the View
            }
        };
    }

    public void onSnackbarClick(View view) {
        snackbar.setVisibility(View.GONE);
        mHandler.removeCallbacks(mRunnable);
    }

    public void onLoginClick(View view) {
        Helper helper = new Helper();
        if (helper.isEditTextEmpty(etUsername)||helper.isEditTextEmpty(etPassword)){
            Toast.makeText(this, "Username and password required", Toast.LENGTH_SHORT).show();
        }
        else if (webService.isNetworkConnected()){
            if(snackbar.getVisibility()==View.VISIBLE) {
                snackbar.setVisibility(View.GONE);
                mHandler.removeCallbacks(mRunnable);
            }
            setEnableFalse();
            restaurantLogin();
        }
        else {
            snackbar.setVisibility(View.VISIBLE);
            mHandler.postDelayed(mRunnable, 4 * 1000);
        }
    }

    private void restaurantLogin(){
        webService.restaurantLoginRequest(this.getResources().getString(R.string.url) + "restaurant_login",
                etUsername.getText().toString(), etPassword.getText().toString(), new WebService.VolleyResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");
                            if(success){
                                MySharedPreferences mySharedPreferences = new MySharedPreferences();
                                mySharedPreferences.setLogin(LoginActivity.this,
                                        true);
                                mySharedPreferences.setUserID(LoginActivity.this,
                                        jsonObject.getInt(Constants.USER_ID));
                                mySharedPreferences.setRestaurantID(LoginActivity.this,
                                        jsonObject.getInt(Constants.RESTAURANT_ID));
                                mySharedPreferences.setUserTitle(LoginActivity.this,
                                        jsonObject.getString(Constants.USER_TITLE));
                                mySharedPreferences.setUserEmail(LoginActivity.this,
                                        jsonObject.getString(Constants.USER_EMAIL));
                                mySharedPreferences.setUserMobileNumber(LoginActivity.this,
                                        jsonObject.getString(Constants.USER_MOBILE_NUMBER));
                                mySharedPreferences.setRestaurantName(LoginActivity.this,
                                        jsonObject.getString(Constants.RESTAURANT_NAME));
                                mySharedPreferences.setRestaurantLocation(LoginActivity.this,
                                        jsonObject.getString(Constants.RESTAURANT_LOCATION));
                                mySharedPreferences.setRestaurantDetails(LoginActivity.this,
                                        jsonObject.getString(Constants.RESTAURANT_DETAIL));
                                mySharedPreferences.setRestaurantContactNumber(LoginActivity.this,
                                        jsonObject.getString(Constants.RESTAURANT_CONTACT_NO));
                                mySharedPreferences.setRestaurantEndTime(LoginActivity.this,
                                        jsonObject.getString(Constants.RESTAURANT_CLOSE_TIME));
                                mySharedPreferences.setRestaurantStartTime(LoginActivity.this,
                                        jsonObject.getString(Constants.RESTAURANT_START_TIME));
                                mySharedPreferences.setRestaurantLogo(LoginActivity.this,
                                        jsonObject.getString(Constants.RESTAURANT_LOGO));
                                mySharedPreferences.setUserImage(LoginActivity.this,
                                        jsonObject.getString(Constants.USER_IMAGE));
                                updateToken();
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                Intent activity = new Intent(LoginActivity.this, AppBaseActivity.class);
                                activity.putExtra("Uniqid","From_Login");
                                activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(activity);
                            }else {
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                setEnableTrue();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            setEnableTrue();
                            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Helper helper = new Helper();
                        helper.volleyErrorMessage(LoginActivity.this,error);
                        setEnableTrue();
                    }
                });
    }

    private void setEnableTrue(){
        linlaHeaderProgress.setVisibility(View.GONE);
        btnLogin.setEnabled(true);
        etPassword.setEnabled(true);
        etUsername.setEnabled(true);
    }
    private void setEnableFalse(){
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
        etPassword.setEnabled(false);
        etUsername.setEnabled(false);
    }

    private void updateToken(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        MySharedPreferences mySharedPreferences = new MySharedPreferences();
        webService.customerUpdateTokenKey(getResources().getString(R.string.url) + "user_token_update", String.valueOf(mySharedPreferences.getUserID(this)),refreshedToken, new WebService.VolleyResponseListener() {

            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}
