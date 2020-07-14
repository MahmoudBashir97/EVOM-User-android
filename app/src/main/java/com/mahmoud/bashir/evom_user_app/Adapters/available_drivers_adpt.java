package com.mahmoud.bashir.evom_user_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmoud.bashir.evom_user_app.R;
import com.mahmoud.bashir.evom_user_app.pojo.driver_Info_Model;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class available_drivers_adpt extends RecyclerView.Adapter<available_drivers_adpt.ViewHolder> {

    Context context;
    List<driver_Info_Model> mlist;

    public available_drivers_adpt(Context context, List<driver_Info_Model> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_avail_drivers,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        driver_Info_Model m = mlist.get(position);

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView driver_img;
        TextView txt_distance;
        Button request_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            driver_img = itemView.findViewById(R.id.driver_img);
            txt_distance = itemView.findViewById(R.id.txt_distance);
            request_btn = itemView.findViewById(R.id.request_btn);

        }
    }
}
