package com.adm.dictionary.bean;

/**
 * 本地词库的Bean
 * Created by Administrator on 2016/10/28.
 */
public class DictionaryBean {
    private String english;
    private String chinese;

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public DictionaryBean(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
    }

    public DictionaryBean() {
    }

    @Override
    public String toString() {
        return "DictionaryBean{" +
                "english='" + english + '\'' +
                ", chinese='" + chinese + '\'' +
                '}';
    }
}
