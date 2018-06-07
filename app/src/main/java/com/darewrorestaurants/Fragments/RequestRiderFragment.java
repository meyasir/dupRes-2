package com.darewrorestaurants.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.darewrorestaurants.Activities.AppBaseActivity;
import com.darewrorestaurants.R;
import com.darewrorestaurants.Utilities.Helper;
import com.darewrorestaurants.Utilities.MySharedPreferences;
import com.darewrorestaurants.Utilities.WebService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jaffar on 2018-02-14.
 */

public class RequestRiderFragment extends Fragment {

    LinearLayout progress;
    EditText etNumber,etName,etDetail,etLoc,etTime;
    Button time20,time40,time60, time120,btnRequest;
    Activity activity;
    WebService webService;
    public RequestRiderFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        webService = new WebService(activity);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_request_rider, container, false);
        init(rootView);
        setOnClickListener();
        return rootView;
    }

    private void init(View view){

        progress = (LinearLayout)view.findViewById(R.id.progress);
        etNumber = (EditText)view.findViewById(R.id.etNumber);
        etName = (EditText)view.findViewById(R.id.etName);
        etDetail = (EditText)view.findViewById(R.id.etDetail);
        etLoc = (EditText)view.findViewById(R.id.etLoc);
        etTime = (EditText)view.findViewById(R.id.etTime);
        setDefaultHintColor(etName);
        setDefaultHintColor(etNumber);
        setDefaultHintColor(etLoc);
        setDefaultHintColor(etTime);
        time20 = (Button)view.findViewById(R.id.time20);
        time40 = (Button)view.findViewById(R.id.time40);
        time60 = (Button)view.findViewById(R.id.time60);
        time120 = (Button)view.findViewById(R.id.time120);
        btnRequest = (Button)view.findViewById(R.id.btnRequest);
    }
    private int hintDefaultColor;
    public void setDefaultHintColor(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText.getCurrentHintTextColor()== Color.RED)
                    editText.setHintTextColor(hintDefaultColor);
            }
        });
    }
    private void setOnClickListener(){
        time20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time20.setBackgroundColor(activity.getResources().getColor(R.color.timeBTNPressed));
                time40.setBackgroundColor(activity.getResources().getColor(R.color.timeBTNDefault));
                time60.setBackgroundColor(activity.getResources().getColor(R.color.timeBTNDefault));
                time120.setBackgroundColor(activity.getResources().getColor(R.color.timeBTNDefault));
                etTime.setText("20");
            }
        });
        time40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time20.setBackgroundColor(activity.getResources().getColor(R.color.timeBTNDefault));
                time40.setBackgroundColor(activity.getResources().getColor(R.color.timeBTNPressed));
                time60.setBackgroundColor(activity.getResources().getColor(R.color.timeBTNDefault));
                time120.setBackgroundColor(activity.getResources().getColor(R.color.timeBTNDefault));
                etTime.setText("40");
            }
        });
        time60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time20.setBackgroundColor(activity.getResources().getColor(R.color.timeBTNDefault));
                time40.setBackgroundColor(activity.getResources().getColor(R.color.timeBTNDefault));
                time60.setBackgroundColor(activity.getResources().getColor(R.color.timeBTNPressed));
                time120.setBackgroundColor(activity.getResources().getColor(R.color.timeBTNDefault));
                etTime.setText("60");
            }
        });
        time120.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time20.setBackgroundColor(activity.getResources().getColor(R.color.timeBTNDefault));
                time40.setBackgroundColor(activity.getResources().getColor(R.color.timeBTNDefault));
                time60.setBackgroundColor(activity.getResources().getColor(R.color.timeBTNDefault));
                time120.setBackgroundColor(activity.getResources().getColor(R.color.timeBTNPressed));
                etTime.setText("120");
            }
        });
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRequestClick();
            }
        });
    }
    public boolean isEditTextEmpty(EditText editText){
        String ed_text = editText.getText().toString().trim();
        if(ed_text.length() == 0 || ed_text.equals("") || ed_text == null)
        {
            if(editText.getCurrentHintTextColor()!=Color.RED) {
                hintDefaultColor = editText.getCurrentHintTextColor();
                editText.setHintTextColor(Color.RED);
            }
            return true;
        }
        else
        {
            return false;
        }
    }
    private void onRequestClick(){
        Helper helper = new Helper();
        boolean flag1=isEditTextEmpty(etName);
        boolean flag2=isEditTextEmpty(etNumber);
        boolean flag3=isEditTextEmpty(etTime);
        boolean flag4=isEditTextEmpty(etLoc);
        if(flag1||flag2||flag3 ||flag4){
            Toast.makeText(activity, "Required Fields Missing", Toast.LENGTH_SHORT).show();
        }
        else if(!helper.isValidMobile(etNumber)){
            // custom dialog
            final Dialog dialog = new Dialog(getActivity(),R.style.MyAlertDialogStyle);
            dialog.setContentView(R.layout.dialog_alert);
            dialog.setCancelable(true);

            TextView tvAlert = (TextView)dialog.findViewById(R.id.tvAlert);
            tvAlert.setText(getResources().getString(R.string.number_alert));
            Button btnOk = (Button)dialog.findViewById(R.id.btnOK);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        else {
            if (webService.isNetworkConnected()){
                if(((AppBaseActivity)activity).isSnackBarVisible()) {
                    ((AppBaseActivity)activity).setSnackBarGone();
                }
                progress.setVisibility(View.VISIBLE);
                btnRequest.setEnabled(false);
                MySharedPreferences mySharedPreferences = new MySharedPreferences();
                webService.requestOrder(activity.getResources().getString(R.string.url) + "request_order"
                        , String.valueOf(mySharedPreferences.getRestaurantID(activity))
                        , etNumber.getText().toString()
                        , etName.getText().toString()
                        , etDetail.getText().toString()
                        , etLoc.getText().toString()
                        , etTime.getText().toString()
                        , String.valueOf(mySharedPreferences.getUserID(activity)), new WebService.VolleyResponseListener() {
                            @Override
                            public void onSuccess(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if(success){
                                        String message = jsonObject.getString("message");
                                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                        progress.setVisibility(View.GONE);
                                        etName.setText("");
                                        etTime.setText("");
                                        etLoc.setText("");
                                        etDetail.setText("");
                                        etNumber.setText("");
                                        Intent i = new Intent(activity, AppBaseActivity.class);
                                        i.putExtra("Uniqid","From_Order_Place");
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    }else {
                                        String message = jsonObject.getString("message");
                                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                        progress.setVisibility(View.GONE);
                                    }
                                    etNumber.setText("");
                                    etName.setText("");
                                    etDetail.setText("");
                                    etLoc.setText("");
                                    etTime.setText("");
                                    btnRequest.setEnabled(true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progress.setVisibility(View.GONE);
                                    btnRequest.setEnabled(true);
                                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                Helper helper = new Helper();
                                helper.volleyErrorMessage(activity,error);
                                progress.setVisibility(View.GONE);
                                btnRequest.setEnabled(true);
                            }
                        });
            }
            else {
                ((AppBaseActivity)activity).setSnackBarVisible();
            }
        }
    }
}
