package com.adm.dictionary.dictionary;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.adm.dictionary.base.BaseActivity;
import com.adm.dictionary.bean.ChineseBean;
import com.adm.dictionary.http.HttpMethods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.adm.dictionary.bean.ChineseBean.Symbol.Parts.Means;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 搜索中文的结果页
 * Created by Administrator on 2016/10/19.
 */
public class ChineseSearchActivity extends BaseActivity {

    private String searchStr;
    private TextView name, duyin;
    private ImageView voice;
    private ListView lv;
    private List<String> meanings;
    private ArrayAdapter<String> adapter;
    private MediaPlayer mp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinese_search);
        searchStr = getIntent().getStringExtra("searchStr");
        initView();
        HttpMethods.getInstance().searchChinese(searchStr).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ChineseBean>() {
            @Override
            public void call(final ChineseBean chineseBean) {
                name.setText(chineseBean.getWord_name());
                String duyinStr = chineseBean.getSymbols().get(0).getWord_symbol();
                if (duyinStr != null && !duyinStr.equals("")) {
                    duyin.setText(duyinStr);
                } else {
                    duyin.setVisibility(View.GONE);
                    voice.setVisibility(View.GONE);
                }
                if (chineseBean.getSymbols().get(0).getParts() != null) {
                    List<Means> means = chineseBean.getSymbols().get(0).getParts().get(0).getMeans();
                    for (Means mean : means) {
                        meanings.add(mean.getWord_mean());
                    }
                }else{
                    showToast("暂无搜索结果");
                }
                adapter.notifyDataSetChanged();
                //播放音频
                voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chineseBean.getSymbols().get(0).getSymbol_mp3() == null || chineseBean.getSymbols().get(0).getSymbol_mp3().equals("")) {
                            showToast("暂无匹配资源");
                            return;
                        }
                        mp.reset();
                        try {
                            mp.setDataSource(chineseBean.getSymbols().get(0).getSymbol_mp3());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mp.prepareAsync();
                    }
                });
            }
        });
    }

    @Override
    public void initView() {
        name = findTextViewById(R.id.act_chinese_name);
        duyin = findTextViewById(R.id.act_chinese_duyin);
        voice = findImageViewById(R.id.act_chinese_voice);
        lv = (ListView) findViewById(R.id.act_chinese_lv);
        meanings = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.text_history, meanings);
        lv.setAdapter(adapter);
        mp = new MediaPlayer();
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChineseSearchActivity.this, EnglishSearchActivity.class);
                intent.putExtra("searchStr", meanings.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp.isPlaying()) {
            mp.stop();
        }
        mp.release();
    }
}
