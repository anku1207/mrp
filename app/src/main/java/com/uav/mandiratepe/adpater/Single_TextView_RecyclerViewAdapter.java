package com.uav.mandiratepe.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.uav.mandiratepe.R;
import com.uav.mandiratepe.vo.DataAdapterVO;

import java.util.List;

public class Single_TextView_RecyclerViewAdapter extends  RecyclerView.Adapter<Single_TextView_RecyclerViewAdapter.ProdectViewHolder>{
        Context mctx ;
        List<DataAdapterVO> productslist;
        int Activityname;



        public Single_TextView_RecyclerViewAdapter(Context mctx, List<DataAdapterVO> productslist, int Activityname) {
        this.mctx = mctx;
        this.productslist = productslist;
        this.Activityname=Activityname;
        }

        @Override
        public ProdectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater= LayoutInflater.from(mctx);
          View view=layoutInflater.inflate(Activityname,null);
       /* ProdectViewHolder holder=new ProdectViewHolder(view);
        return holder;*/
        return new ProdectViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProdectViewHolder holder, int position) {

        final DataAdapterVO pro=productslist.get(position);

        if(pro.getText()!="new"){
            holder.textView.setText(pro.getText());



        }else {

            Toast.makeText(mctx, "sfsfds", Toast.LENGTH_SHORT).show();
            holder.image.setVisibility(View.VISIBLE);
            holder.textView.setVisibility(View.GONE);

        }




        }

        @Override
        public int getItemCount() {
                return productslist.size();
                }
        public  class ProdectViewHolder extends RecyclerView.ViewHolder {

            TextView textView;
            LinearLayout linearLayout;
            ImageView image;
            public ProdectViewHolder(View itemView) {
                super(itemView);
                textView=itemView.findViewById(R.id.textview);
                linearLayout=itemView.findViewById(R.id.linearLayout);
                image=itemView.findViewById(R.id.image);

            }
        }
}
