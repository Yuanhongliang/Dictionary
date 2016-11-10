package com.adm.dictionary.http;

import com.adm.dictionary.bean.ChineseBean;
import com.adm.dictionary.bean.EnglishBean;
import com.adm.dictionary.bean.SentenceBean;
import com.adm.dictionary.bean.EveryDayWords;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/14.
 */
public class HttpMethods {

    private static HttpMethods instance;

    /**
     * 翻译句子
     */
    private ApiService service1;
    /**
     * 每日一句
     */
    private ApiService service2;
    /**
     * 翻译单词
     */
    private ApiService service3;


    private HttpMethods() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();
        Retrofit retrofit1 = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://fanyi.youdao.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Retrofit retrofit2 = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://open.iciba.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Retrofit retrofit3 = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://dict-co.iciba.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service1 = retrofit1.create(ApiService.class);
        service2 = retrofit2.create(ApiService.class);
        service3 = retrofit3.create(ApiService.class);
    }

    public static HttpMethods getInstance() {
        if (instance == null) {
            synchronized (HttpMethods.class) {
                if (instance == null) {
                    instance = new HttpMethods();
                }
            }
        }
        return instance;
    }

    public Observable<SentenceBean> sentenceTransLate(String q) {
        return service1.sentenceTransLate(Constants.KEYFROM, Constants.KEY, "data", "json", "1.1", q);
    }
    public Observable<EveryDayWords> getEveryDayWords(){
        return  service2.getEveryDayWords();
    }

    public Observable<EnglishBean> searchEnglish(String w){
        return service3.englishSearch(w,Constants.CIBAKEY,"json");
    }
    public Observable<ChineseBean> searchChinese(String w){
        return service3.chineseSearch(w,Constants.CIBAKEY,"json");
    }

}
