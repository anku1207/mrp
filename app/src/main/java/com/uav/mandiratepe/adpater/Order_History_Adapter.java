package com.uav.mandiratepe.adpater;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uav.mandiratepe.R;
import com.uav.mandiratepe.activity.Order_Summary;
import com.uav.mandiratepe.vo.OrderVO;

import java.util.List;

public class Order_History_Adapter extends  RecyclerView.Adapter<Order_History_Adapter.ProdectViewHolder>{
    Context mctx ;
    List<OrderVO> productslist;
    int Activityname;

    Activity activity ;



    public Order_History_Adapter(Context mctx, List<OrderVO> productslist, int Activityname) {
        this.mctx = mctx;
        this.productslist = productslist;
        this.Activityname=Activityname;
        activity =(Activity) mctx;
    }

    @Override
    public Order_History_Adapter.ProdectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mctx).inflate(Activityname, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new ProdectViewHolder(rootView);

    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(Order_History_Adapter.ProdectViewHolder holder, int position) {

        final OrderVO pro=productslist.get(position);

        holder.amount.setText(""+pro.getGrossTotal().intValue());
        holder.deliveryamount.setText(""+pro.getDeliveryCharges().intValue());
        holder.totalamount.setText(""+pro.getNetAmount().intValue());
        holder.totalunit.setText(""+pro.getTotalItem().intValue());
        holder.totalweight.setText(""+pro.getTotalWeight());
        holder.orderstatus.setText(pro.getStatusName());



        holder.orderstatus.setTextColor(Color.parseColor(pro.getColorCode()));



        holder.detail.setTag(pro.getOrderId());
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("orderId",view.getTag().toString());
                Intent intent =new Intent(activity, Order_Summary.class);
                intent.putExtra("orderid",view.getTag().toString());
                activity.startActivityForResult(intent,201);
                activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }
        });

        if(pro.getDesc()!=null){
            holder.detail.setVisibility(View.GONE);
        }else {
            holder.detail.setVisibility(View.VISIBLE);
        }




    }

    @Override
    public int getItemCount() {
        return productslist.size();
    }
    public  class ProdectViewHolder extends RecyclerView.ViewHolder {

        TextView amount,deliveryamount,totalamount,totalunit,totalweight,detail ,orderstatus;

        public ProdectViewHolder(View itemView) {
            super(itemView);
            amount=itemView.findViewById(R.id.amount);
            deliveryamount=itemView.findViewById(R.id.deliveryamount);
            totalamount=itemView.findViewById(R.id.totalamount);
            totalunit=itemView.findViewById(R.id.totalunit);
            totalweight=itemView.findViewById(R.id.totalweight);
            detail=itemView.findViewById(R.id.detail);
            orderstatus=itemView.findViewById(R.id.orderstatus);


        }
    }
}
