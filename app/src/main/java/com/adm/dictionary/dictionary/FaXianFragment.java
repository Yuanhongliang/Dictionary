package com.adm.dictionary.dictionary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.adm.dictionary.base.BaseFragment;
import com.adm.dictionary.util.SharedPreUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现Fragment
 * Created by Administrator on 2016/10/18.
 */
public class FaXianFragment extends BaseFragment {


    private static String HISTORY_KEY = "history";
    private View v;
    private EditText et;
    private TextView tv;
    private GridView gv;
    private List<String> histories = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Button clear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_faxian, null);
        initView();
        getHistory();
        return v;
    }

    /**
     * 获取搜索历史
     */
    private void getHistory() {
        List<String> data = SharedPreUtil.getList(getActivity(), "history");
        if (data != null) {
            histories.addAll(data);
        }
    }

    @Override
    public void initView() {
        clear = findButById(v, R.id.frag_fa_clear);
        et = findEtbyId(v, R.id.frag_fa_et);
        tv = findTextViewbyId(v, R.id.frag_fa_tv);
        gv = (GridView) v.findViewById(R.id.frag_fa_gv);

        /**
         * 清空历史
         */
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (histories == null || histories.size() == 0) {
                    Toast.makeText(getActivity(), "暂无搜索历史", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreUtil.clearHistory(getActivity());
                histories.clear();
                adapter.notifyDataSetChanged();
            }
        });

        /**
         * 搜索事件
         */
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchStr = et.getText().toString().trim();
                    search(searchStr);
                }
                return false;
            }
        });
        /**
         * 长句翻译
         */
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SentenceTranslateActivity.class));
            }
        });
        adapter = new ArrayAdapter<>(getActivity(), R.layout.text_his, histories);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                search(histories.get(position));
            }
        });
    }

    private void search(String searchStr) {
        if (searchStr.equals("")) {
            showToast("请输入文本");
            return;
        }
        if (searchStr.contains(" ")) {
            showToast("搜索词中不允许空格哦~");
            return;
        }
        if (histories.contains(searchStr)) {
            histories.remove(searchStr);
            histories.add(searchStr);
        } else {
            histories.add(searchStr);
        }
        adapter.notifyDataSetChanged();
        SharedPreUtil.saveHistory(getActivity(), HISTORY_KEY, histories);
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        et.setText("");
        Intent intent = new Intent();
        intent.putExtra("searchStr", searchStr);
        int length = (searchStr.charAt(0) + "").getBytes().length;
        if (length == 1) {
            intent.setClass(getActivity(), EnglishSearchActivity.class);
        } else if (length == 3) {
            intent.setClass(getActivity(), ChineseSearchActivity.class);
        }
        startActivity(intent);
    }
}
