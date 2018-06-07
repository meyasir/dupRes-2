package com.darewrorestaurants.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.darewrorestaurants.Models.FoodItem;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jaffar on 3/1/2017.
 */
public class WebService {

    Context mContext;
    //new added
    private RequestQueue mRequestQueue;
    private static WebService mInstance;

    public WebService(Context mContext) {
        this.mContext = mContext;
    }

    //new added
    public static synchronized WebService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WebService(context);
        }
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
//    public void userUpdateTokenKey(final String url, final User user, final VolleyResponseListener volleyResponseListener){
//        try {
//            RequestQueue queue = Volley.newRequestQueue(mContext);
//
//            StringRequest req = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String s) {
//                            volleyResponseListener.onSuccess(s);
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    volleyResponseListener.onError(volleyError);
//                }
//            }){
//                @Override
//                protected Map<String, String> getParams(){
//                    HashMap<String,String> params = new HashMap<String,String>();
//                    params.put("user_id", String.valueOf(user.getId()));
//                    params.put("mobile_token", user.getTokenKey());
//
//                    return params;
//                }
//            };
//
//            queue.add(req);
//        }catch (Exception e){}
//    }
//
//    public void getFoodItems(String url, final String res_id, final VolleyResponseListener volleyResponseListener){
//        try {
//            RequestQueue queue = Volley.newRequestQueue(mContext);
//
//            StringRequest req = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String s) {
//                            volleyResponseListener.onSuccess(s);
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    volleyResponseListener.onError(volleyError);
//                }
//            }){
//                @Override
//                protected Map<String, String> getParams(){
//                    HashMap<String,String> params = new HashMap<String,String>();
//                    params.put("restaurant_id",res_id);
//
//                    return params;
//                }
//            };
//
//            queue.add(req);
//        }catch (Exception e){}
//    }

    public void customerUpdateTokenKey(final String url, final String id, final String token, final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put("user_id", id);
                    params.put("mobile_token", token);

                    return params;
                }
            };

            queue.add(req);
        }catch (Exception e){}
    }
    public void restaurantLoginRequest(String url, final String userName,final String password,final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put(Constants.USER_NAME,userName);
                    params.put("password",password);
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }

    public void getOrdersRequest(String url, final String resID,  final String userID,final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put(Constants.RESTAURANT_ID,resID);
                    params.put(Constants.USER_ID,userID);
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }

    public void getOrdersDetailList(String url, final String orderID, final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put("order_id",orderID);
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }

    public void getOrdersRequest(String url, final String resID,final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put(Constants.RESTAURANT_ID,resID);
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }
    public void proceedOrder(String url, final String orderID, final String userID, final String name, final String time, final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put("order_id",orderID);
                    params.put("order_name",name);
                    params.put("order_ready_time",time);
                    params.put(Constants.USER_ID,userID);
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }
    public void requestOrder(String url,
                             final String resID,
                             final String mobileNumber,
                             final String name,
                             final String orderDetail,
                             final String loc,
                             final String readyTime,
                             final String userID,
                             final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put("restaurant_id",resID);
                    params.put("customer_mobile_no",mobileNumber);
                    params.put("customer_name",name);
                    params.put("order_detail",orderDetail);
                    params.put("delivery_location",loc);
                    params.put("order_ready_time_field",readyTime);
                    params.put("user_id",userID);
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }
    public void cancelOrder(String url, final String orderID, final String reason, final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put("order_id",orderID);
                    params.put("reason",reason);
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }
    public void getCancelReasons(String url, final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }
    public void getFoodItemsRequest(String url, final String resID,final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put(Constants.RESTAURANT_ID,resID);
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }

    public void addRestaurantFoodItem(String url, final String resID, final String resFoodCategoryID, final FoodItem foodItem,final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put("restaurant_id",resID);
                    params.put("restaurant_food_category_id",resFoodCategoryID);
                    params.put("restaurant_food_name",foodItem.getName());
                    params.put("restaurant_food_price",foodItem.getPrice());
                    params.put("restaurant_food_quantity",foodItem.getWeight());
                    params.put("restaurant_food_description",foodItem.getDesc());
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }

    public void addRestaurantFoodCategory(String url, final String resID, final String resFoodCategory, final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put("restaurant_id",resID);
                    params.put("restaurant_food_category",resFoodCategory);
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }

    public void updateRestaurantFoodItem(String url, final String resID, final String resFoodCategoryID, final FoodItem foodItem, final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put("restaurant_food_category_id",resFoodCategoryID);
                    params.put("restaurant_food_menu_id",String.valueOf(foodItem.getId()));
                    params.put("restaurant_id",resID);
                    params.put("restaurant_food_name",foodItem.getName());
                    params.put("restaurant_food_price",foodItem.getPrice());
                    params.put("restaurant_food_quantity",foodItem.getWeight());
                    params.put("restaurant_food_description",foodItem.getDesc());
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }
//    public void changeUserTitle(String url, final String userID,final String userTitle,final VolleyResponseListener volleyResponseListener){
//        try {
//            RequestQueue queue = Volley.newRequestQueue(mContext);
//
//            StringRequest req = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String s) {
//                            volleyResponseListener.onSuccess(s);
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    volleyResponseListener.onError(volleyError);
//                }
//            }){
//                @Override
//                protected Map<String, String> getParams(){
//                    HashMap<String,String> params = new HashMap<String,String>();
//                    params.put(MySharedPreferences.USER_ID,userID);
//                    params.put(MySharedPreferences.USER_TITLE,userTitle);
//                    return params;
//                }
//            };
//
//            queue.add(req);
//        }catch (Exception e){}
//    }
//    public void changeUserEmail(String url, final String userID,final String userEmail,final VolleyResponseListener volleyResponseListener){
//        try {
//            RequestQueue queue = Volley.newRequestQueue(mContext);
//
//            StringRequest req = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String s) {
//                            volleyResponseListener.onSuccess(s);
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    volleyResponseListener.onError(volleyError);
//                }
//            }){
//                @Override
//                protected Map<String, String> getParams(){
//                    HashMap<String,String> params = new HashMap<String,String>();
//                    params.put(MySharedPreferences.USER_ID,userID);
//                    params.put(MySharedPreferences.USER_EMAIL,userEmail);
//                    return params;
//                }
//            };
//
//            queue.add(req);
//        }catch (Exception e){}
//    }
    public void changePassword(String url, final String userID,final String curPassword,final String newPassword,final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put(Constants.USER_ID,userID);
                    params.put("user_password",curPassword);
                    params.put("new_password",newPassword);
                    return params;
                }
            };

            queue.add(req);
        }catch (Exception e){}
    }
//
//    public void getOrders(String url, final String resID, final VolleyResponseListener volleyResponseListener){
//        try {
//            RequestQueue queue = Volley.newRequestQueue(mContext);
//
//            StringRequest req = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String s) {
//                            volleyResponseListener.onSuccess(s);
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    volleyResponseListener.onError(volleyError);
//                }
//            }){
//                @Override
//                protected Map<String, String> getParams(){
//                    HashMap<String,String> params = new HashMap<String,String>();
//                    params.put(MySharedPreferences.USER_ID,resID);
//                    return params;
//                }
//            };
//
//            queue.add(req);
//        }catch (Exception e){}
//    }

    public void loadImage(String url, final VolleyImageResponse volleyImageResponse){
        // Retrieves an image specified by the URL, displays it in the UI.
        //RequestQueue queue = Volley.newRequestQueue(mContext);
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        volleyImageResponse.onSuccess(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        volleyImageResponse.onError(error);
                    }
                });

        //queue.add(request);
        // Add a request (in this example, called stringRequest) to your RequestQueue.
        WebService.getInstance(mContext).addToRequestQueue(request);
    }

    public interface VolleyResponseListener {
        void onSuccess(String response);
        void onError(VolleyError error);
    }

    public interface VolleyImageResponse {
        void onSuccess(Bitmap bitmap);
        void onError(VolleyError error);
    }
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
