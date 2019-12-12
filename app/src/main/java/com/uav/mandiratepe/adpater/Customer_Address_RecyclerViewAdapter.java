package com.uav.mandiratepe.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.uav.mandiratepe.R;
import com.uav.mandiratepe.activity.Check_Out;
import com.uav.mandiratepe.constant.ErrorConstant;
import com.uav.mandiratepe.vo.AreaVO;
import com.uav.mandiratepe.vo.CustomerAddressVO;

import java.util.List;

import static com.uav.mandiratepe.activity.Check_Out.pincode;

public class Customer_Address_RecyclerViewAdapter extends  RecyclerView.Adapter<Customer_Address_RecyclerViewAdapter.ProdectViewHolder>{
        Context mctx ;
        List<CustomerAddressVO> productslist;
        int Activityname;



        public Customer_Address_RecyclerViewAdapter(Context mctx, List<CustomerAddressVO> productslist,  int Activityname) {
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
        public void onBindViewHolder(ProdectViewHolder myholder,int position) {

        ProdectViewHolder holder=myholder;
        final CustomerAddressVO pro=productslist.get(position);

        if(pro!=null){
            holder.textView.setText(pro.getFullAddress());
            holder.linearLayout.setTag(pro.getCustomerAddressId());
            holder.image.setVisibility(View.GONE);
            holder.textView.setVisibility(View.VISIBLE);
            Log.w("address",pro.getAreaName()+":"+pro.getAreaId());
        }else {
            holder.image.setVisibility(View.VISIBLE);
            holder.textView.setVisibility(View.GONE);
            holder.linearLayout.setTag(null);
        }





        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Check_Out.addressposstion=  view.getTag()!=null?(int)view.getTag():null;


                    Check_Out.addresslayout.setVisibility(View.VISIBLE);
                    TranslateAnimation animate = new TranslateAnimation(
                            0,
                            0,
                            view.getHeight(),
                            0);
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    Check_Out.addresslayout.startAnimation(animate);




                if(pro!=null){
                    Check_Out.usermobile.setText(pro.getMobileNumber());
                    Check_Out.useraddress.setText(pro.getAddress1());
                    Check_Out.userlandmark.setText(pro.getLandMark());
                    pincode.setText(pro.getPincode());
                    Check_Out.city.setText(pro.getCity().getCityName());
                    Check_Out.state.setText(pro.getStateRegion().getStateRegionName());
                    Check_Out.username.setText(pro.getCustomerName());
                    Check_Out.area.setText(pro.getAreaName());
                    Check_Out.areaId = pro.getAreaId();

                    Check_Out.usermobile.setEnabled(false);
                    Check_Out.useraddress.setEnabled(false);
                    Check_Out.userlandmark.setEnabled(false);
                    Check_Out.area.setEnabled(false);
                    pincode.setEnabled(false);
                    Check_Out.city.setEnabled(false);
                    Check_Out.state.setEnabled(false);
                    Check_Out.username.setEnabled(false);




                }else {
                    Check_Out.usermobile.setText("");
                    Check_Out.useraddress.setText("");
                    Check_Out.userlandmark.setText("");
                    pincode.setText("");
                    Check_Out.city.setText("");
                    Check_Out.state.setText("");
                    Check_Out.username.setText("");
                    Check_Out.area.setText("");

                    Check_Out.usermobile.setEnabled(true);
                    Check_Out.useraddress.setEnabled(true);
                    Check_Out.userlandmark.setEnabled(true);
                    pincode.setEnabled(true);
                    Check_Out.username.setEnabled(true);
                    Check_Out.area.setEnabled(true);

                }

                Check_Out.usermobile.setError(null);
                Check_Out.useraddress.setError(null);
                Check_Out.userlandmark.setError(null);
              //  ((TextView)Check_Out.area.getSelectedView()).setError(null);
                pincode.setError(null);
                Check_Out.city.setError(null);
                Check_Out.state.setError(null);
                Check_Out.username.setError(null);
            }
        });




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
