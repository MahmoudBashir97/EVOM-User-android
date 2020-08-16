package com.mahmoud.bashir.evom_user_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmoud.bashir.evom_user_app.R;
import com.mahmoud.bashir.evom_user_app.pojo.driver_Info_Model;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class available_drivers_adpt extends RecyclerView.Adapter<available_drivers_adpt.ViewHolder> {

    Context context;
    List<driver_Info_Model> mlist;
    RequestOnClickInterface requestOnClickInterface;

    public available_drivers_adpt(Context context, List<driver_Info_Model> mlist,RequestOnClickInterface requestOnClickInterface) {
        this.context = context;
        this.mlist = mlist;
        this.requestOnClickInterface = requestOnClickInterface;
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

        holder.txt_driver_name.setText(m.getDriver_name());
        Picasso.get().load(m.getDriver_img()).into(holder.driver_img);
        holder.request_btn.setOnClickListener(view -> {


            requestOnClickInterface.OnClick(position,
                    m.getDriver_name(),
                    m.getDriver_img(),
                    m.getDriver_ph(),
                    m.getDriver_token(),
                    m.getId(),
                    m.getCar_Number(),
                    m.getCar_Model(),
                    m.getDriver_lat(),
                    m.getDriver_lng());
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView driver_img;
        TextView txt_distance,txt_driver_name;
        Button request_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            driver_img = itemView.findViewById(R.id.driver_img);
            txt_distance = itemView.findViewById(R.id.txt_distance);
            txt_driver_name = itemView.findViewById(R.id.txt_driver_name);
            request_btn = itemView.findViewById(R.id.request_btn);
        }
    }
}
