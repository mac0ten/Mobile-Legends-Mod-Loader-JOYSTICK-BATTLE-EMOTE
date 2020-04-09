package com.changer.modloader.ml.molo.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changer.modloader.ml.molo.Objects.ModItem;
import com.changer.modloader.ml.molo.R;
import com.changer.modloader.ml.molo.tool;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ModItemAdapter extends RecyclerView.Adapter<ModItemAdapter.ViewHolder> {

    ArrayList<ModItem> mods = new ArrayList<>();
    Context context;

    int target;

    public ModItemAdapter(ArrayList<ModItem> mods, Context context, int target) {
        this.mods = mods;
        this.context = context;
        this.target = target;
    }
//    1 = JOYSTICK
//    2 = ANIEMOJI
//    3 = TOURET WARNING
//    4 = TOURNAMENT
//    5 = LAUNCH GAME
//    6 = MATCH READY
//    7 = MATCH STARTED

    itemClickListener itemClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mod_item, parent, false);
        return new ViewHolder(view);
    }

    public interface itemClickListener {
        void onItemClickListner(int position, ViewHolder viewHolder, int target, View sourceClick);
    }

    public void setOnItemClickListener(itemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ModItem modItem = mods.get(position);
        if (position == mods.size() - 1) {
            final float scale = context.getResources().getDisplayMetrics().density;
            int pixel1 = (int) (160 * scale + 0.5f);
            int pixel2 = (int) (300 * scale + 0.5f);
            int pixel3 = (int) (2 * scale + 0.5f);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    pixel1,
                    pixel2
            );
            params.setMargins(0, 0, pixel3, 0);
            ((LinearLayout) holder.itemView).setLayoutParams(params);
        }
        if (modItem != null) {
            //IMAGE HANDLE
            if (modItem.getDrawableImage() <= 0) {
                tool.picChange(context, holder.img_mod, modItem.getMod_pic());
            } else holder.img_mod.setImageResource(modItem.getDrawableImage());

            holder.tv_modname.setText(modItem.getMod_name());

            //CHECK IF MOD ID IS USED
            Drawable iconVector = context.getResources().getDrawable(R.drawable.vector_check);
            Log.d("targelock", target + "");
//            if (tool.getSharedPref(context, modItem.getMod_id(),target) != null) {
//                iconVector = context.getResources().getDrawable(R.drawable.vector_close);
//            } else {
//                iconVector = context.getResources().getDrawable(R.drawable.vector_check);
//            }//BACKUP BELOW
            if (tool.getSharedPref(context, modItem.getMod_id(), target) != null) {
                iconVector = context.getResources().getDrawable(R.drawable.vector_close);
            } else {
                iconVector = context.getResources().getDrawable(R.drawable.vector_check);
            }

            holder.btn_trymod.setImageDrawable(iconVector);
            holder.btn_trymod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClickListner(position, holder, target, v);
                }
            });
            //END


        }
    }

    @Override
    public int getItemCount() {
        return mods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView img_mod;
        public TextView tv_modname;
        public FloatingActionButton btn_trymod;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            //this.setIsRecyclable(false);

            img_mod = itemView.findViewById(R.id.img_mod);
            tv_modname = itemView.findViewById(R.id.tv_modname);
            btn_trymod = itemView.findViewById(R.id.btn_trymod);
            itemView.setOnClickListener(this);
        }

        private void unSelectButtons() {

        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                //itemClickListener.onItemClickListner(getAdapterPosition(),itemView,target,v);
            }
        }
    }
}
