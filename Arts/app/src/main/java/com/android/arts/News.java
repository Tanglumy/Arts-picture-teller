package com.android.arts;

import java.io.Serializable;


public class News implements Serializable {
    private Integer id;//id
    private String title;//标题
    private String img;//新闻
    private String content;//内容
    private String date;//时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public News(Integer id, String title, String img, String content, String date) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.content = content;
        this.date = date;
    }
}
