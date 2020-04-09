package com.changer.modloader.ml.molo.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.changer.modloader.ml.molo.Objects.DashboardItem;
import com.changer.modloader.ml.molo.R;
import com.changer.modloader.ml.molo.tool;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    ArrayList<DashboardItem> dashboardItems = new ArrayList<>();
    private Context context;

    OnItemClickListener itemClickListener;

    public DashboardAdapter(Context context, ArrayList<DashboardItem> dashboardItems) {
        this.dashboardItems = dashboardItems;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DashboardItem dashboardItem = dashboardItems.get(position);
        holder.tv_dashboardname.setText(dashboardItem.getDashboard_name());
        if (dashboardItem.getDashboard_img().length() > 1) {
            tool.picChange(context, holder.img_dashboard, dashboardItem.getDashboard_img());
        } else {
            Drawable drawable =context.getResources().getDrawable(R.drawable.empty);
            if(dashboardItem.getDashboard_drawable_img()>0) {
                drawable = context.getResources().getDrawable(dashboardItem.getDashboard_drawable_img());
            }
            holder.img_dashboard.setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return dashboardItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_dashboardname;
        ImageView img_dashboard;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tv_dashboardname = itemView.findViewById(R.id.tv_dashboardname);
            img_dashboard = itemView.findViewById(R.id.img_dashboard);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
