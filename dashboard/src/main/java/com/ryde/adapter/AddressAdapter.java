package com.ryde.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryde.R;
import com.ryde.model.AddressModel;

import java.util.ArrayList;

/**
 * Created by dsndw3 on 09.06.2017.
 */

public class AddressAdapter extends BaseAdapter{

    Context context;
    ArrayList<AddressModel> arrayList;

    public AddressAdapter(Context context, ArrayList<AddressModel> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_address, null);

        AddressModel addressModel = (AddressModel) getItem(position);

        TextView tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);

        tvAddress.setText(addressModel.getTitle());

        return convertView;
    }
}
