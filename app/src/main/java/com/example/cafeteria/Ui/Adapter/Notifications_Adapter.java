package com.example.cafeteria.Ui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.cafeteria.Model.CategoriesModel;
import com.example.cafeteria.Model.NotificatiosModel;
import com.example.cafeteria.R;
import com.example.cafeteria.Ui.Activity.ItemsByCategoryId;
import com.example.cafeteria.Ui.Activity.MainActivity;
import com.example.cafeteria.Ui.Fragment.HomeFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class Notifications_Adapter extends RecyclerView.Adapter<Notifications_Adapter.ViewHolder> {

    private Context context;
    private List<NotificatiosModel> list;

    public Notifications_Adapter(List<NotificatiosModel> notificatiosModels, Context context) {
        this.context = context;
        this.list = notificatiosModels;
    }

    @NonNull
    @Override
    public Notifications_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_notifcations, parent, false);
        return new Notifications_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Notifications_Adapter.ViewHolder holder, int position) {
        holder.Tv_Title.setText(list.get(position).getName());
        holder.Tv_Notification.setText(list.get(position).getNotification());
        holder.Tv_notification_date.setText(list.get(position).getAdded_at());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView Tv_Title,Tv_Notification,Tv_notification_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Tv_Title = itemView.findViewById(R.id.title);
            Tv_Notification = itemView.findViewById(R.id.notification);
            Tv_notification_date = itemView.findViewById(R.id.notification_date);
        }
    }
}




