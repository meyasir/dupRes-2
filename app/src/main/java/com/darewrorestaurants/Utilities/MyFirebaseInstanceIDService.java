package com.darewrorestaurants.Utilities;

import android.util.Log;
import com.darewrorestaurants.R;

import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

/**
 * Created by Jaffar on 11/15/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    MySharedPreferences mySharedPreferences;

    int customerID;
    private static final String TAG = "MyFirebaseIIDService";
    private WebService webService;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]@Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        mySharedPreferences = new MySharedPreferences();
        mySharedPreferences.getPrefs(this);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        customerID = mySharedPreferences.getUserID(this);
        webService = new WebService(this);
        if(webService.isNetworkConnected()) {
            sendRegistrationToServer(customerID);
        }
    }
    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     */
    private void sendRegistrationToServer(int id) {
        // TODO: Implement this method to send token to your app server.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        webService.customerUpdateTokenKey(getResources().getString(R.string.url) + "user_token_update", String.valueOf(id),refreshedToken, new WebService.VolleyResponseListener() {

            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }
}
