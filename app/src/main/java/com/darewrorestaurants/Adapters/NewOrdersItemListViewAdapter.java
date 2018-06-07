package com.darewrorestaurants.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.darewrorestaurants.Models.OrderFoodItem;
import com.darewrorestaurants.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaffar on 2018-02-14.
 */

public class NewOrdersItemListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<OrderFoodItem> orderFoodItems = null;
    private ArrayList<OrderFoodItem> arraylist;
    public NewOrdersItemListViewAdapter(Context context,
                                    List<OrderFoodItem> orderFoodItems) {
        mContext = context;
        this.orderFoodItems = orderFoodItems;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<OrderFoodItem>();
        this.arraylist.addAll(orderFoodItems);
    }

    public class ViewHolder {
        TextView name;
        TextView weight;
        TextView price;
        TextView quantity;
        TextView total;
    }

    @Override
    public int getCount() {
        return orderFoodItems.size();
    }

    @Override
    public Object getItem(int position) {
        return orderFoodItems.get(position);    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_new_orders_item, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.weight = (TextView) view.findViewById(R.id.weight);
            holder.price = (TextView) view.findViewById(R.id.price);
            holder.quantity = (TextView) view.findViewById(R.id.quantity);
            holder.total = (TextView) view.findViewById(R.id.total);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews
        holder.name.setText(orderFoodItems.get(position).getName());
        holder.price.setText(orderFoodItems.get(position).getPrice());
        holder.quantity.setText(orderFoodItems.get(position).getQuantity());
        int t = Integer.valueOf(orderFoodItems.get(position).getPrice())*Integer.valueOf(orderFoodItems.get(position).getQuantity());
        holder.total.setText(""+t);
        if(orderFoodItems.get(position).getWeight()!=null
                &&!orderFoodItems.get(position).getWeight().matches("")) {
            holder.weight.setText(orderFoodItems.get(position).getWeight());
        }
        else {
            holder.weight.setText("");
        }

        return view;
    }
}
