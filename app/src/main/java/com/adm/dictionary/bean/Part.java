package com.adm.dictionary.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2016/10/24.
 */
@Entity
public class Part {
    @Id
    private Long id;
    private Long wordId;
    private String part;
    private String means;

    public Long getWordId() {
        return wordId;
    }

    public void setWordId(Long wordId) {
        this.wordId = wordId;
    }

    public Part(String part, String means) {
        this.part = part;
        this.means = means;
    }


    @Generated(hash = 1811726784)
    public Part(Long id, Long wordId, String part, String means) {
        this.id = id;
        this.wordId = wordId;
        this.part = part;
        this.means = means;
    }

    @Generated(hash = 130301790)
    public Part() {
    }


    public String getMeans() {
        return this.means;
    }

    public void setMeans(String means) {
        this.means = means;
    }

    public String getPart() {
        return this.part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Part{" +
                "id=" + id +
                ", part='" + part + '\'' +
                ", means='" + means + '\'' +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }
}