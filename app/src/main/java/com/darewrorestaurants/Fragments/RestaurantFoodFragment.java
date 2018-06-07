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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.darewrorestaurants.Activities.AppBaseActivity;
import com.darewrorestaurants.Adapters.FoodItemCustomAdapter;
import com.darewrorestaurants.Models.FoodCategory;
import com.darewrorestaurants.Models.FoodItem;
import com.darewrorestaurants.Models.FoodItemBtnTag;
import com.darewrorestaurants.Models.Order;
import com.darewrorestaurants.Models.OrderFoodItem;
import com.darewrorestaurants.R;
import com.darewrorestaurants.Utilities.Helper;
import com.darewrorestaurants.Utilities.MySharedPreferences;
import com.darewrorestaurants.Utilities.WebService;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jaffar on 2018-02-14.
 */

public class RestaurantFoodFragment extends Fragment {

    WebService webService;
    LinearLayout linlaHeaderProgress;
    RelativeLayout contant;
    Activity activity;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2;
    private Dialog dialogAddFoodCategory,dialogAddFoodItem;

    private ExpandableListView lvFoodItems;
    private TextView tvRestroName,tvRestroDesc;
    private ArrayList<FoodCategory> foodCategories;
    private FoodItemCustomAdapter foodItemCustomAdapter;
    private ImageView refreshImageView,logo;
    public RestaurantFoodFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_restaurant_food, container, false);
        init(rootView);
        loadData();
        setRestaurantData();
        return rootView;
    }

    private void init(View rootView){
        tvRestroName = (TextView)rootView.findViewById(R.id.tvRestroName);
        tvRestroDesc = (TextView)rootView.findViewById(R.id.tvRestroDesc);
        lvFoodItems = (ExpandableListView)rootView.findViewById(R.id.lvFoodItems);
        materialDesignFAM = (FloatingActionMenu) rootView.findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton)rootView.findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton)rootView.findViewById(R.id.material_design_floating_action_menu_item2);
        logo = (ImageView)rootView.findViewById(R.id.logo);
        linlaHeaderProgress = (LinearLayout)rootView. findViewById(R.id.progress);
        contant = (RelativeLayout)rootView.findViewById(R.id.contant);
    }

    private void setRestaurantData(){
        tvRestroName.setText("Burger King");
        tvRestroDesc.setText("University Road, Peshawar");
        materialDesignFAM.setClosedOnTouchOutside(true);
        int colorThatYouWant = activity.getResources().getColor(R.color.ShipGray);
        materialDesignFAM.getMenuIconView().setColorFilter(colorThatYouWant);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                materialDesignFAM.close(true);
                AddFoodCategory();
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                materialDesignFAM.close(true);
                AddFoodItem();
            }
        });
    }
    private void loadData(){
        contant.setVisibility(View.GONE);
        if (webService.isNetworkConnected()){
            if(((AppBaseActivity)activity).isSnackBarVisible()) {
                ((AppBaseActivity)activity).setSnackBarGone();
            }
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            getFoodItems();
        }
        else {
            ((AppBaseActivity)activity).setSnackBarVisible();
        }
    }

    private void getFoodItems(){
        MySharedPreferences mySharedPreferences = new MySharedPreferences();
        webService.getFoodItemsRequest(activity.getResources().getString(R.string.url) + "get_restaurant_food_menu",
                String.valueOf(mySharedPreferences.getRestaurantID(activity)), new WebService.VolleyResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean exist = jsonObject.getBoolean("exist");
                            if(exist){
                                foodCategories = new ArrayList<>();
                                JSONArray foodDataJSONArray = jsonObject.getJSONArray("restaurant_food_categories");
                                for(int fd= 0; fd<foodDataJSONArray.length();fd++){
                                    JSONObject foodCategoryJSONObject= foodDataJSONArray.getJSONObject(fd);
                                    FoodCategory foodCategory = new FoodCategory();
                                    foodCategory.setId(foodCategoryJSONObject.getInt("restaurant_food_category_id"));
                                    foodCategory.setCategory(foodCategoryJSONObject.getString("restaurant_food_category"));
                                    foodCategory.setFoodItems(new ArrayList<FoodItem>());
                                    JSONArray foodMenuJSONArray = foodCategoryJSONObject.getJSONArray("restaurant_food_menus");
                                    for(int fm =0; fm<foodMenuJSONArray.length();fm++){
                                        JSONObject foodMenuJSONObject = foodMenuJSONArray.getJSONObject(fm);
                                        FoodItem foodItem = new FoodItem();
                                        foodItem.setId(foodMenuJSONObject.getInt("restaurant_food_menu_id"));
                                        foodItem.setName(foodMenuJSONObject.getString("restaurant_food_name"));
                                        foodItem.setWeight(foodMenuJSONObject.getString("restaurant_food_quantity"));
                                        foodItem.setPrice(foodMenuJSONObject.getString("restaurant_food_price"));
                                        foodItem.setDesc(foodMenuJSONObject.getString("restaurant_food_description"));

                                        foodCategory.getFoodItems().add(foodItem);
                                    }
                                    foodCategories.add(foodCategory);
                                }
                                setFoodItems();
                                linlaHeaderProgress.setVisibility(View.GONE);
                            }
                            else {
                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void setFoodItems(){
        foodItemCustomAdapter = new FoodItemCustomAdapter(activity, foodCategories, new FoodItemCustomAdapter.AddImageViewClick() {
            @Override
            public void onAddImageViewClick(final FoodItemBtnTag tag) {
                dialogAddFoodItem= new Dialog(activity,R.style.MyAlertDialogStyle);
                dialogAddFoodItem.setContentView(R.layout.dialog_add_food_item);
                dialogAddFoodItem.setTitle("Add Food Category");
                final LinearLayout progress = (LinearLayout) dialogAddFoodItem.findViewById(R.id.progress);
                final Spinner spFoodCategory = (Spinner)dialogAddFoodItem.findViewById(R.id.spFoodCategory);
                spFoodCategory.setVisibility(View.GONE);
                final EditText etFoodName = (EditText)dialogAddFoodItem.findViewById(R.id.etFoodName);
                final EditText etFoodPrice = (EditText)dialogAddFoodItem.findViewById(R.id.etFoodPrice);
                final EditText etFoodWeight = (EditText)dialogAddFoodItem.findViewById(R.id.etFoodWeight);
                final EditText etFoodDesc = (EditText)dialogAddFoodItem.findViewById(R.id.etFoodDesc);
                final Button btnCancel = (Button)dialogAddFoodItem.findViewById(R.id.btnCancel);
                final Button btnSave = (Button)dialogAddFoodItem.findViewById(R.id.btnSave);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogAddFoodItem.dismiss();
                    }
                });
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Helper helper = new Helper();
                        if(helper.isEditTextEmpty(etFoodName)||helper.isEditTextEmpty(etFoodPrice)){
                            Toast.makeText(activity, "Required fileds missing", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            FoodItem fi = new FoodItem();
                            fi.setName(etFoodName.getText().toString());
                            fi.setPrice(etFoodPrice.getText().toString());
                            fi.setWeight(etFoodWeight.getText().toString());
                            fi.setDesc(etFoodDesc.getText().toString());
                            if (webService.isNetworkConnected()) {
                                if (((AppBaseActivity) activity).isSnackBarVisible()) {
                                    ((AppBaseActivity) activity).setSnackBarGone();
                                }
                                progress.setVisibility(View.VISIBLE);
                                MySharedPreferences mySharedPreferences = new MySharedPreferences();
                                webService.addRestaurantFoodItem(activity.getResources().getString(R.string.url) + "add_food_menu",
                                        String.valueOf(mySharedPreferences.getRestaurantID(activity)), String.valueOf(foodCategories.get(tag.getParent()).getId()),
                                        fi, new WebService.VolleyResponseListener() {
                                            @Override
                                            public void onSuccess(String response) {
                                                try {
                                                    progress.setVisibility(View.GONE);
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    boolean success = jsonObject.getBoolean("success");
                                                    if (success) {
                                                        Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                        dialogAddFoodItem.dismiss();
                                                    } else {
                                                        Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                                                    progress.setVisibility(View.GONE);
                                                }
                                            }

                                            @Override
                                            public void onError(VolleyError error) {
                                                Helper helper = new Helper();
                                                helper.volleyErrorMessage(activity, error);
                                                progress.setVisibility(View.GONE);
                                            }
                                        });
                            } else {
                                ((AppBaseActivity) activity).setSnackBarVisible();
                            }
                        }
                    }
                });
                dialogAddFoodItem.show();
            }
        }, new FoodItemCustomAdapter.EditImageViewClick() {
            @Override
            public void onEditImageViewClick(FoodItemBtnTag tag) {
                EditFoodItem(tag);
            }
        }, new FoodItemCustomAdapter.DeleteImageViewClick() {
            @Override
            public void onDeleteImageViewClick(FoodItemBtnTag tag) {
                Toast.makeText(activity, "Delete", Toast.LENGTH_SHORT).show();
            }
        });
        lvFoodItems.setAdapter(foodItemCustomAdapter);
        contant.setVisibility(View.VISIBLE);
    }
    public void AddFoodCategory(){
        dialogAddFoodCategory= new Dialog(activity,R.style.MyAlertDialogStyle);
        dialogAddFoodCategory.setContentView(R.layout.dialog_add_food_category);
        dialogAddFoodCategory.setTitle("Add Food Category");
        final LinearLayout progress = (LinearLayout)dialogAddFoodCategory.findViewById(R.id.progress);
        final EditText etFoodCategory = (EditText)dialogAddFoodCategory.findViewById(R.id.etFoodCategory);
        final Button btnCancel = (Button)dialogAddFoodCategory.findViewById(R.id.btnCancel);
        final Button btnSave = (Button)dialogAddFoodCategory.findViewById(R.id.btnSave);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddFoodCategory.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etFoodCategory.getText().toString()!=""&&
                        !etFoodCategory.getText().toString().matches("")&&
                        etFoodCategory.getText().toString().length()!=0) {
                    if (webService.isNetworkConnected()) {
                        if (((AppBaseActivity) activity).isSnackBarVisible()) {
                            ((AppBaseActivity) activity).setSnackBarGone();
                        }
                        progress.setVisibility(View.VISIBLE);
                        MySharedPreferences mySharedPreferences = new MySharedPreferences();
                        Toast.makeText(activity, String.valueOf(mySharedPreferences.getRestaurantID(activity)), Toast.LENGTH_SHORT).show();
                        webService.addRestaurantFoodCategory(activity.getResources().getString(R.string.url) + "add_food_category",
                                String.valueOf(mySharedPreferences.getRestaurantID(activity)), etFoodCategory.getText().toString(),
                                 new WebService.VolleyResponseListener() {
                                    @Override
                                    public void onSuccess(String response) {
                                        try {
                                            progress.setVisibility(View.GONE);
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if (success) {
                                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                dialogAddFoodCategory.dismiss();
                                            } else {
                                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                                            progress.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onError(VolleyError error) {
                                        Helper helper = new Helper();
                                        helper.volleyErrorMessage(activity, error);
                                        progress.setVisibility(View.GONE);
                                    }
                                });
                    } else {
                        ((AppBaseActivity) activity).setSnackBarVisible();
                    }
                }
                else {
                    ;
                }

            }
        });
        dialogAddFoodCategory.show();
    }

    public void AddFoodItem(){
        dialogAddFoodItem= new Dialog(activity,R.style.MyAlertDialogStyle);
        dialogAddFoodItem.setContentView(R.layout.dialog_add_food_item);
        dialogAddFoodItem.setTitle("Add Food Category");
        final LinearLayout progress = (LinearLayout) dialogAddFoodItem.findViewById(R.id.progress);
        final Spinner spFoodCategory = (Spinner)dialogAddFoodItem.findViewById(R.id.spFoodCategory);
        ArrayAdapter<String> foodCategoriesAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }

        };
        foodCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for(FoodCategory fc:foodCategories){
            foodCategoriesAdapter.add(fc.getCategory());
        }
        foodCategoriesAdapter.add("Restaurant Food Category");
        spFoodCategory.setAdapter(foodCategoriesAdapter);
        spFoodCategory.setSelection(foodCategoriesAdapter.getCount());
        final EditText etFoodName = (EditText)dialogAddFoodItem.findViewById(R.id.etFoodName);
        final EditText etFoodPrice = (EditText)dialogAddFoodItem.findViewById(R.id.etFoodPrice);
        final EditText etFoodWeight = (EditText)dialogAddFoodItem.findViewById(R.id.etFoodWeight);
        final EditText etFoodDesc = (EditText)dialogAddFoodItem.findViewById(R.id.etFoodDesc);
        final Button btnCancel = (Button)dialogAddFoodItem.findViewById(R.id.btnCancel);
        final Button btnSave = (Button)dialogAddFoodItem.findViewById(R.id.btnSave);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddFoodItem.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper helper = new Helper();
                if(helper.isEditTextEmpty(etFoodName)||helper.isEditTextEmpty(etFoodPrice)){
                    Toast.makeText(activity, "Required fileds missing", Toast.LENGTH_SHORT).show();
                }
                else {
                    FoodItem fi = new FoodItem();
                    fi.setName(etFoodName.getText().toString());
                    fi.setPrice(etFoodPrice.getText().toString());
                    fi.setWeight(etFoodWeight.getText().toString());
                    fi.setDesc(etFoodDesc.getText().toString());
                    if (webService.isNetworkConnected()) {
                        if (((AppBaseActivity) activity).isSnackBarVisible()) {
                            ((AppBaseActivity) activity).setSnackBarGone();
                        }
                        progress.setVisibility(View.VISIBLE);
                        MySharedPreferences mySharedPreferences = new MySharedPreferences();
                        webService.addRestaurantFoodItem(activity.getResources().getString(R.string.url) + "add_food_menu",
                                String.valueOf(mySharedPreferences.getRestaurantID(activity)), String.valueOf(foodCategories.get(spFoodCategory.getSelectedItemPosition()).getId()),
                                fi, new WebService.VolleyResponseListener() {
                                    @Override
                                    public void onSuccess(String response) {
                                        try {
                                            progress.setVisibility(View.GONE);
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if (success) {
                                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                dialogAddFoodItem.dismiss();
                                            } else {
                                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                                            progress.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onError(VolleyError error) {
                                        Helper helper = new Helper();
                                        helper.volleyErrorMessage(activity, error);
                                        progress.setVisibility(View.GONE);
                                    }
                                });
                    } else {
                        ((AppBaseActivity) activity).setSnackBarVisible();
                    }
                }
            }
        });
        dialogAddFoodItem.show();
    }

    public void EditFoodItem(final FoodItemBtnTag tag){
        dialogAddFoodItem= new Dialog(activity,R.style.MyAlertDialogStyle);
        dialogAddFoodItem.setContentView(R.layout.dialog_add_food_item);
        dialogAddFoodItem.setTitle("Add Food Category");
        final LinearLayout progress = (LinearLayout) dialogAddFoodItem.findViewById(R.id.progress);
        final Spinner spFoodCategory = (Spinner)dialogAddFoodItem.findViewById(R.id.spFoodCategory);
        final ArrayAdapter<String> foodCategoriesAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }

        };
        foodCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for(FoodCategory fc:foodCategories){
            foodCategoriesAdapter.add(fc.getCategory());
        }
        foodCategoriesAdapter.add("Restaurant Food Category");
        spFoodCategory.setAdapter(foodCategoriesAdapter);
        spFoodCategory.setSelection(tag.getParent());
        final EditText etFoodName = (EditText)dialogAddFoodItem.findViewById(R.id.etFoodName);
        final EditText etFoodPrice = (EditText)dialogAddFoodItem.findViewById(R.id.etFoodPrice);
        final EditText etFoodWeight = (EditText)dialogAddFoodItem.findViewById(R.id.etFoodWeight);
        final EditText etFoodDesc = (EditText)dialogAddFoodItem.findViewById(R.id.etFoodDesc);
        FoodItem foodItem = foodCategories.get(tag.getParent()).getFoodItems().get(tag.getChild());
        etFoodName.setText(foodItem.getName());
        etFoodPrice.setText(foodItem.getPrice());
        etFoodWeight.setText(foodItem.getWeight());
        etFoodDesc.setText(foodItem.getDesc());
        final Button btnCancel = (Button)dialogAddFoodItem.findViewById(R.id.btnCancel);
        final Button btnSave = (Button)dialogAddFoodItem.findViewById(R.id.btnSave);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddFoodItem.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodItem fi = new FoodItem();
                fi.setId(foodCategories.get(tag.getParent()).getFoodItems().get(tag.getChild()).getId());
                fi.setName(etFoodName.getText().toString());
                fi.setPrice(etFoodPrice.getText().toString());
                fi.setWeight(etFoodWeight.getText().toString());
                fi.setDesc(etFoodDesc.getText().toString());
                if (webService.isNetworkConnected()){
                    if(((AppBaseActivity)activity).isSnackBarVisible()) {
                        ((AppBaseActivity)activity).setSnackBarGone();
                    }
                    progress.setVisibility(View.VISIBLE);
                    MySharedPreferences mySharedPreferences = new MySharedPreferences();
                    webService.updateRestaurantFoodItem(activity.getResources().getString(R.string.url) + "update_restaurant_food_data",
                            String.valueOf(mySharedPreferences.getRestaurantID(activity)), String.valueOf(foodCategories.get(spFoodCategory.getSelectedItemPosition()).getId()),
                            fi, new WebService.VolleyResponseListener() {
                                @Override
                                public void onSuccess(String response) {
                                    try {
                                        progress.setVisibility(View.GONE);
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        if(success){
                                            Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                            dialogAddFoodItem.dismiss();
                                        }
                                        else {
                                            Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
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
        dialogAddFoodItem.show();
    }

    private void editFoodItem(int foodCatID, FoodItem fi){

    }
}
