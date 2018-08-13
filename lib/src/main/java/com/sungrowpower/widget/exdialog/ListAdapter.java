package com.sungrowpower.widget.exdialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sungrowpower.widget.R;

import java.util.List;

/**
 * @author huangzhihong
 * @version 1.0
 * @description ListAdapter
 * @date 2018/8/11
 */
public class ListAdapter extends BaseAdapter {

    private List<String> mList;
    private ExDialog.Builder mBuilder;

    public ListAdapter(ExDialog.Builder builder) {
        this.mList = builder.mList;
        this.mBuilder = builder;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public String getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mBuilder.mContext).inflate(R.layout.exd_dialog_item,parent,false);
        }
        TextView item = (TextView) convertView.findViewById(R.id.exd_id_item);
        item.setText(mList.get(position));
        if (mBuilder.mTypeEnum == TypeEnum.LIST && mBuilder.mListItemTextColor != -1) {
            item.setTextColor(mBuilder.mListItemTextColor);
        }
        if (mBuilder.mTypeEnum == TypeEnum.SHEET && mBuilder.mSheetItemTextColor != -1) {
            item.setTextColor(mBuilder.mSheetItemTextColor);
        }
        return convertView;
    }
}
