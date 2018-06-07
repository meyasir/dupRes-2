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

public class TodayOrdersFragment extends Fragment {

    WebService webService;
    LinearLayout linlaHeaderProgress;
    List<Order> orderList;
    TodayOrdersListViewAdapter todayOrdersListViewAdapter;
    ListView lvTodayOrders;
    private Dialog dialogTodayOrder;
    private TextView message;

    Activity activity;

    public TodayOrdersFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_today_orders, container, false);
        init(rootView);
        loadData();

        return rootView;
    }

    private void init(View rootView){
        linlaHeaderProgress = (LinearLayout)rootView. findViewById(R.id.progress);
        lvTodayOrders = (ListView)rootView.findViewById(R.id.lvTodayOrders);
        message = (TextView) rootView.findViewById(R.id.message);

    }

    private void loadData(){
        if (webService.isNetworkConnected()){
            if(((AppBaseActivity)activity).isSnackBarVisible()) {
                ((AppBaseActivity)activity).setSnackBarGone();
            }
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            getTodayOrdersHistory();
        }
        else {
            ((AppBaseActivity)activity).setSnackBarVisible();
        }
    }

    private void getTodayOrdersHistory() {
        MySharedPreferences mySharedPreferences = new MySharedPreferences();
        webService.getOrdersRequest(activity.getResources().getString(R.string.url) + "today_orders_history",
                String.valueOf(mySharedPreferences.getRestaurantID(activity)), new WebService.VolleyResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean today_orders_history_available = jsonObject.getBoolean("today_orders_history_available");
                            if (today_orders_history_available) {
                                orderList = new ArrayList<>();
                                JSONArray todayDeliveryOrdersJSONArray = jsonObject.getJSONArray("to_day_orders");
                                for (int td = 0; td < todayDeliveryOrdersJSONArray.length(); td++) {
                                    JSONObject todayDeliveryOrderJSONObject = todayDeliveryOrdersJSONArray.getJSONObject(td);
                                    Order order = new Order();
                                    order.setId(todayDeliveryOrderJSONObject.getInt("order_id"));
                                    order.setDate(todayDeliveryOrderJSONObject.getString("order_date_time"));
                                    order.setCustomerName(todayDeliveryOrderJSONObject.getString("customer_name"));
                                    order.setStatus(todayDeliveryOrderJSONObject.getString("OrderStatus"));
                                    order.setOrderFoodItems(new ArrayList<OrderFoodItem>());
                                    JSONArray orderListsJSONArray = todayDeliveryOrderJSONObject.getJSONArray("order_lists");
                                    for (int ol = 0; ol < orderListsJSONArray.length(); ol++) {
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
        todayOrdersListViewAdapter = new TodayOrdersListViewAdapter(activity, orderList, new TodayOrdersListViewAdapter.OnViewClick() {
            @Override
            public void onViewClick(int position, int id, String name) {
                dialogTodayOrder = new Dialog(activity,R.style.MyAlertDialogStyle);
                dialogTodayOrder.setContentView(R.layout.dialog_waiting_orders);
                dialogTodayOrder.setTitle("Today Order");
                // if button is clicked, close the custom dialog
                ListView listView = (ListView)dialogTodayOrder.findViewById(R.id.listView);
                final TextView tvCustomerName = (TextView)dialogTodayOrder.findViewById(R.id.tvCustomerName);
                final TextView price = (TextView) dialogTodayOrder.findViewById(R.id.price);
                tvCustomerName.setText(name);
                final Button btnOK = (Button)dialogTodayOrder.findViewById(R.id.btnOK);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogTodayOrder.dismiss();
                    }
                });

                NewOrdersItemListViewAdapter newOrdersItemListViewAdapter = new NewOrdersItemListViewAdapter(activity, orderList.get(position).getOrderFoodItems());
                listView.setAdapter(newOrdersItemListViewAdapter);
                ArrayList<OrderFoodItem> orderFoodItems = orderList.get(position).getOrderFoodItems();
                int t = 0;
                for(OrderFoodItem orderFoodItem:orderFoodItems){
                    t+=(Integer.valueOf(orderFoodItem.getPrice())*Integer.valueOf(orderFoodItem.getQuantity()));
                }
                price.setText(t+" RS");
                dialogTodayOrder.show();
            }
        });
        lvTodayOrders.setAdapter(todayOrdersListViewAdapter);
    }

}
