package com.darewrorestaurants.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.darewrorestaurants.Models.FoodCategory;
import com.darewrorestaurants.Models.FoodItem;
import com.darewrorestaurants.Models.FoodItemBtnTag;
import com.darewrorestaurants.R;

import java.util.ArrayList;

/**
 * Created by Jaffar on 9/27/2017.
 */
public class FoodItemCustomAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private ArrayList<FoodCategory> foodCategories;
    private AddImageViewClick addImageViewClick = null;
    private EditImageViewClick editImageViewClick = null;
    private DeleteImageViewClick deleteImageViewClick = null;
    public FoodItemCustomAdapter(Context context, ArrayList<FoodCategory> foodCategories, AddImageViewClick listener2,EditImageViewClick listener,
                                 DeleteImageViewClick listener1) {
        this._context = context;
        this.foodCategories = foodCategories;
        addImageViewClick = listener2;
        editImageViewClick = listener;
        deleteImageViewClick = listener1;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.foodCategories.get(groupPosition).getFoodItems().get(childPosition).getName();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        int strid = this.foodCategories.get(groupPosition).getFoodItems().get(childPosition).getId();
        long id = Long.valueOf(strid);
        return id;
    }
    public class ViewHolder {
        TextView name;
        TextView desc;
        TextView price;
        TextView weight;
        ImageView editImageView,deleteImageView,addImageView;
    }
    public String getChildName(int groupPosition, int childPosition) {
        return this.foodCategories.get(groupPosition).getFoodItems().get(childPosition).getName();
    }
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final FoodCategory foodCategory = this.foodCategories.get(groupPosition);
        final FoodItem foodItem = foodCategory.getFoodItems().get(childPosition);
//        Toast.makeText(this._context, foodItem.getName(), Toast.LENGTH_SHORT).show();
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adapter_food_item_list, null);

            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.weight = (TextView) convertView.findViewById(R.id.weight);
            holder.desc = (TextView) convertView.findViewById(R.id.desc);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.editImageView = (ImageView) convertView.findViewById(R.id.editImageView);
            holder.deleteImageView = (ImageView) convertView.findViewById(R.id.deleteImageView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


        // Set the results into TextViews
        holder.name.setText(foodItem.getName());
        if(foodItem.getDesc() == null || foodItem.getDesc().isEmpty() || foodItem.getDesc().length() == 0
                || foodItem.getDesc().equals("") || foodItem.getDesc().matches("")){
            holder.desc.setVisibility(View.GONE);
        }
        else{
            holder.desc.setVisibility(View.VISIBLE);
            holder.desc.setText(foodItem.getDesc());
        }
        if(foodItem.getWeight() == null || foodItem.getWeight().isEmpty() || foodItem.getWeight().length() == 0
                || foodItem.getWeight().equals("") || foodItem.getWeight().matches("")){
            holder.weight.setVisibility(View.GONE);
        }
        else{
            holder.weight.setVisibility(View.VISIBLE);
            holder.weight.setText(foodItem.getWeight());
        }
        holder.price.setText(foodItem.getPrice());

        FoodItemBtnTag tag = new FoodItemBtnTag(groupPosition,childPosition);
        holder.editImageView.setTag(tag); //For passing the list item index
        holder.deleteImageView.setTag(tag); //For passing the list item index

        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editImageViewClick != null){
                    editImageViewClick.onEditImageViewClick((FoodItemBtnTag)holder.editImageView.getTag());
                }
            }
        });
        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteImageViewClick != null){
                    deleteImageViewClick.onDeleteImageViewClick((FoodItemBtnTag)holder.deleteImageView.getTag());
                }
            }
        });
        return convertView;
    }
    public interface EditImageViewClick{
        public abstract void onEditImageViewClick(FoodItemBtnTag tag);
    }
    public interface DeleteImageViewClick{
        public abstract void onDeleteImageViewClick(FoodItemBtnTag tag);
    }
    public interface AddImageViewClick{
        public abstract void onAddImageViewClick(FoodItemBtnTag tag);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size=0;
        if(this.foodCategories.get(groupPosition).getFoodItems()!=null)
            size = this.foodCategories.get(groupPosition).getFoodItems().size();
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.foodCategories.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.foodCategories.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        final FoodCategory menuCategory = this.foodCategories.get(groupPosition);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adapter_food_category, null);
            holder.addImageView = (ImageView)convertView.findViewById(R.id.addImageView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Get  menu_category_row.xml file element and set value
        ((TextView) convertView.findViewById(R.id.tvfc)).setText(menuCategory.getCategory());
        FoodItemBtnTag tag = new FoodItemBtnTag();
        tag.setParent(groupPosition);
        holder.addImageView.setTag(tag); //For passing the list item index
        holder.addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addImageViewClick != null){
                    addImageViewClick.onAddImageViewClick((FoodItemBtnTag)holder.addImageView.getTag());
                }
            }
        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void notifyDataSetChanged()
    {
        // Refresh List rows
        super.notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty()
    {
        return ((this.foodCategories == null) || this.foodCategories.isEmpty());
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }
}

