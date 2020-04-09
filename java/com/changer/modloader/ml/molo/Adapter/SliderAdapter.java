package com.changer.modloader.ml.molo.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.changer.modloader.ml.molo.Objects.ModItem;
import com.changer.modloader.ml.molo.R;
import com.changer.modloader.ml.molo.tool;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.ViewHolder> {

    Context context;
    ArrayList<ModItem> mods = new ArrayList<>();

    public SliderAdapter(Context context, ArrayList<ModItem> mods) {
        this.context = context;
        this.mods = mods;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageslider_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ModItem mod = mods.get(position);
        if(mod!=null){
            Log.e("sliderimg",mod.getDrawableImage()+"");
            if (mod.getDrawableImage() <= 0) {
                tool.picChange(context, viewHolder.image_item, mod.getMod_pic());
            } else viewHolder.image_item.setImageResource(mod.getDrawableImage());

        }
    }

    @Override
    public int getCount() {
        return mods.size();
    }

    class ViewHolder extends SliderViewAdapter.ViewHolder {
        ImageView image_item;
        public ViewHolder(View itemView) {
            super(itemView);
            image_item=itemView.findViewById(R.id.sliderimage_item);
        }
    }
}
