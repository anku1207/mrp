package com.uav.mandiratepe.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uav.mandiratepe.R;
import com.uav.mandiratepe.util.Utility;
import com.uav.mandiratepe.vo.ProductVO;

import java.util.List;

public class Order_Summary_Adapter extends  RecyclerView.Adapter<Order_Summary_Adapter.ProdectViewHolder>{
    Context mctx ;
    List<ProductVO> productslist;
    int Activityname;



    public Order_Summary_Adapter(Context mctx, List<ProductVO> productslist, int Activityname) {
        this.mctx = mctx;
        this.productslist = productslist;
        this.Activityname=Activityname;
    }

    @Override
    public Order_Summary_Adapter.ProdectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mctx).inflate(Activityname, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new Order_Summary_Adapter.ProdectViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(Order_Summary_Adapter.ProdectViewHolder holder, int position) {

        final ProductVO pro=productslist.get(position);

        holder.amount.setText(mctx.getString(R.string.Rs)+" " +pro.getTotal());
        holder.calculat.setText(""+pro.getDescription());
        holder.productname.setText(""+pro.getProductName());


        if(Utility.hindinameutf8(pro.getUtfCode())!= null && !Utility.hindinameutf8(pro.getUtfCode()).equals("")){

            holder.hindiname.setText(Utility.hindinameutf8(pro.getUtfCode()));
        }else{
            holder.hindiname.setText("");
        }




    }

    @Override
    public int getItemCount() {
        return productslist.size();
    }
    public  class ProdectViewHolder extends RecyclerView.ViewHolder {

        TextView productname,calculat,amount,hindiname;

        public ProdectViewHolder(View itemView) {
            super(itemView);
            amount=itemView.findViewById(R.id.amount);
            calculat=itemView.findViewById(R.id.calculat);
            productname=itemView.findViewById(R.id.productname);
            hindiname=itemView.findViewById(R.id.hindiname);


        }
    }
}
