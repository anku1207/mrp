package com.uav.mandiratepe.adpater;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.uav.mandiratepe.R;
import com.uav.mandiratepe.override.UAVTextView;
import com.uav.mandiratepe.vo.DataAdapterVO;


import java.util.ArrayList;

public class TextTextAdapter extends BaseAdapter {
    private Context context;
    ArrayList<DataAdapterVO> dataList;
    private int design;
    private LayoutInflater layoutInflater;
    private int length;

   public TextTextAdapter(Context context, ArrayList<DataAdapterVO> dataList, int design){
        this.context=context;
        this.dataList = dataList;
        this.design = design;
        layoutInflater=((Activity)context).getLayoutInflater();
        this.length = dataList.size();
    }
    @Override
    public int getCount() {
        return this.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        convertView = LayoutInflater.from(context).
                inflate(this.design, parent, false);

        UAVTextView textViewL=(UAVTextView) convertView.findViewById(R.id.listtext1);
        UAVTextView textViewR=(UAVTextView) convertView.findViewById(R.id.listtext2);

        DataAdapterVO dataAdapterVO = (DataAdapterVO)dataList.get(position);

        if(dataAdapterVO.getText().equals("Total")){
            textViewR.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }

        if(dataAdapterVO.getText().equals("Category")){
            textViewL.setText( dataAdapterVO.getText());
            textViewR.setText( dataAdapterVO.getText2());

        }else {
            textViewL.setText( dataAdapterVO.getText());
            textViewR.setText(context.getString(R.string.Rs)+" "+ dataAdapterVO.getText2());

        }

        return convertView;
    }

}
