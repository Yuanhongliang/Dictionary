package com.adm.dictionary.dictionary;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adm.dictionary.base.BaseFragment;
import com.adm.dictionary.bean.EveryDayWords;
import com.adm.dictionary.http.HttpMethods;
import com.adm.dictionary.util.HttpUtil;
import com.bumptech.glide.Glide;

import java.io.IOException;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 推荐Fragment
 * Created by Administrator on 2016/10/18.
 */
public class TuijianFragment extends BaseFragment {

    private View v;
    private ImageView img, voice;
    private TextView content1, content2, des;
    private MediaPlayer mp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_tuijian, null);
        initView();
        getData();
        return v;
    }

    private void getData() {
        if(!HttpUtil.isNetworkAvailable(getActivity())){
            showToast("当前网络不可用");
            voice.setVisibility(View.GONE);
            return;
        }

        HttpMethods.getInstance().getEveryDayWords().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<EveryDayWords>() {
            @Override
            public void call(final EveryDayWords everyDayWords) {
                content1.setText(everyDayWords.getContent());
                content2.setText(everyDayWords.getNote());
                des.setText("      " + everyDayWords.getTranslation());
                Glide.with(getActivity()).load(everyDayWords.getPicture2()).into(img);
                voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mp.reset();
                        try {
                            mp.setDataSource(everyDayWords.getTts());
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
        img = findImageViewbyId(v, R.id.frag_tui_img);
        voice = findImageViewbyId(v, R.id.frag_tui_voice);
        content1 = findTextViewbyId(v, R.id.frag_tui_content1);
        content2 = findTextViewbyId(v, R.id.frag_tui_content2);
        des = findTextViewbyId(v, R.id.frag_tui_des);
        mp = new MediaPlayer();

        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp.isPlaying()) {
            mp.stop();
        }
        mp.release();
    }
}
