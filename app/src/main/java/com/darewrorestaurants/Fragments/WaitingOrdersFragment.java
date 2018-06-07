package com.darewrorestaurants.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.darewrorestaurants.Activities.AppBaseActivity;
import com.darewrorestaurants.Adapters.NewOrdersItemListViewAdapter;
import com.darewrorestaurants.Adapters.NewOrdersListViewAdapter;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jaffar on 2018-02-14.
 */

public class WaitingOrdersFragment extends Fragment {

    WebService webService;
    LinearLayout linlaHeaderProgress;
    List<Order> orderList;
    Activity activity;
    WaitingOrdersListViewAdapter waitingOrdersListViewAdapter;
    ListView lvWaitingOrders;
    private Dialog dialogWatingOrder;
    private TextView message;


    public WaitingOrdersFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_waiting_orders, container, false);
        init(rootView);
        loadData();
        return rootView;
    }
    private void init(View rootView){
        linlaHeaderProgress = (LinearLayout)rootView. findViewById(R.id.progress);
        lvWaitingOrders = (ListView)rootView.findViewById(R.id.lvWaitingOrders);
        message = (TextView) rootView.findViewById(R.id.message);
    }

    private void loadData(){
        if (webService.isNetworkConnected()){
            if(((AppBaseActivity)activity).isSnackBarVisible()) {
                ((AppBaseActivity)activity).setSnackBarGone();
            }
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            getWaitingOrders();
        }
        else {
            ((AppBaseActivity)activity).setSnackBarVisible();
        }
    }
    private void getWaitingOrders(){
        MySharedPreferences mySharedPreferences = new MySharedPreferences();
        webService.getOrdersRequest(activity.getResources().getString(R.string.url) + "waiting_orders",
                String.valueOf(mySharedPreferences.getRestaurantID(activity)), new WebService.VolleyResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean waiting_orders_available = jsonObject.getBoolean("waiting_orders_available");
                            if(waiting_orders_available){
                                orderList = new ArrayList<>();
                                JSONArray waitingOrdersJSONArray = jsonObject.getJSONArray("waiting_orders");
                                for(int wo= 0; wo<waitingOrdersJSONArray.length();wo++){
                                    JSONObject waitingOrderJSONObject= waitingOrdersJSONArray.getJSONObject(wo);
                                    Order order = new Order();
                                    order.setId(waitingOrderJSONObject.getInt("order_id"));
                                    order.setDate(waitingOrderJSONObject.getString("order_date_time"));
                                    order.setCustomerName(waitingOrderJSONObject.getString("customer_name"));
                                    order.setStatus(waitingOrderJSONObject.getString("OrderStatus"));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String givenDateString = waitingOrderJSONObject.getString("order_ready_time");
                                    long timeInMilliseconds=0;
                                    try {
                                        Date mDate = sdf.parse(givenDateString);
                                        timeInMilliseconds = mDate.getTime();
                                        System.out.println("Date in milli :: " + timeInMilliseconds);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    order.setReadyTime(timeInMilliseconds);
                                    order.setOrderFoodItems(new ArrayList<OrderFoodItem>());
                                    JSONArray orderListsJSONArray = waitingOrderJSONObject.getJSONArray("order_lists");
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
                                setWaitingOrders();
                                message.setVisibility(View.GONE);
                                linlaHeaderProgress.setVisibility(View.GONE);
                            }
                            else {
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
                        helper.volleyErrorMessage(activity,error);
                        linlaHeaderProgress.setVisibility(View.GONE);
                    }
                });
    }

    private void setWaitingOrders(){
        waitingOrdersListViewAdapter = new WaitingOrdersListViewAdapter(activity, orderList, new WaitingOrdersListViewAdapter.OnViewClick() {
            @Override
            public void onViewClick(int position, int id, String name) {
                dialogWatingOrder = new Dialog(activity,R.style.MyAlertDialogStyle);
                dialogWatingOrder.setContentView(R.layout.dialog_waiting_orders);
                dialogWatingOrder.setTitle("Waiting Order");
                // if button is clicked, close the custom dialog
                ListView listView = (ListView)dialogWatingOrder.findViewById(R.id.listView);
                final TextView tvCustomerName = (TextView)dialogWatingOrder.findViewById(R.id.tvCustomerName);
                tvCustomerName.setText(name);
                final TextView price = (TextView) dialogWatingOrder.findViewById(R.id.price);
                final Button btnOK = (Button)dialogWatingOrder.findViewById(R.id.btnOK);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogWatingOrder.dismiss();
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
                dialogWatingOrder.show();
            }
        });
        lvWaitingOrders.setAdapter(waitingOrdersListViewAdapter);
    }
}