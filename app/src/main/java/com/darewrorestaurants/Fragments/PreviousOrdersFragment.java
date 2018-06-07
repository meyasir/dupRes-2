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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.darewrorestaurants.Activities.AppBaseActivity;
import com.darewrorestaurants.Adapters.NewOrdersItemListViewAdapter;
import com.darewrorestaurants.Adapters.TodayDeliveryListViewAdapter;
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

public class PreviousOrdersFragment extends Fragment {

    WebService webService;
    LinearLayout linlaHeaderProgress;
    RelativeLayout relativeLayout;
    List<Order> orderList;
    List<OrderFoodItem> orderFoodItemList;
    TodayDeliveryListViewAdapter todayDeliveryListViewAdapter;
    ListView lvTodayDelivery;
    private Dialog dialogDeliveryOrder;
    private TextView message;

    Activity activity;

    public PreviousOrdersFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_previous_orders, container, false);
        init(rootView);
        loadData();

        return rootView;
    }

    private void init(View rootView){
        linlaHeaderProgress = (LinearLayout)rootView. findViewById(R.id.progress);
        relativeLayout = (RelativeLayout) rootView. findViewById(R.id.relativeLayout);
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
        webService.getOrdersRequest(activity.getResources().getString(R.string.url)+ "previous_orders_history",
                String.valueOf(mySharedPreferences.getRestaurantID(activity)), new WebService.VolleyResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean previous_orders_history_available = jsonObject.getBoolean("previous_orders_history_available");
                            if (previous_orders_history_available) {
                                orderList = new ArrayList<>();
                                JSONArray todayDeliveryOrdersJSONArray = jsonObject.getJSONArray("previous_orders");
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
                                message.setText(jsonObject.getString("message"));
                                message.setVisibility(View.VISIBLE);
                                linlaHeaderProgress.setVisibility(View.GONE);
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
            public void onViewClick(final int position, int id, String name) {
                dialogDeliveryOrder = new Dialog(activity,R.style.MyAlertDialogStyle);
                dialogDeliveryOrder.setContentView(R.layout.dialog_waiting_orders);
                dialogDeliveryOrder.setTitle("Previous Order");
                // if button is clicked, close the custom dialog
                final TextView tvCustomerName = (TextView)dialogDeliveryOrder.findViewById(R.id.tvCustomerName);
                final ListView listView = (ListView)dialogDeliveryOrder.findViewById(R.id.listView);
                tvCustomerName.setText(name);
                final TextView price = (TextView) dialogDeliveryOrder.findViewById(R.id.price);
                tvCustomerName.setText(name);
                final Button btnOK = (Button)dialogDeliveryOrder.findViewById(R.id.btnOK);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogDeliveryOrder.dismiss();
                    }
                });

                if (webService.isNetworkConnected()){
                    if(((AppBaseActivity)activity).isSnackBarVisible()) {
                        ((AppBaseActivity)activity).setSnackBarGone();
                    }
                    relativeLayout.setVisibility(View.GONE);
                    linlaHeaderProgress.setVisibility(View.VISIBLE);
                    webService.getOrdersDetailList(activity.getResources().getString(R.string.url) + "order_detail_list",
                            String.valueOf(id),
                            new WebService.VolleyResponseListener() {
                                @Override
                                public void onSuccess(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        if (success) {
                                            orderFoodItemList = new ArrayList<>();
                                            JSONArray orderListJSONArray = jsonObject.getJSONArray("order_lists");
                                            for (int ol = 0; ol < orderListJSONArray.length(); ol++) {
                                                JSONObject orderListJSONObject = orderListJSONArray.getJSONObject(ol);
                                                OrderFoodItem orderFoodItem = new OrderFoodItem();
                                                orderFoodItem.setName(orderListJSONObject.getString("restaurant_food_name"));
                                                orderFoodItem.setWeight(orderListJSONObject.getString("restaurant_food_quantity"));
                                                orderFoodItem.setQuantity(orderListJSONObject.getString("quantity"));
                                                orderFoodItem.setPrice(orderListJSONObject.getString("restaurant_food_price"));
                                                orderFoodItemList.add(orderFoodItem);
                                            }
                                            linlaHeaderProgress.setVisibility(View.GONE);
                                            relativeLayout.setVisibility(View.VISIBLE);
                                            NewOrdersItemListViewAdapter newOrdersItemListViewAdapter = new NewOrdersItemListViewAdapter(activity, orderFoodItemList);
                                            listView.setAdapter(newOrdersItemListViewAdapter);

                                            int t = 0;
                                            for(OrderFoodItem orderFoodItem:orderFoodItemList){
                                                t+=(Integer.valueOf(orderFoodItem.getPrice())*Integer.valueOf(orderFoodItem.getQuantity()));
                                            }
                                            price.setText(t+" RS");
                                            dialogDeliveryOrder.show();
                                        } else {
                                            Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                            linlaHeaderProgress.setVisibility(View.GONE);
                                            relativeLayout.setVisibility(View.VISIBLE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                                        linlaHeaderProgress.setVisibility(View.GONE);
                                        relativeLayout.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    Helper helper = new Helper();
                                    helper.volleyErrorMessage(activity, error);
                                    linlaHeaderProgress.setVisibility(View.GONE);
                                    relativeLayout.setVisibility(View.VISIBLE);
                                }
                            });
                }
                else {
                    ((AppBaseActivity)activity).setSnackBarVisible();
                }

            }
        });
        lvTodayDelivery.setAdapter(todayDeliveryListViewAdapter);
    }

}
