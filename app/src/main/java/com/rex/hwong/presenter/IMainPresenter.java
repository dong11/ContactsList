package com.rex.hwong.presenter;

import com.rex.hwong.bean.Contacts;

import java.util.ArrayList;

/**
 * @author dong {hwongrex@gmail.com}
 * @date 16/6/8
 * @time 上午9:49
 */
public interface IMainPresenter {
    void queryContacts();
    void filterContacts(ArrayList<Contacts> list, String filterStr);
}
