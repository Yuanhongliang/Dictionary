package com.adm.dictionary.dictionary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.adm.dictionary.base.BaseActivity;
import com.adm.dictionary.bean.DictionaryBean;
import com.adm.dictionary.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地词典Activity
 * Created by Administrator on 2016/10/27.
 */
public class DictionaryActivity extends BaseActivity {


    private ListView lv;
    private SQLiteDatabase db;
    private List<DictionaryBean> list;
    private DictAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        String dbName = getFilesDir().getAbsolutePath() + "/dictionary.db";
        db = FileUtil.openDatabase(this, dbName);
        initView();
        setUpData();
    }

    /**
     * 获取数据绑定到控件上
     */
    private void setUpData() {
        Cursor c = db.query("t_words", null, null, null, null, null, null, null);
        list = new ArrayList<>();
        while (c.moveToNext()) {
            DictionaryBean bean = new DictionaryBean(c.getString(0), c.getString(1));
            list.add(bean);
        }
        c.close();
        adapter = new DictAdapter();
        lv.setAdapter(adapter);
    }

    @Override
    public void initView() {
        lv = (ListView) findViewById(R.id.act_dict_lv);
    }


    class DictAdapter extends BaseAdapter {

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
            ViewHolder vh = null;
            if (v == null) {
                v = View.inflate(DictionaryActivity.this, R.layout.item_dictionary, null);
                vh = new ViewHolder();
                vh.english = (TextView) v.findViewById(R.id.item_dict_english);
                vh.chinese = (TextView) v.findViewById(R.id.item_dict_chinese);
                v.setTag(vh);
            } else {
                vh = (ViewHolder) v.getTag();
            }
            vh.english.setText(list.get(position).getEnglish());
            vh.chinese.setText(list.get(position).getChinese());
            return v;
        }

        class ViewHolder {
            TextView english, chinese;
        }
    }
}
