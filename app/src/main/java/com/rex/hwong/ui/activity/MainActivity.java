package com.rex.hwong.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.rex.hwong.R;
import com.rex.hwong.bean.Contacts;
import com.rex.hwong.presenter.IMainPresenter;
import com.rex.hwong.presenter.impl.MainPresenterImpl;
import com.rex.hwong.ui.adapter.SortAdapter;
import com.rex.hwong.ui.iView.IMain;
import com.rex.hwong.ui.view.ClearEditText;
import com.rex.hwong.ui.view.SlideBar;

import java.util.ArrayList;

public class MainActivity extends Activity implements IMain {

    private IMainPresenter mIMainPresenter;
    private SlideBar mSlideBar;
    private TextView mTextView;
    private SortAdapter mAdapter;
    private ListView mListView;
    private ArrayList<Contacts> mList;
    private ArrayList<Contacts> mSourceLists = new ArrayList<>();
    private ClearEditText mEditText;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mIMainPresenter = new MainPresenterImpl(this, getApplicationContext());

        mSlideBar = (SlideBar) findViewById(R.id.slideBar);

        mTextView = (TextView) findViewById(R.id.letter);

        mListView = (ListView) findViewById(R.id.listView);

        mEditText = (ClearEditText) findViewById(R.id.filter_edit);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mIMainPresenter.filterContacts(mSourceLists, s.toString());
            }
        });

        mList = new ArrayList<>();

        mAdapter = new SortAdapter(this, mList);

        mListView.setAdapter(mAdapter);

        mSlideBar.setOnTouchLetterChangeListenner(new SlideBar.OnTouchLetterChangeListenner() {

                    @Override
                    public void onTouchLetterChange(boolean isTouched, String s) {

                        mTextView.setText(s);
                        if (isTouched) {
                            mTextView.setVisibility(View.VISIBLE);
                        } else {
                            mTextView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mTextView.setVisibility(View.GONE);
                                }
                            }, 100);
                        }
                        //该字母首次出现的位置
                        int position = mAdapter.getPositionForSection(s.charAt(0));
                        mListView.setSelection(position);
                    }
                });

        mIMainPresenter.queryContacts();
    }

    @Override
    public void showContacts(ArrayList<Contacts> contacts) {
        mList.clear();
        mList.addAll(contacts);
        if(isFirst) {
            isFirst = false;
            mSourceLists.addAll(contacts);
        }
        mAdapter.notifyDataSetChanged();
    }
}
