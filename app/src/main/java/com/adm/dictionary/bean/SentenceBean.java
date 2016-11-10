package com.adm.dictionary.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 查询句子的返回的Bean
 * Created by Administrator on 2016/10/17.
 */
public class SentenceBean {

    private List<String> translation;
    private Basic basic;
    private String query;
    private String errorCode;
    private List<Web> web;

    public List<Web> getWeb() {
        return web;
    }

    public void setWeb(List<Web> web) {
        this.web = web;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "SentenceBean{" +
                "translation=" + translation +
                ", basic=" + basic +
                ", query='" + query + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", web=" + web +
                '}';
    }

    class Basic {
        @SerializedName("us-phonetic") private String us_phonetic;
        private String phonetic;
        @SerializedName("uk-phonetic") private String uk_phonetic;
        private List<String> explains;

        public String getUs_phonetic() {
            return us_phonetic;
        }

        public void setUs_phonetic(String us_phonetic) {
            this.us_phonetic = us_phonetic;
        }

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        public String getUk_phonetic() {
            return uk_phonetic;
        }

        public void setUk_phonetic(String uk_phonetic) {
            this.uk_phonetic = uk_phonetic;
        }

        public List<String> getExplains() {
            return explains;
        }

        public void setExplains(List<String> explains) {
            this.explains = explains;
        }

        @Override
        public String toString() {
            return "Basic{" +
                    "us_phonetic='" + us_phonetic + '\'' +
                    ", phonetic='" + phonetic + '\'' +
                    ", uk_phonetic='" + uk_phonetic + '\'' +
                    ", explains=" + explains +
                    '}';
        }
    }

    class Web{
        private String key;
        private List<String> value;

        @Override
        public String toString() {
            return "Web{" +
                    "key='" + key + '\'' +
                    ", value=" + value +
                    '}';
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }


}
