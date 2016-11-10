package com.adm.dictionary.dictionary;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.adm.dictionary.app.MyApp;
import com.adm.dictionary.base.BaseActivity;
import com.adm.dictionary.bean.PlanCount;
import com.adm.dictionary.bean.WordBean;
import com.adm.dictionary.dao.WordBeanDao;
import com.adm.dictionary.util.SharedPreUtil;
import com.zhl.cbdialog.CBDialogBuilder;

import org.greenrobot.greendao.rx.RxQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 复习计划列表
 * Created by Administrator on 2016/10/26.
 */
public class PlanActivity extends BaseActivity {

    private ListView lv;
    private Button add;
    private List<String> plans;
    private Random random;
    private static String GROUP_KEY = "group";
    private PlanAdapter adapter;
    private Dialog dialog;
    private List<PlanCount> counts;
    private WordBeanDao dao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planlist);
        dao = ((MyApp) getApplication()).getDaoSession().getWordBeanDao();
        initView();
        setUpData();
    }

    private void setUpData() {

        List<String> list = SharedPreUtil.getGroup(this, GROUP_KEY);
        plans = new ArrayList<>();
        counts = new ArrayList<>();
        if (list != null) {
            plans.addAll(list);
            for (String str : plans) {
                long a = ((MyApp) getApplication()).getDaoSession().getWordBeanDao().queryBuilder().where(WordBeanDao.Properties.GroupName.eq(str)).buildCount().count();
                long b = ((MyApp) getApplication()).getDaoSession().getWordBeanDao().queryBuilder().where(WordBeanDao.Properties.GroupName.eq(str)).
                        where(WordBeanDao.Properties.Done.eq(true)).buildCount().count();
                counts.add(new PlanCount(a, b));
            }
        }
        adapter = new PlanAdapter();
        lv.setAdapter(adapter);
    }

    @Override
    public void initView() {
        random = new Random();
        lv = (ListView) findViewById(R.id.act_plan_lv);
        TextView emptyView = new TextView(this);
        emptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        emptyView.setText("复习计划空空如也，快去添加吧~");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) lv.getParent()).addView(emptyView);
        lv.setEmptyView(emptyView);
        add = findButById(R.id.act_plan_add);
        //添加计划事件
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.show();
                }
            }
        });
        View v = View.inflate(this, R.layout.view_et, null);
        final EditText et = (EditText) v.findViewById(R.id.et);
        dialog = new CBDialogBuilder(this, CBDialogBuilder.DIALOG_STYLE_NORMAL).setTitle("新建").showIcon(false).setButtonClickListener(true, new CBDialogBuilder.onDialogbtnClickListener() {
            @Override
            public void onDialogbtnClick(Context context, Dialog dialog, int whichBtn) {
                String name = et.getText().toString().trim();
                if (name.equals("")) {
                    showToast("请输入计划名称");
                    return;
                }
                if (plans.contains(name)) {
                    showToast("计划已经存在");
                    return;
                }
                et.setText("");
                plans.add(name);
                SharedPreUtil.saveGroup(PlanActivity.this, GROUP_KEY, plans);
                counts.add(new PlanCount(0, 0));
                adapter.notifyDataSetChanged();
            }
        }).setView(v).create();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (counts.get(position).getTotal() == 0) {
                    showToast("快去单词本添加单词吧~");
                    return;
                }
                if (counts.get(position).getTotal() == counts.get(position).getDone()) {
                    showToast("该计划已完成，自动删除");
                    //将数据库中的done改为false
                    List<WordBean> beans = dao.queryBuilder().where(WordBeanDao.Properties.GroupName.eq(plans.get(position))).build().list();
                    for (WordBean bean : beans) {
                        bean.setDone(false);
                    }
                    dao.updateInTx(beans);
                    plans.remove(position);
                    counts.remove(position);
                    SharedPreUtil.saveGroup(PlanActivity.this, "group", plans);
                    adapter.notifyDataSetChanged();
                    return;
                }
                Intent intent = new Intent(PlanActivity.this, PlanPracticeActivity.class);
                intent.putExtra("groupName", plans.get(position));
                startActivity(intent);
            }
        });
    }

    class PlanAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return plans.size();
        }

        @Override
        public Object getItem(int position) {
            return plans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            ViewHolder vh = null;

            if (v == null) {
                v = View.inflate(PlanActivity.this, R.layout.item_plan, null);
                vh = new ViewHolder();
                vh.cv = (CardView) v.findViewById(R.id.item_plan_cv);
                vh.name = (TextView) v.findViewById(R.id.item_plan_name);
                vh.des = (TextView) v.findViewById(R.id.item_plan_des);
                v.setTag(vh);
            } else {
                vh = (ViewHolder) v.getTag();
            }

            int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
            vh.cv.setCardBackgroundColor(ranColor);
            vh.name.setText(plans.get(position));
            vh.des.setText("共" + counts.get(position).getTotal() + "个，已完成" + counts.get(position).getDone() + "个");
            return v;
        }

        class ViewHolder {
            TextView name, des;
            CardView cv;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpData();
    }
}
