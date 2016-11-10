package com.adm.dictionary.dictionary;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.adm.dictionary.app.MyApp;
import com.adm.dictionary.base.BaseActivity;
import com.adm.dictionary.bean.WordBean;
import com.adm.dictionary.util.SharedPreUtil;
import com.zhl.cbdialog.CBDialogBuilder;

import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.rx.RxQuery;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 单词本Activity
 * Created by Administrator on 2016/10/25.
 */
public class DanCiBenActivity extends BaseActivity {

    private ListView lv;
    private List<WordBean> list;
    private Button edit, add;
    private boolean editMode;
    private DanCiAdapter adapter;
    private RxDao<WordBean, Long> dao;
    private Dialog dialog;
    private List<String> planList;
    private int position = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danciben);
        initView();
        setListener();
        getData();
    }

    /**
     * 从数据库中获取单词本
     */
    private void getData() {
        dao = ((MyApp) getApplication()).getDaoSession().getWordBeanDao().rx();
        RxQuery<WordBean> query = ((MyApp) getApplication()).getDaoSession().getWordBeanDao().queryBuilder().rx();
        query.list().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<WordBean>>() {
            @Override
            public void call(List<WordBean> wordBeen) {
                list.addAll(wordBeen);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 设置监听事件
     */
    private void setListener() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add.setVisibility(View.VISIBLE);
                for (WordBean bean : list) {
                    bean.setChecked(false);
                }
                editMode = true;
                adapter.notifyDataSetChanged();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add.setVisibility(View.GONE);
                editMode = false;
                adapter.notifyDataSetChanged();
                addToPlan();

            }
        });

    }

    /**
     * 添加到复习计划
     */
    private void addToPlan() {
        planList = SharedPreUtil.getGroup(this, "group");

        if (planList == null) {
            showToast("暂无计划，赶紧去添加吧~");
            return;
        }
        String[] items = new String[planList.size()];
        dialog = new CBDialogBuilder(this, CBDialogBuilder.DIALOG_STYLE_NORMAL).showConfirmButton(true).setTitle("选择计划").showIcon(false).setItems(planList.toArray(items), new CBDialogBuilder.onDialogItemClickListener() {
            @Override
            public void onDialogItemClick(CBDialogBuilder.DialogItemAdapter ItemAdapter, Context context, CBDialogBuilder dialogbuilder, Dialog dialog, int position) {
                //选择item的事件
                DanCiBenActivity.this.position = position;
            }
        }).setButtonClickListener(true, new CBDialogBuilder.onDialogbtnClickListener() {
            @Override
            public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                //确认按钮的事件
                if (position == -1) {
                    showToast("请选择计划~");
                    return;
                }
                insertDataBase();
            }
        }).create();
        dialog.show();

    }

    /**
     * 加入复习计划到数据库
     */
    private void insertDataBase() {
        for (WordBean bean : list) {
            if (bean.isChecked()) {
                bean.setGroupName(planList.get(position));
            }
        }
        dao.updateInTx(list).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Iterable<WordBean>>() {
            @Override
            public void call(Iterable<WordBean> wordBeen) {
                showToast("添加计划列表成功");
                position = -1;//
            }
        });
    }

    @Override
    public void initView() {
        edit = findButById(R.id.act_danciben_edit);
        add = findButById(R.id.act_danciben_but);
        lv = (ListView) findViewById(R.id.act_danciben_lv);
        TextView emptyView = new TextView(this);
        emptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        emptyView.setText("单词本空空如也，快去搜索添加吧~");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) lv.getParent()).addView(emptyView);
        lv.setEmptyView(emptyView);
        list = new ArrayList<>();
        adapter = new DanCiAdapter();
        lv.setAdapter(adapter);
    }


    class DanCiAdapter extends BaseAdapter {

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
        public View getView(int position, View v, ViewGroup parent) {
            ViewHolder vh;
            if (v == null) {
                v = View.inflate(DanCiBenActivity.this, R.layout.item_danci, null);
                vh = new ViewHolder();
                vh.name = (TextView) v.findViewById(R.id.item_danci_name);
                vh.symbol = (TextView) v.findViewById(R.id.item_danci_symbol);
                vh.meaning = (TextView) v.findViewById(R.id.item_danci_meaning);
                vh.checkBox = (CheckBox) v.findViewById(R.id.item_danci_check);
                vh.del = (TextView) v.findViewById(R.id.item_danci_del);
                v.setTag(vh);
            } else {
                vh = (ViewHolder) v.getTag();
            }
            final WordBean bean = list.get(position);
            vh.name.setText(bean.getName());
            vh.symbol.setText("[英]" + bean.getEn_sympol() + "/[美]" + bean.getAm_symbol());
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < bean.getParts().size(); i++) {
                builder.append(bean.getParts().get(i).getPart()).append("  ").append(bean.getParts().get(i).getMeans()).append("\r\n");
            }
            vh.meaning.setText(builder.toString());
            vh.checkBox.setChecked(bean.isChecked());
            if (editMode) {
                vh.checkBox.setVisibility(View.VISIBLE);
            } else {
                vh.checkBox.setVisibility(View.GONE);
            }
            vh.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    bean.setChecked(isChecked);
                }
            });
            vh.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dao.delete(bean).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            showToast("移除成功");
                            list.remove(bean);
                            notifyDataSetChanged();
                        }
                    });
                }
            });

            return v;
        }

        class ViewHolder {
            TextView name, symbol, meaning, del;
            CheckBox checkBox;
        }
    }


    @Override
    public void onBackPressed() {
        if (editMode) {
            add.setVisibility(View.GONE);
            editMode = false;
            adapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }
}
