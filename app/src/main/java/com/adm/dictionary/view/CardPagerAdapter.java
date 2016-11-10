package com.adm.dictionary.view;

import android.media.MediaPlayer;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adm.dictionary.bean.WordBean;
import com.adm.dictionary.dictionary.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<WordBean> mData;
    private float mBaseElevation;
    private MediaPlayer mp;

    public CardPagerAdapter(List<WordBean> mData) {
        this.mData = mData;
        mViews = new ArrayList<>();
        mp = new MediaPlayer();
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        for (int i = 0; i < mData.size(); i++) {
            mViews.add(null);
        }
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter, container, false);
        container.addView(view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }
        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        initData(view, position);
        mViews.set(position, cardView);
        return view;
    }

    /**
     * 讲数据添加到控件上
     *
     * @param view
     * @param position
     */
    private void initData(View view, final int position) {
        TextView title = (TextView) view.findViewById(R.id.item_prac_name);
        TextView ukduyin = (TextView) view.findViewById(R.id.item_prac_ukduyin);
        TextView usduyin = (TextView) view.findViewById(R.id.item_prac_usduyin);
        Button ukvoice = (Button) view.findViewById(R.id.item_prac_ukvoice);
        Button usvoice = (Button) view.findViewById(R.id.item_prac_usvoice);
        final TextView but = (TextView) view.findViewById(R.id.item_prac_but);
        final TextView meaning = (TextView) view.findViewById(R.id.item_prac_meaning);
        title.setText(mData.get(position).getName());
        ukduyin.setText("[美]" + mData.get(position).getEn_sympol());
        usduyin.setText("[英]" + mData.get(position).getAm_symbol());
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mData.get(position).getParts().size(); i++) {
            builder.append(mData.get(position).getParts().get(i).getPart()).append("  ").append(mData.get(position).getParts().get(i).getMeans()).append("\r\n");
        }
        meaning.setText(builder.toString());
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but.setVisibility(View.INVISIBLE);
                meaning.setVisibility(View.VISIBLE);
            }
        });
        ukvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.get(position).getEn_mp3() == null || mData.get(position).getEn_mp3().equals("")) {
                    return;
                }
                mp.reset();
                try {
                    mp.setDataSource(mData.get(position).getEn_mp3());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.prepareAsync();
            }
        });
        usvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.get(position).getAm_mp3() == null || mData.get(position).getAm_mp3().equals("")) {
                    return;
                }
                mp.reset();
                try {
                    mp.setDataSource(mData.get(position).getAm_mp3());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.prepareAsync();
            }
        });
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }


    public void releaseMp() {
        if (mp.isPlaying()) {
            mp.stop();
        }
        mp.release();
    }

}
