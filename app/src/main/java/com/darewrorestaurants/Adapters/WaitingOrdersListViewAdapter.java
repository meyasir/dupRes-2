package com.darewrorestaurants.Adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.darewrorestaurants.Models.Order;
import com.darewrorestaurants.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jaffar on 2018-02-14.
 */

public class WaitingOrdersListViewAdapter extends ArrayAdapter<Order> {

    private LayoutInflater lf;
    private List<ViewHolder> lstHolders;
    private List<Order> orders;
    private Handler mHandler = new Handler();
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (lstHolders) {
                long currentTime = System.currentTimeMillis();
                for (ViewHolder holder : lstHolders) {
                    holder.updateTimeRemaining(currentTime);
                }
            }
        }
    };
    private OnViewClick onViewClick = null;
    public WaitingOrdersListViewAdapter(Context context,
                                    List<Order> orders, OnViewClick listener) {
        super(context, 0, orders);
        this.orders = orders;
        lf = LayoutInflater.from(context);
        lstHolders = new ArrayList<>();
        startUpdateTimer();
        onViewClick = listener;
    }
    private void startUpdateTimer() {
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView  = lf.inflate(R.layout.adapter_waiting_orders, null);
            // Locate the TextViews in listview_item.xml
            holder.customerName = (TextView) convertView.findViewById(R.id.customerName);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.orderid = (TextView) convertView.findViewById(R.id.orderid);
            holder.orderdate = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
            synchronized (lstHolders) {
                lstHolders.add(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setData(getItem(position));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onViewClick.onViewClick(position, orders.get(position).getId(),orders.get(position).getCustomerName());
            }
        });
        return convertView;
    }

    public interface OnViewClick{
        public abstract void onViewClick(int position, int id, String name);
    }

    private class ViewHolder {
        TextView customerName;
        TextView tvTime;
        TextView status;
        Order mOrder;
        TextView orderid;
        TextView orderdate;

        public void setData(Order item) {
            mOrder = item;
            customerName.setText(item.getCustomerName());
            orderid.setText("#"+item.getId());
            String date[]  = item.getDate().split(" ");
            String time = date[1];
            DateFormat f1 = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
            Date d = null;
            try {
                d = f1.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat f2 = new SimpleDateFormat("h:mma");
            time = f2.format(d).toLowerCase(); // "12:18am"
            date = date[0].split("-");
            String month="";
            if(date[1].matches("01"))
                month="Jan";
            else if(date[1].matches("02"))
                month="Feb";
            else if(date[1].matches("03"))
                month="Mar";
            else if(date[1].matches("04"))
                month="Apr";
            else if(date[1].matches("05"))
                month="May";
            else if(date[1].matches("06"))
                month="June";
            else if(date[1].matches("07"))
                month="July";
            else if(date[1].matches("08"))
                month="Aug";
            else if(date[1].matches("09"))
                month="Sep";
            else if(date[1].matches("10"))
                month="Oct";
            else if(date[1].matches("11"))
                month="Nov";
            else if(date[1].matches("12"))
                month="Dec";
            String day = date[2];

            orderdate.setText(day+" "+month+", "+time);
            status.setText(item.getStatus());
            updateTimeRemaining(System.currentTimeMillis());

        }

        public void updateTimeRemaining(long currentTime) {
            long timeDiff = mOrder.getReadyTime()- currentTime;
            if (timeDiff > 0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                String h,m,s;
                h = String.valueOf(hours);
                m = String.valueOf(minutes);
                s = String.valueOf(seconds);
                if(h.length()==1)
                    h="0"+h;
                if(m.length()==1)
                    m="0"+m;
                if(s.length()==1)
                    s="0"+s;
                tvTime.setText(h + ":" + m + ":" + s);
            } else {
                tvTime.setText("Time Up");
            }
        }
    }
}
