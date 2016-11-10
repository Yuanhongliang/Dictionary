package com.adm.dictionary.http;

import com.adm.dictionary.bean.ChineseBean;
import com.adm.dictionary.bean.EnglishBean;
import com.adm.dictionary.bean.SentenceBean;
import com.adm.dictionary.bean.EveryDayWords;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


/**
 * 所有的网络请求方法
 * Created by Administrator on 2016/10/14.
 */
public interface ApiService {
    /**
     * 查询句子的方法
     * @param keyfrom 申请值
     * @param key 申请值
     * @param type 固定为data
     * @param doctype json/xml
     * @param version 固定为1.1
     * @param q 查询句子
     * @return 句子Bean
     */
    @GET("/openapi.do")
    Observable<SentenceBean> sentenceTransLate(@Query("keyfrom") String keyfrom, @Query("key") String key, @Query("type") String type, @Query("doctype")
    String doctype, @Query("version") String version, @Query("q") String q);

    /**
     * 查询每日一句
     * @return 每日一句信息
     */
    @GET("/dsapi")
    Observable<EveryDayWords> getEveryDayWords();

    /**
     * 查询英文
     * @param w 单词
     * @param key 申请的key
     * @param type json/xml
     * @return 英文bean
     */
    @GET("/api/dictionary.php")
    Observable<EnglishBean> englishSearch(@Query("w") String w,@Query("key") String key,@Query("type") String type);

    /**
     * 查询中文
     * @param w 单词
     * @param key 申请的key
     * @param type json/xml
     * @return 中文Bean
     */
    @GET("/api/dictionary.php")
    Observable<ChineseBean> chineseSearch(@Query("w") String w, @Query("key") String key, @Query("type") String type);

}
