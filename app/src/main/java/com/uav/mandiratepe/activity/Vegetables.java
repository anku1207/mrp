package com.uav.mandiratepe.activity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uav.mandiratepe.R;
import com.uav.mandiratepe.adpater.Product_Adapter;
import com.uav.mandiratepe.bo.ProductBO;
import com.uav.mandiratepe.permission.Session;
import com.uav.mandiratepe.vo.ProductVO;
import com.uav.mandiratepe.volley.VolleyResponseListener;
import com.uav.mandiratepe.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.uav.mandiratepe.activity.Product_List.category;

/**
 * A simple {@link Fragment} subclass.
 */
public class Vegetables extends Fragment {

    TextView add,subtraction,value,amount,errormsg;

    TextView textView;
    RecyclerView recyclerView;
    Integer position;

    boolean isScrolling=false;
    LinearLayoutManager manager;
    int currentItems,totalItems,scrollOutItems;

    List<ProductVO> product_lists =new ArrayList<>();
    Product_Adapter product_adapter;
    ImageView imagebatchlock;


  //  SwipeRefreshLayout pullToRefresh;

    public Vegetables() {
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vegetables, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);



        recyclerView=rootView.findViewById(R.id.recyclerview);
        imagebatchlock=rootView.findViewById(R.id.imagebatchlock);
        errormsg=rootView.findViewById(R.id.errormsg);
        manager=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);

        errormsg.setVisibility(View.GONE);
        imagebatchlock.setVisibility(View.GONE);

        recyclerView.setLayoutManager(manager);

        // pullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.pullToRefresh);

        getitemlist();
       /* pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            int Refreshcounter = 1; //Counting how many times user have refreshed the layout

            @Override
            public void onRefresh() {

                try {
                    Product_List.totalamount.setText(String.valueOf(Integer.parseInt( Product_List.totalamount.getText().toString())-Integer.parseInt(Product_List.categaryamount.getText().toString())));
                    categaryitem.setText(category.getJSONObject(Product_List.selecttabid).getString("categoryName"));
                    Product_List.categaryamount.setText("0");

                    getitemlist();
                    pullToRefresh.setEnabled(false);

                }catch (Exception e){

                }



            }
        });*/






    }


    public void getitemlist(){

        try {
            JSONObject jsonObject =category.getJSONObject(getArguments().getInt("section_number"));

            VolleyUtils.makeJsonObjectRequest(getContext(), ProductBO.getTodayProductList(jsonObject.getInt("categoryId"), Session.getMandiId(getContext()),Session.getSessionByKey(getContext(),Session.CACHE_ORDERID)), new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }
                @Override
                public void onResponse(Object resp) throws JSONException {
                    JSONObject response = (JSONObject) resp;

                    if(!response.getString("status").equals("success")){
                        JSONArray error =response.getJSONArray("error");
                        imagebatchlock.setVisibility(View.VISIBLE);
                        errormsg.setVisibility(View.VISIBLE);

                        StringBuilder sb = new StringBuilder();
                        for(int i=0; i<error.length(); i++){
                            sb.append(error.get(i)).append("\n");
                        }

                        errormsg.setText(sb.toString());

                    }else {
                        JSONObject object=response.getJSONObject("dataList");

                        JSONArray jsonArray =object.getJSONArray("productList");

                        Log.w("resp",jsonArray.toString());



                        for(int i=0; i<jsonArray.length();i++ ){
                            JSONObject object1=jsonArray.getJSONObject(i);
                            ProductVO productVO =new ProductVO();

                            productVO.setProductName(object1.getString("productName"));
                            productVO.setProductID(object1.getInt("productId"));
                            productVO.setThumbnailImagePath(object1.getString("thumbnailImagePath"));
                            productVO.setMop(object1.getDouble("mop"));
                            productVO.setUnitTypeName(object1.getString("unitTypeName"));
                            productVO.setCategoryId(object1.getInt("categoryId"));
                            productVO.setCategoryName(object1.getString("categoryName"));
                            productVO.setMaximumQty(object1.getInt("maximumQty"));
                            productVO.setQty(object1.getInt("qty"));
                            productVO.setAvgweight(Double.parseDouble(object1.get("avgweight").toString()));


                            if(object1.has("nutritionImage")){
                                productVO.setNutritionImage(object1.getString("nutritionImage"));
                            }


                            if(object1.has("utfCode")){
                                productVO.setUtfCode(object1.getString("utfCode"));
                            }

                            if(Integer.valueOf(productVO.getQty())>0){
                                productVO.setTotal(String.valueOf(productVO.getQty()*productVO.getMop().intValue()));
                                Product_List.orderItemList.put(productVO.getCategoryId()+"-"+productVO.getProductID(),productVO);
                            }
                            product_lists.add(productVO);

                        }

                        Product_List.categaryitem.setText(getContext().getString(R.string.Rs)+" : "+String.valueOf(Product_List.computeOrderProductAmt(Product_List.orderItemList)));

                        // pullToRefresh.setRefreshing(false);
                        product_adapter=new Product_Adapter(getContext(),product_lists ,R.layout.item_design,recyclerView);
                        product_adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(product_adapter);







                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
