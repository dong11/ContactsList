package com.rex.hwong.presenter.impl;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.rex.hwong.bean.Contacts;
import com.rex.hwong.presenter.IMainPresenter;
import com.rex.hwong.ui.iView.IMain;
import com.rex.hwong.utils.CharacterParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author dong {hwongrex@gmail.com}
 * @date 16/6/8
 * @time 上午9:51
 */

public class MainPresenterImpl implements IMainPresenter{
    private IMain mIMain;
    private Context mContext;
    private CharacterParser mCharacterParser;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x01) {
                mIMain.showContacts((ArrayList<Contacts>) msg.obj);
            }
        }
    };

    public MainPresenterImpl(IMain main, Context context) {
        if (main == null)
            throw new IllegalArgumentException("main must not be null");
        mIMain = main;
        mContext = context;
        mCharacterParser = CharacterParser.getInstance();
    }

    @Override
    public void queryContacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver resolver = mContext.getContentResolver();
                ArrayList<Contacts> list = queryContacts(resolver);
                Message msg = mHandler.obtainMessage();
                msg.what = 0x01;
                msg.obj = list;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void filterContacts(ArrayList<Contacts> list, String filterStr) {
        ArrayList<Contacts> filterDateList = new ArrayList<Contacts>();

        if(TextUtils.isEmpty(filterStr)){
            filterDateList = list;
        }else{
            filterDateList.clear();
            for(Contacts contacts : list) {
                String name = contacts.getName();
                if(name != null && (name.indexOf(filterStr.toString()) != -1 || mCharacterParser.getSelling(name).startsWith(filterStr.toString()))){
                    filterDateList.add(contacts);
                }
            }
        }

        Collections.sort(filterDateList);
        mIMain.showContacts(filterDateList);
    }

    /**
     * 读取联系人
     * @param resolver
     */
    private ArrayList<Contacts> queryContacts(ContentResolver resolver){
        ArrayList<Contacts> list = new ArrayList<>();
        Uri uri = Uri.parse("content://com.android.contacts/contacts");
        Cursor cursor = resolver.query(uri, new String[]{"_id"}, null, null, null);
        while (cursor.moveToNext()) {
            int contractID = cursor.getInt(0);
            Contacts contacts = new Contacts();
            contacts.set_id(contractID);
            uri = Uri.parse("content://com.android.contacts/contacts/" + contractID + "/data");
            Cursor cursor1 = resolver.query(uri, new String[]{"mimetype", "data1", "data2"}, null, null, null);
            while (cursor1.moveToNext()) {
                String data1 = cursor1.getString(cursor1.getColumnIndex("data1"));
                String mimeType = cursor1.getString(cursor1.getColumnIndex("mimetype"));
                if ("vnd.android.cursor.item/name".equals(mimeType)) { //是姓名
                    contacts.setName(data1);
                    String pinyin = mCharacterParser.getSelling(data1);
                    String sortString = pinyin.substring(0, 1).toUpperCase();

                    if(sortString.matches("[A-Z]")){
                        contacts.setPinyinName(sortString.toUpperCase());
                    }else{
                        contacts.setPinyinName("#");
                    }
                    contacts.setPinyinName(pinyin.substring(0, 1).toUpperCase());
                } else if ("vnd.android.cursor.item/phone_v2".equals(mimeType)) { //手机
                    contacts.setNumber(data1);
                }
            }
            list.add(contacts);
            cursor1.close();
        }
        cursor.close();

        try {
            Collections.sort(list);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
