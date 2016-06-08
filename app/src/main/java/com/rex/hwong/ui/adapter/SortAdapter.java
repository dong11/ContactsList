package com.rex.hwong.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.rex.hwong.R;
import com.rex.hwong.bean.Contacts;

import java.util.List;

/**
 * @author dong {hwongrex@gmail.com}
 * @date 16/6/8
 * @time 下午2:16
 */

public class SortAdapter extends BaseAdapter implements SectionIndexer {

    private List<Contacts> list = null;
    private Context mContext;

    public SortAdapter(Context mContext, List<Contacts> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {

        ViewHolder viewHolder = null;
        final Contacts mContent = list.get(position);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.tv_catalog);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position获取分类的首字母的char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(section)){
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getPinyinName());
        }else{
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(list.get(position).getName()) || "null".equals(list.get(position).getName())){
            viewHolder.tvTitle.setText(""+ list.get(position).getNumber());
        } else {
            viewHolder.tvTitle.setText(""+ list.get(position).getName());
        }

        return view;

    }

    class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
    }


    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getPinyinName().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getPinyinName();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}
