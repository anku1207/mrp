package com.uav.mandiratepe.adpater;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import com.uav.mandiratepe.R;
import com.uav.mandiratepe.activity.Product_List;
import com.uav.mandiratepe.util.Utility;
import com.uav.mandiratepe.vo.ProductVO;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

public class Product_Adapter extends RecyclerView.Adapter<Product_Adapter.ProdectViewHolder> {
    Context mctx;
    List<ProductVO> productslist;
    int Activityname;
    Animation animation ;
    RecyclerView recyclerView;
    DecimalFormat df = new DecimalFormat(".000");
    DecimalFormat gmsdf = new DecimalFormat("000");



    public static JSONObject jsonObject =new JSONObject();



    public Product_Adapter(Context mctx, List<ProductVO> productslist, int Activityname ,RecyclerView recyclerView) {
        this.mctx = mctx;
        this.productslist = productslist;
        this.Activityname = Activityname;
        this.recyclerView=recyclerView;

        animation = AnimationUtils.loadAnimation(mctx, R.anim.animation);

    }

    @Override
    public Product_Adapter.ProdectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mctx);
        View  view = layoutInflater.inflate(Activityname, parent, false);
        return new Product_Adapter.ProdectViewHolder(view);
    }

    @Override

    public void onBindViewHolder(final Product_Adapter.ProdectViewHolder holder, final int position) {

        final ProductVO pro = productslist.get(position);

       // orderItemList.put(pro.getCategoryId()+"-"+pro.getProductID(),pro);

        holder.name.setText(pro.getProductName());
        holder.rate.setText( String.valueOf( pro.getMop().intValue()) +"/"+String.valueOf(pro.getUnitTypeName()) );
        holder.value.setText( String.valueOf( (pro.getQty()==null || pro.getQty()==0?0:pro.getQty()  )));
        holder.amount.setText( mctx.getString(R.string.Rs)+" "+ String.valueOf(pro.getTotal()==null ?0:pro.getTotal()));
        if(pro.getUtfCode()!=null){
            if(Utility.hindinameutf8(pro.getUtfCode())!= null && !Utility.hindinameutf8(pro.getUtfCode()).equals("")){
                String[] utfArr=pro.getUtfCode().split(" ");
                String utlText="";
                for(int i=0; i<utfArr.length; i++){
                    if(utlText!="") utlText +=" ";
                    utlText += String.valueOf(Html.fromHtml(Utility.hindinameutf8(utfArr[i])));
                }
                holder.hindiname.setText(utlText);
            }else{
                holder.hindiname.setText("");
            }
           // String result =pro.getUtfCode();

        }else{
            holder.hindiname.setText("");
        }
        if((pro.getMaximumQty()!=null?pro.getMaximumQty():0)>(pro.getQty()==null || pro.getQty()==0?0:pro.getQty() )){
            holder.add.setEnabled(true);
        }else {
            holder.add.setEnabled(false);
        }


        if(!pro.getUnitTypeName().equals("PCS") && pro.getQty()!=null && pro.getQty()!=0){



            holder.weight.setText(getComputedWt(pro));
            holder.weight.setVisibility(View.VISIBLE);
        }else {
            holder.weight.setVisibility(View.GONE);
            holder.weight.setText("");
        }



        Picasso.with(mctx).load(pro.getThumbnailImagePath()).fit()
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.vegitable)
                .into(holder.image);
        holder.add.setTag(position);

        if(pro.getNutritionImage()!=null){
            holder.vetamininfo.setVisibility(View.VISIBLE);
        }else {
            holder.vetamininfo.setVisibility(View.GONE);
        }


        holder.vetamininfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nutritionsInfo(pro);
            }
        });


        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.amount.startAnimation(animation);
                Product_List.totalamount.startAnimation(animation);
                Product_List.categaryamount.startAnimation(animation);
                Product_List.categaryitem.startAnimation(animation);
                holder.weight.startAnimation(animation);
                try {

                    if(Integer.parseInt(holder.value.getText().toString())>=0 ){
                        computeProductAmount(1,holder,pro, position );

                    }
                }catch (Exception e){
                    Log.w("Error",e.getMessage());
                }
            }
        });


        holder.subtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.amount.startAnimation(animation);
                Product_List.totalamount.startAnimation(animation);
                Product_List.categaryamount.startAnimation(animation);
                Product_List.categaryitem.startAnimation(animation);
                holder.weight.startAnimation(animation);
                try {
                    if(Integer.parseInt(holder.value.getText().toString())>0){
                        computeProductAmount(-1,holder,pro, position );

                    }
                }catch (Exception  e){

                    Toast.makeText(mctx, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return productslist.size();
    }

    public class ProdectViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView textView,add,subtraction,value,amount,name,hindiname,rate,weight;
        TextView vetamininfo;
        ImageView image;



        public ProdectViewHolder(View rootView) {
            super(rootView);
             vetamininfo = rootView.findViewById(R.id.vetamininfo);


             add=rootView.findViewById(R.id.add);
             subtraction=rootView.findViewById(R.id.subtraction);
             value=rootView.findViewById(R.id.value);
             amount=rootView.findViewById(R.id.amount);
             image=rootView.findViewById(R.id.image);
             name=rootView.findViewById(R.id.name);
             hindiname=rootView.findViewById(R.id.hindiname);
             rate=rootView.findViewById(R.id.rate);
             weight=rootView.findViewById(R.id.weight);

        }
    }

    public void computeProductAmount(int signifier, Product_Adapter.ProdectViewHolder holder, ProductVO productVO, int position ){


        holder.value.setText(String.valueOf(Integer.parseInt(holder.value.getText().toString())+ signifier));
        productVO.setQty(Integer.parseInt(holder.value.getText().toString()));
        productslist.set(position, productVO);
        Product_List.orderItemList.put(productVO.getCategoryId()+"-"+productVO.getProductID(),productVO);
        productVO.setTotal(String.valueOf(productVO.getQty()*productVO.getMop().intValue()));
        holder.amount.setText(mctx.getString(R.string.Rs)+" "+String.valueOf(productVO.getQty()*productVO.getMop().intValue()));
        Product_List.categaryitem.setText(mctx.getString(R.string.Rs)+" : "+String.valueOf(Product_List.computeOrderProductAmt(Product_List.orderItemList)));



        if(productVO.getQty()!=0 && !productVO.getUnitTypeName().equals("PCS")){
            holder.weight.setVisibility(View.VISIBLE);

            holder.weight.setText(getComputedWt(productVO));
        //    holder.weight.setText("Weight : "+(df.format((productVO.getQty()*productVO.getAvgweight())/1000)) +" Kg");
        }else {
            holder.weight.setVisibility(View.GONE);
            holder.weight.setText("");
        }
        if((productVO.getMaximumQty()!=null?productVO.getMaximumQty():0)>(productVO.getQty()==null || productVO.getQty()==0?0:productVO.getQty() )){
           holder.add.setEnabled(true);
        }else {
            holder.add.setEnabled(false);
        }
    }

    public  String getComputedWt(ProductVO pro){
       Double wt= pro.getQty()*pro.getAvgweight();
        String wtValue="", wtUnit="Kg";
        if(wt>=1000) {
            int mod = wt.intValue() % 1000;
            if(mod==0){
                wtValue =  String.valueOf(( wt.intValue())/ 1000);
            }else {
                wtValue = df.format((pro.getQty() * pro.getAvgweight()) / 1000);
            }
        }else if((pro.getQty()*pro.getAvgweight())<1000){
            wtValue =gmsdf.format(pro.getQty()*pro.getAvgweight());
            wtUnit =  "Gms";
        }
        return "Weight : "+wtValue +" "+ wtUnit;
    }

    public void nutritionsInfo(ProductVO productVO){
        Dialog var3 = new Dialog(mctx);
        var3.requestWindowFeature(1);
        var3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        var3.setContentView(R.layout.product_nutritions);
        // var3.setCanceledOnTouchOutside(false);
        var3.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        ImageView  imageView =var3.findViewById(R.id.imageview);

        Picasso.with(mctx).load(productVO.getNutritionImage())
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.vegitable)
                .into(imageView);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(var3.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        var3.show();
        var3.getWindow().setAttributes(lp);
    }
}