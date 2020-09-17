package com.superhong.exdialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * @author huangzhihong
 * @version 1.0
 * @description ListAdapter
 * @date 2018/8/11
 */
public class ListAdapter extends BaseAdapter {

    private List<String> mList;
    private List<Integer> colors;
    private ExDialog.Builder mBuilder;
    private int selectPosition;

    public ListAdapter(ExDialog.Builder builder) {
        this.mList = builder.getList();
        this.colors = builder.itemColors();
        this.selectPosition = builder.selectPosition();
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
            convertView = LayoutInflater.from(mBuilder.getContext()).inflate(R.layout.exd_dialog_item,parent,false);
        }
        TextView item = (TextView) convertView.findViewById(R.id.exd_id_item);
        item.setText(mList.get(position));
        ImageView icon = convertView.findViewById(R.id.exd_icon_item);
        if (selectPosition >= 0) {
            item.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            item.setPadding(dp2Px(item.getContext(),30),0,dp2Px(item.getContext(),16),0);
            if (selectPosition == position) {
                if (mBuilder.selectIcon() != null) {
                    icon.setImageDrawable(mBuilder.selectIcon());
                }
                icon.setVisibility(View.VISIBLE);
            } else {
                icon.setVisibility(View.INVISIBLE);
            }
        } else {
            icon.setVisibility(View.GONE);
        }
        if (colors != null && !colors.isEmpty()) {
            item.setTextColor(colors.get(position));
        } else {
            if (mBuilder.getTypeEnum() == TypeEnum.LIST && mBuilder.getListItemTextColor() != -1) {
                item.setTextColor(mBuilder.getListItemTextColor());
            }
            if (mBuilder.getTypeEnum() == TypeEnum.SHEET && mBuilder.getSheetItemTextColor() != -1) {
                item.setTextColor(mBuilder.getSheetItemTextColor());
            }
        }

        return convertView;
    }

    private int dp2Px(Context context,float value) {
        return (int) (context.getResources().getDisplayMetrics().density * value + 0.5f);
    }
}
