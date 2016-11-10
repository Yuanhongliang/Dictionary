package com.adm.dictionary.dictionary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.adm.dictionary.app.MyApp;
import com.adm.dictionary.base.BaseActivity;
import com.adm.dictionary.bean.WordBean;
import com.adm.dictionary.dao.WordBeanDao;
import com.adm.dictionary.view.CardPagerAdapter;
import com.adm.dictionary.view.ShadowTransformer;

import org.greenrobot.greendao.rx.RxQuery;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 复习单词的界面
 * Created by Administrator on 2016/10/27.
 */
public class PlanPracticeActivity extends BaseActivity {

    private ViewPager vp;
    private CardPagerAdapter adapter;
    private ShadowTransformer transformer;
    private RxQuery<WordBean> query;
    private List<WordBean> list;
    private String groupName;
    private int position;
    private WordBeanDao dao;
    private Button remember, unremember;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_practice);
        groupName = getIntent().getStringExtra("groupName");
        initView();
    }

    @Override
    public void initView() {
        vp = (ViewPager) findViewById(R.id.act_prac_vp);
        remember = findButById(R.id.act_prac_remember);
        unremember = findButById(R.id.act_prac_unremember);
        dao = ((MyApp) getApplication()).getDaoSession().getWordBeanDao();
        query = ((MyApp) getApplication()).getDaoSession().getWordBeanDao().queryBuilder().where(WordBeanDao.Properties.GroupName.eq(groupName))
                .where(WordBeanDao.Properties.Done.eq("false")).rx();
        query.list().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<WordBean>>() {
            @Override
            public void call(List<WordBean> wordBeen) {
                list = wordBeen;
                adapter = new CardPagerAdapter(list);
                transformer = new ShadowTransformer(vp, adapter);
                transformer.enableScaling(true);
                vp.setAdapter(adapter);
                vp.setPageTransformer(false, transformer);
            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                PlanPracticeActivity.this.position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WordBean bean = list.get(position);
                bean.setDone(true);
                dao.update(bean);
                if (position != list.size() - 1) {
                    vp.setCurrentItem(position + 1);
                } else {
//                    最后一个界面的时候 关闭
                    showToast("本次复习结束，继续努力");
                    finish();
                }
            }
        });
        unremember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != list.size() - 1) {
                    vp.setCurrentItem(position + 1);
                } else {
                    showToast("本次复习结束，继续努力");
                    finish();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.releaseMp();
    }
}
