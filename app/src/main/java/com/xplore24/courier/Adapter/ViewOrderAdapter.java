package com.xplore24.courier.Adapter;


 import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.xplore24.courier.Model.ViewOrderModel;
import com.xplore24.courier.R;

import java.util.List;


public class ViewOrderAdapter extends RecyclerView.Adapter<ViewOrderAdapter.ViewHolder> {

    private Context context;
    private List<ViewOrderModel> viewOrderModels;

    public ViewOrderAdapter(Context context, List viewOrderModels) {
        this.context = context;
        this.viewOrderModels = viewOrderModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderdetailslayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(viewOrderModels.get(position));

        ViewOrderModel order = viewOrderModels.get(position);

        holder.orderId.setText(order.get_id());
        holder.phonenum.setText(order.getPhone());
        holder.address.setText(order.getAddress());
        holder.deliveryCharge.setText(order.getDeliveryCharge());
        holder.money.setText(order.getMoney());
        holder.status.setText(order.getStatus());


    }

    @Override
    public int getItemCount() {
        return viewOrderModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView status,orderId,phonenum,address,money,deliveryCharge;

        public ViewHolder(View itemView) {
            super(itemView);

            status = (TextView) itemView.findViewById(R.id.ordrstatus);
            phonenum = (TextView) itemView.findViewById(R.id.orderphone);
            address = (TextView) itemView.findViewById(R.id.orderaddress);
            orderId = (TextView) itemView.findViewById(R.id.ordrid);
            money = (TextView) itemView.findViewById(R.id.ordrcollectionmoney);
            deliveryCharge = (TextView) itemView.findViewById(R.id.ordercharge);




        }
    }

}