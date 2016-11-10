package com.adm.dictionary.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adm.dictionary.base.BaseFragment;

/**
 * Created by Administrator on 2016/10/18.
 */
public class WoDeFragment extends BaseFragment {

    private View v;
    private TextView frag_my_danciben, frag_my_plan, frag_my_dict;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_wode, null);
        initView();
        return v;
    }

    @Override
    public void initView() {
        frag_my_danciben = findTextViewbyId(v, R.id.frag_my_danciben);
        frag_my_plan = findTextViewbyId(v, R.id.frag_my_plan);
        frag_my_dict = findTextViewbyId(v, R.id.frag_my_dict);
        frag_my_danciben.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DanCiBenActivity.class));
            }
        });
        frag_my_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PlanActivity.class));
            }
        });
        frag_my_dict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DictionaryActivity.class));
            }
        });
    }
}
