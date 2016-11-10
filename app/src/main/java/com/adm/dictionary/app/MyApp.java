package com.adm.dictionary.app;

import android.app.Application;

import com.adm.dictionary.dao.DaoMaster;
import com.adm.dictionary.dao.DaoSession;

import org.greenrobot.greendao.database.Database;


/**
 * APP入口
 * Created by Administrator on 2016/10/24.
 */
public class MyApp extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "word_db" );
        Database db = helper.getReadableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession(){
        return  daoSession;
    }
}
