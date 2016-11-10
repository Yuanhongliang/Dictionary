package com.adm.dictionary.dictionary;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.adm.dictionary.base.BaseActivity;
import com.adm.dictionary.bean.SentenceBean;
import com.adm.dictionary.http.HttpMethods;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/19.
 */
public class SentenceTranslateActivity extends BaseActivity {

    private EditText et;

    private Button but;

    private TextView tv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence);
        initView();

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                String str = et.getText().toString().trim();
                if (et.equals("")) {
                    showToast("请输入内容");
                    return;
                }
                HttpMethods.getInstance().sentenceTransLate(str).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<SentenceBean>() {
                    @Override
                    public void call(SentenceBean sentenceBean) {
                        tv.setText(sentenceBean.getTranslation().get(0));
                    }
                });
            }
        });
    }

    @Override
    public void initView() {
        et = findEtnById(R.id.act_sentence_et);
        but = findButById(R.id.act_sentence_but);
        tv = findTextViewById(R.id.act_sentence_tv);
    }


}
