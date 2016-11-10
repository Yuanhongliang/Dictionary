package com.adm.dictionary.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/10/17.
 */
public class FileUtil {

    public static SQLiteDatabase openDatabase(Context context,String dbfile) {
        try {
            File file = new File(dbfile);
            if (!file.exists()) {
                InputStream is = context.getAssets().open("dictionary.db");
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[1024];
                int count = -1;
                while ((count = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                    fos.flush();
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
            return db;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
