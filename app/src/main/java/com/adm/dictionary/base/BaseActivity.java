package com.adm.dictionary.base;

import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/10/18.
 */
public abstract class BaseActivity extends FragmentActivity {

    public TextView findTextViewById(int id) {
        return (TextView) findViewById(id);
    }

    public ImageView findImageViewById(int id) {
        return (ImageView) findViewById(id);
    }

    public LinearLayout findLinById(int id) {
        return (LinearLayout) findViewById(id);
    }

    public EditText findEtnById(int id) {
        return (EditText) findViewById(id);
    }

    public Button findButById(int id) {
        return (Button) findViewById(id);
    }

    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public abstract void initView();

}
