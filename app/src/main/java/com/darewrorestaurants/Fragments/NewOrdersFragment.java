package com.darewrorestaurants.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.darewrorestaurants.Activities.AppBaseActivity;
import com.darewrorestaurants.Activities.LoginActivity;
import com.darewrorestaurants.Adapters.NewOrdersItemListViewAdapter;
import com.darewrorestaurants.Adapters.NewOrdersListViewAdapter;
import com.darewrorestaurants.Models.FoodItem;
import com.darewrorestaurants.Models.OrderFoodItem;
import com.darewrorestaurants.Models.Order;
import com.darewrorestaurants.R;
import com.darewrorestaurants.Utilities.Helper;
import com.darewrorestaurants.Utilities.MySharedPreferences;
import com.darewrorestaurants.Utilities.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaffar on 2018-02-14.
 */

public class NewOrdersFragment extends Fragment {
    static ArrayList<RadioButton> deliveryTypeRBArrayList;
    WebService webService;
    LinearLayout linlaHeaderProgress;
    List<Order> orderList;
    NewOrdersListViewAdapter newOrdersListViewAdapter;
    ListView lvNewOrders;
    private Dialog dialogNewOrder;
    private TextView message;

    Activity activity;
    public NewOrdersFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_new_orders, container, false);
        init(rootView);
        loadData();

        return rootView;
    }

    private void init(View rootView){
        linlaHeaderProgress = (LinearLayout)rootView. findViewById(R.id.progress);
        lvNewOrders = (ListView)rootView.findViewById(R.id.lvNewOrders);
        message = (TextView) rootView.findViewById(R.id.message);
    }

    private void loadData(){
        if (webService.isNetworkConnected()){
            if(((AppBaseActivity)activity).isSnackBarVisible()) {
                ((AppBaseActivity)activity).setSnackBarGone();
            }
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            getNewOrders();
        }
        else {
            ((AppBaseActivity)activity).setSnackBarVisible();
        }
    }
    private void getNewOrders(){
        MySharedPreferences mySharedPreferences = new MySharedPreferences();
        webService.getOrdersRequest(activity.getResources().getString(R.string.url) + "new_orders",
                String.valueOf(mySharedPreferences.getRestaurantID(activity)), new WebService.VolleyResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean new_orders_available = jsonObject.getBoolean("new_orders_available");
                            if(new_orders_available){
                                orderList = new ArrayList<>();
                                JSONArray newOrdersJSONArray = jsonObject.getJSONArray("new_orders");
                                for(int no= 0; no<newOrdersJSONArray.length();no++){
                                    JSONObject newOrderJSONObject= newOrdersJSONArray.getJSONObject(no);
                                    Order order = new Order();
                                    order.setId(newOrderJSONObject.getInt("order_id"));
                                    order.setDate(newOrderJSONObject.getString("order_date_time"));
                                    order.setCustomerName(newOrderJSONObject.getString("customer_name"));
                                    order.setOrderFoodItems(new ArrayList<OrderFoodItem>());
                                    JSONArray orderListsJSONArray = newOrderJSONObject.getJSONArray("order_lists");
                                    for(int ol =0; ol<orderListsJSONArray.length();ol++){
                                        JSONObject orderListJSONObject = orderListsJSONArray.getJSONObject(ol);
                                        OrderFoodItem orderFoodItem = new OrderFoodItem();
                                        orderFoodItem.setName(orderListJSONObject.getString("restaurant_food_name"));
                                        orderFoodItem.setWeight(orderListJSONObject.getString("restaurant_food_quantity"));
                                        orderFoodItem.setQuantity(orderListJSONObject.getString("quantity"));
                                        orderFoodItem.setPrice(orderListJSONObject.getString("restaurant_food_price"));
                                        order.getOrderFoodItems().add(orderFoodItem);
                                    }
                                    orderList.add(order);
                                }
                                setNewOrders();
                                message.setVisibility(View.GONE);
                                linlaHeaderProgress.setVisibility(View.GONE);
                            }
                            else {
                                //Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                linlaHeaderProgress.setVisibility(View.GONE);
                                message.setText(jsonObject.getString("message"));
                                message.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                            linlaHeaderProgress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {
                        Helper helper = new Helper();
                        helper.volleyErrorMessage(activity,error);
                        linlaHeaderProgress.setVisibility(View.GONE);
                    }
                });
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
    private void setNewOrders(){

        newOrdersListViewAdapter = new NewOrdersListViewAdapter(activity, orderList, new NewOrdersListViewAdapter.OnViewClick() {
            @Override
            public void onViewClick(int position, final int id, String name) {
                dialogNewOrder = new Dialog(activity,R.style.SuperMaterialTheme);
                dialogNewOrder.setContentView(R.layout.dialog_new_order_process);
                dialogNewOrder.setTitle("New Order");
                // if button is clicked, close the custom dialog
                ListView listView = (ListView)dialogNewOrder.findViewById(R.id.listView);
                final TextView tvCustomerName = (TextView)dialogNewOrder.findViewById(R.id.tvCustomerName);
                final Button time20 = (Button)dialogNewOrder.findViewById(R.id.time20);
                final Button time40 = (Button)dialogNewOrder.findViewById(R.id.time40);
                final Button time60 = (Button)dialogNewOrder.findViewById(R.id.time60);
                final Button time120 = (Button)dialogNewOrder.findViewById(R.id.time120);
                final EditText etName = (EditText)dialogNewOrder.findViewById(R.id.etName);
                final EditText etTime = (EditText)dialogNewOrder.findViewById(R.id.etTime);
                setDefaultHintColor(etName);
                setDefaultHintColor(etTime);
                final TextView price = (TextView) dialogNewOrder.findViewById(R.id.price);
                final LinearLayout progress = (LinearLayout)dialogNewOrder.findViewById(R.id.progress);

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
                tvCustomerName.setText(name);
                etName.setText(name);

                NewOrdersItemListViewAdapter newOrdersItemListViewAdapter = new NewOrdersItemListViewAdapter(activity, orderList.get(position).getOrderFoodItems());
                listView.setAdapter(newOrdersItemListViewAdapter);
                ArrayList<OrderFoodItem> orderFoodItems = orderList.get(position).getOrderFoodItems();
                int t = 0;
                for(OrderFoodItem orderFoodItem:orderFoodItems){
                    t+=(Integer.valueOf(orderFoodItem.getPrice())*Integer.valueOf(orderFoodItem.getQuantity()));
                }
                price.setText(t+" RS");
                final Button btnProcess = (Button)dialogNewOrder.findViewById(R.id.btnProcess);
                final Button btnCancel = (Button)dialogNewOrder.findViewById(R.id.btnCancel);

                btnProcess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Helper helper = new Helper();
                        boolean flag1=isEditTextEmpty(etName);
                        boolean flag3=isEditTextEmpty(etTime);
                        if(flag1||flag3){
                            Toast.makeText(activity, "Required fields missing", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (webService.isNetworkConnected()){
                                if(((AppBaseActivity)activity).isSnackBarVisible()) {
                                    ((AppBaseActivity)activity).setSnackBarGone();
                                }
                                progress.setVisibility(View.VISIBLE);
                                btnCancel.setEnabled(false);
                                btnProcess.setEnabled(false);
                                MySharedPreferences mySharedPreferences = new MySharedPreferences();
                                webService.proceedOrder(activity.getResources().getString(R.string.url) + "place_order"
                                        , String.valueOf(id)
                                        , String.valueOf(mySharedPreferences.getUserID(activity))
                                        , etName.getText().toString()
                                        , etTime.getText().toString(), new WebService.VolleyResponseListener() {
                                            @Override
                                            public void onSuccess(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    boolean success = jsonObject.getBoolean("success");
                                                    if(success){
                                                        String message = jsonObject.getString("message");
                                                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                                        dialogNewOrder.dismiss();
                                                        Intent i = new Intent(activity, AppBaseActivity.class);
                                                        i.putExtra("Uniqid","From_Order_Place");
                                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(i);
                                                    }else {
                                                        String message = jsonObject.getString("message");
                                                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                                        boolean refresh = jsonObject.getBoolean("refresh");
                                                        if(refresh){
                                                            Intent i = new Intent(activity, AppBaseActivity.class);
                                                            i.putExtra("Uniqid","From_Order_Place");
                                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(i);
                                                        }
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    progress.setVisibility(View.GONE);
                                                    btnCancel.setEnabled(true);
                                                    btnProcess.setEnabled(true);
                                                }
                                            }

                                            @Override
                                            public void onError(VolleyError error) {
                                                Helper helper = new Helper();
                                                helper.volleyErrorMessage(activity,error);
                                                progress.setVisibility(View.GONE);
                                                btnCancel.setEnabled(true);
                                                btnProcess.setEnabled(true);
                                            }
                                        });
                            }
                            else {
                                ((AppBaseActivity)activity).setSnackBarVisible();
                            }
                        }
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialogCancel = new Dialog(activity,R.style.MyAlertDialogStyle);
                        dialogCancel.setContentView(R.layout.dialog_cancel);
                        dialogCancel.setTitle("Cancel Order");
                        dialogCancel.setCancelable(true);
                        final Button btnSubmit = (Button)dialogCancel.findViewById(R.id.btnSubmit);
                        final EditText etReason = (EditText)dialogCancel.findViewById(R.id.etReason);
                        etReason.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                for(RadioButton radioButton:deliveryTypeRBArrayList){
                                    radioButton.setChecked(false);
                                }
                            }
                        });
//                        final Spinner spinner = (Spinner)dialogCancel.findViewById(R.id.spinner);
                        final LinearLayout linearLayoutCR = (LinearLayout)dialogCancel.findViewById(R.id.linearLayoutCR);


                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                    if (webService.isNetworkConnected()){
                                        if(((AppBaseActivity)activity).isSnackBarVisible()) {
                                            ((AppBaseActivity)activity).setSnackBarGone();
                                        }
                                        String reason = "";
                                        boolean check = false;
                                        int rbid = -1;
                                        for(RadioButton rb:deliveryTypeRBArrayList){
                                            if(rb.isChecked()){
                                                check = true;
                                                rbid = rb.getId();
                                                break;
                                            }
                                        }
                                        if(etReason.getText().toString().matches("")
                                                &&!check){
                                            Toast.makeText(activity, "Please select or enter cancel reason", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            progress.setVisibility(View.VISIBLE);
                                            btnCancel.setEnabled(false);
                                            btnProcess.setEnabled(false);
                                            dialogCancel.dismiss();
                                            if(!etReason.getText().toString().matches("")){
                                                reason = etReason.getText().toString();
                                            }
                                            else
                                                reason = deliveryTypeRBArrayList.get(rbid).getText().toString();
                                            webService.cancelOrder(activity.getResources().getString(R.string.url) + "cancel_order"
                                                    , String.valueOf(id)
                                                    , reason
                                                    , new WebService.VolleyResponseListener() {
                                                        @Override
                                                        public void onSuccess(String response) {
                                                            try {
                                                                JSONObject jsonObject = new JSONObject(response);
                                                                boolean success = jsonObject.getBoolean("success");
                                                                if(success){
                                                                    String message = jsonObject.getString("message");
                                                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                                                    dialogNewOrder.dismiss();
                                                                    Intent i = new Intent(activity, AppBaseActivity.class);
                                                                    i.putExtra("Uniqid","From_Order_Place");
                                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivity(i);
                                                                }else {
                                                                    String message = jsonObject.getString("message");
                                                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                progress.setVisibility(View.GONE);
                                                                btnCancel.setEnabled(true);
                                                                btnProcess.setEnabled(true);
                                                            }

                                                        }

                                                        @Override
                                                        public void onError(VolleyError error) {
                                                            Helper helper = new Helper();
                                                            helper.volleyErrorMessage(activity,error);
                                                            progress.setVisibility(View.GONE);
                                                            btnCancel.setEnabled(true);
                                                            btnProcess.setEnabled(true);
                                                        }
                                                    });
                                        }
                                    }
                                    else {
                                        ((AppBaseActivity)activity).setSnackBarVisible();
                                    }
                            }
                        });
                        if (webService.isNetworkConnected()){
                            if(((AppBaseActivity)activity).isSnackBarVisible()) {
                                ((AppBaseActivity)activity).setSnackBarGone();
                            }
                            progress.setVisibility(View.VISIBLE);
                            webService.getCancelReasons(activity.getResources().getString(R.string.url) + "get_cancel_reasons"
                                    , new WebService.VolleyResponseListener() {
                                        @Override
                                        public void onSuccess(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                JSONArray jsonArray = jsonObject.getJSONArray("reasons");
                                                List<String> spinnerArray =  new ArrayList<String>();
                                                for(int i=0;i<jsonArray.length();i++)
                                                    spinnerArray.add(jsonArray.getString(i));

//                                                ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
//                                                        activity, android.R.layout.simple_spinner_item, spinnerArray);
//                                                mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                                spinner.setAdapter(mAdapter);
                                                int i=0;
                                                deliveryTypeRBArrayList = new ArrayList<>();
                                                for(String cancelReason:spinnerArray){
                                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                    params.gravity = Gravity.CENTER;
                                                    RadioButton radioButton = new RadioButton(activity);
                                                    radioButton.setId(i);
                                                    i++;
                                                    radioButton.setText(cancelReason);
                                                    radioButton.setTextAppearance(activity,android.R.style.TextAppearance_Medium);
                                                    radioButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            onDeliveryTypeRadioButtonClicked(v);
                                                        }
                                                    });
                                                    radioButton.setLayoutParams(params);
                                                    deliveryTypeRBArrayList.add(radioButton);

                                                    linearLayoutCR.addView(radioButton);
                                                }
                                                linearLayoutCR.setVisibility(View.VISIBLE);
                                                progress.setVisibility(View.GONE);
                                                dialogCancel.show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                progress.setVisibility(View.GONE);
                                            }

                                        }

                                        @Override
                                        public void onError(VolleyError error) {
                                            Helper helper = new Helper();
                                            helper.volleyErrorMessage(activity,error);
                                            progress.setVisibility(View.GONE);
                                        }
                                    });
                        }
                        else {
                            ((AppBaseActivity)activity).setSnackBarVisible();
                        }

                    }
                });
                dialogNewOrder.show();
            }
        });
        lvNewOrders.setAdapter(newOrdersListViewAdapter);
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
    public void onDeliveryTypeRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        for(RadioButton radioButton:deliveryTypeRBArrayList){
            if(radioButton.getId()!=view.getId()){
                radioButton.setChecked(false);
            }
        }
    }
}
