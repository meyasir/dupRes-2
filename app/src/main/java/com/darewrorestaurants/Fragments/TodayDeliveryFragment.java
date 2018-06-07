package com.darewrorestaurants.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.darewrorestaurants.Activities.AppBaseActivity;
import com.darewrorestaurants.Adapters.NewOrdersItemListViewAdapter;
import com.darewrorestaurants.Adapters.TodayDeliveryListViewAdapter;
import com.darewrorestaurants.Adapters.TodayOrdersListViewAdapter;
import com.darewrorestaurants.Adapters.WaitingOrdersListViewAdapter;
import com.darewrorestaurants.Models.Order;
import com.darewrorestaurants.Models.OrderFoodItem;
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

public class TodayDeliveryFragment extends Fragment {

    WebService webService;
    LinearLayout linlaHeaderProgress;
    List<Order> orderList;
    TodayDeliveryListViewAdapter todayDeliveryListViewAdapter;
    ListView lvTodayDelivery;
    private Dialog dialogDeliveryOrder;
    private TextView message;

    Activity activity;

    public TodayDeliveryFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_today_delivery, container, false);
        init(rootView);
        loadData();

        return rootView;
    }

    private void init(View rootView){
        linlaHeaderProgress = (LinearLayout)rootView. findViewById(R.id.progress);
        lvTodayDelivery = (ListView)rootView.findViewById(R.id.lvTodayDelivery);
        message = (TextView) rootView.findViewById(R.id.message);

    }

    private void loadData(){
        if (webService.isNetworkConnected()){
            if(((AppBaseActivity)activity).isSnackBarVisible()) {
                ((AppBaseActivity)activity).setSnackBarGone();
            }
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            getTodayDeliveryOrders();
        }
        else {
            ((AppBaseActivity)activity).setSnackBarVisible();
        }
    }

    private void getTodayDeliveryOrders() {
        MySharedPreferences mySharedPreferences = new MySharedPreferences();
        webService.getOrdersRequest(activity.getResources().getString(R.string.url) + "today_delivery_requests_history",
                String.valueOf(mySharedPreferences.getRestaurantID(activity)),
                String.valueOf(mySharedPreferences.getUserID(activity)),
                new WebService.VolleyResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean today_delivery_requests_history_available = jsonObject.getBoolean("today_delivery_requests_history_available");
                            if (today_delivery_requests_history_available) {
                                orderList = new ArrayList<>();
                                JSONArray todayDeliveryOrdersJSONArray = jsonObject.getJSONArray("to_day_delivery_requests");
                                for (int td = 0; td < todayDeliveryOrdersJSONArray.length(); td++) {
                                    JSONObject todayDeliveryOrderJSONObject = todayDeliveryOrdersJSONArray.getJSONObject(td);
                                    Order order = new Order();
                                    order.setId(todayDeliveryOrderJSONObject.getInt("order_id"));
                                    order.setDate(todayDeliveryOrderJSONObject.getString("order_date_time"));
                                    order.setCustomerName(todayDeliveryOrderJSONObject.getString("customer_name"));
                                    order.setStatus(todayDeliveryOrderJSONObject.getString("OrderStatus"));
                                    order.setDetail(todayDeliveryOrderJSONObject.getString("order_detail"));
                                    orderList.add(order);
                                }
                                setTodayDeliveryOrders();
                                message.setVisibility(View.GONE);
                                linlaHeaderProgress.setVisibility(View.GONE);
                            } else {
//                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                        helper.volleyErrorMessage(activity, error);
                        linlaHeaderProgress.setVisibility(View.GONE);
                    }
                });
    }

    private void setTodayDeliveryOrders(){
        todayDeliveryListViewAdapter = new TodayDeliveryListViewAdapter(activity, orderList, new TodayDeliveryListViewAdapter.OnViewClick() {
            @Override
            public void onViewClick(int position, int id, String name) {
                dialogDeliveryOrder = new Dialog(activity,R.style.MyAlertDialogStyle);
                dialogDeliveryOrder.setContentView(R.layout.dialog_today_delivery_orders);
                dialogDeliveryOrder.setTitle("Today Order");
                // if button is clicked, close the custom dialog
                final TextView tvCustomerName = (TextView)dialogDeliveryOrder.findViewById(R.id.tvCustomerName);
                final TextView tvDetail = (TextView)dialogDeliveryOrder.findViewById(R.id.tvDetail);

                tvCustomerName.setText(name);
                final Button btnOK = (Button)dialogDeliveryOrder.findViewById(R.id.btnOK);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogDeliveryOrder.dismiss();
                    }
                });
                String details = orderList.get(position).getDetail().replace("<br />","\n");
                tvDetail.setText(details);
                dialogDeliveryOrder.show();
            }
        });
        lvTodayDelivery.setAdapter(todayDeliveryListViewAdapter);
    }

}
