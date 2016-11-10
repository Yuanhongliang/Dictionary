package com.adm.dictionary.dictionary;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adm.dictionary.app.MyApp;
import com.adm.dictionary.base.BaseActivity;
import com.adm.dictionary.bean.EnglishBean;
import com.adm.dictionary.bean.Part;
import com.adm.dictionary.bean.WordBean;
import com.adm.dictionary.dao.WordBeanDao;
import com.adm.dictionary.http.HttpMethods;

import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.rx.RxQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 搜索英文的结果页
 * Created by Administrator on 2016/10/19.
 */
public class EnglishSearchActivity extends BaseActivity {

    private String searchStr;
    private TextView name, ukDuyin, usDuyin;
    private LinearLayout parts;
    private ImageView ukVoice, usVoice, star;
    private MediaPlayer mp;
    private RxQuery<WordBean> query;
    private RxDao<WordBean, Long> dao;
    private RxDao<Part, Long> partDao;
    private String wordName;
    private boolean collectState;
    public WordBean wordBean;
    private EnglishBean englishBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_search);
        searchStr = getIntent().getStringExtra("searchStr");
        initView();
        HttpMethods.getInstance().searchEnglish(searchStr).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<EnglishBean>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("error", e.getLocalizedMessage());
            }

            @Override
            public void onNext(EnglishBean englishBean) {
                setUpData(englishBean);
                EnglishSearchActivity.this.englishBean = englishBean;
            }
        });

    }

    /**
     * 将数据显示到控件上
     *
     * @param englishBean
     */
    private void setUpData(EnglishBean englishBean) {
        if (englishBean.getWord_name() == null || englishBean.getWord_name().equals("")) {
            showToast("没有您想要的结果哦");
            return;
        }
        wordName = englishBean.getWord_name();
        queryResult();
        name.setText(englishBean.getWord_name());
        final EnglishBean.Symbols symbols = englishBean.getSymbols().get(0);
        ukDuyin.setText("[英] " + symbols.getPh_en());
        usDuyin.setText("[美] " + symbols.getPh_am());
        //播放音频
        ukVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (symbols.getPh_en_mp3() == null || symbols.getPh_en_mp3().equals("")) {
                    showToast("暂无匹配资源");
                    return;
                }
                mp.reset();
                try {
                    mp.setDataSource(symbols.getPh_en_mp3());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.prepareAsync();
            }
        });
        usVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (symbols.getPh_am_mp3() == null || symbols.getPh_am_mp3().equals("")) {
                    showToast("暂无匹配资源");
                    return;
                }
                mp.reset();
                try {
                    mp.setDataSource(symbols.getPh_am_mp3());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.prepareAsync();
            }
        });
        for (int i = 0; i < symbols.getParts().size(); i++) {
            View v = View.inflate(this, R.layout.text_means, null);
            TextView part = (TextView) v.findViewById(R.id.part);
            TextView means = (TextView) v.findViewById(R.id.means);
            part.setText(symbols.getParts().get(i).getPart());
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < symbols.getParts().get(i).getMeans().size(); j++) {
                if (j < symbols.getParts().get(i).getMeans().size() - 1) {
                    sb.append(symbols.getParts().get(i).getMeans().get(j)).append(";").append("\r\n");
                } else {
                    sb.append(symbols.getParts().get(i).getMeans().get(j));
                }
            }
            means.setText(sb.toString());
            parts.addView(v);
        }


    }

    private void queryResult() {
        query = ((MyApp) getApplication()).getDaoSession().getWordBeanDao().queryBuilder().where(WordBeanDao.Properties.Name.eq(wordName)).rx();
        query.list().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<WordBean>>() {


            @Override
            public void call(List<WordBean> wordBeen) {
                Log.e("query", wordBeen.toString());
                if (wordBeen == null || wordBeen.size() == 0) {
                    collectState = false;
                    star.setImageResource(R.drawable.staroff);
                } else {
                    wordBean = wordBeen.get(0);
                    collectState = true;
                    star.setImageResource(R.drawable.staron);
                }
            }
        });
    }

    @Override
    public void initView() {
        dao = ((MyApp) getApplication()).getDaoSession().getWordBeanDao().rx();
        partDao = ((MyApp) getApplication()).getDaoSession().getPartDao().rx();
        name = findTextViewById(R.id.act_english_name);
        ukDuyin = findTextViewById(R.id.act_english_ukduyin);
        usDuyin = findTextViewById(R.id.act_english_usduyin);
        ukVoice = findImageViewById(R.id.act_english_ukvoice);
        usVoice = findImageViewById(R.id.act_english_usvoice);
        parts = findLinById(R.id.act_english_lin);
        star = findImageViewById(R.id.act_english_star);
        mp = new MediaPlayer();
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collectState) {
                    dao.deleteByKey(wordBean.getId()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            partDao.deleteInTx(wordBean.getParts()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {
                                    showToast("移除成功");
                                    collectState = false;
                                    star.setImageResource(R.drawable.staroff);
                                }
                            });
                        }
                    });
                } else {
                    final WordBean word = new WordBean();
                    word.setName(englishBean.getWord_name());
                    word.setAm_symbol(englishBean.getSymbols().get(0).getPh_am());
                    word.setEn_sympol(englishBean.getSymbols().get(0).getPh_en());
                    word.setAm_mp3(englishBean.getSymbols().get(0).getPh_am_mp3());
                    word.setEn_mp3(englishBean.getSymbols().get(0).getPh_en_mp3());
                    dao.insert(word).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<WordBean>() {
                        @Override
                        public void call(WordBean wordBean) {
                            EnglishSearchActivity.this.wordBean = wordBean;
                            List<EnglishBean.Symbols.Parts> parts = englishBean.getSymbols().get(0).getParts();
                            final List<Part> addParts = word.getParts();
                            for (int i = 0; i < parts.size(); i++) {
                                Part part = new Part(null, wordBean.getId(), parts.get(i).getPart(), parts.get(i).getMeans().toString());
                                addParts.add(part);
                            }
                            partDao.insertInTx(addParts).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Iterable<Part>>() {
                                @Override
                                public void call(Iterable<Part> parts) {
                                    showToast("添加成功");
                                    collectState = true;
                                    star.setImageResource(R.drawable.staron);
                                }
                            });

                        }
                    });

                }
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
