package com.education.xeal.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.education.xeal.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author by Mohit Arora on 8/5/18.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context ctx;
    private List<String> listGetNotification;

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvNotification)
        TextView tvNotification;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public NotificationAdapter(Context ctx, List<String> listGetNotification) {
        this.ctx = ctx;
        this.listGetNotification = listGetNotification;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvNotification.setText(listGetNotification.get(position));
    }

    @Override
    public int getItemCount() {
        return listGetNotification.size();
    }
}