package com.example.uipractice;

import java.io.Serializable;

public class Directory implements Serializable {
    static final long serialVersionUID = 727566175075960653L;
    private int id;
    private String title;
    //    private String description;
//    private String bgImageUrl;
    private String cardImageUrl;
    private String videoUrl;
    private int pk;
    private String dir_name;
    private String dir_type;

    public void setPk(int pk) {
        this.pk = pk;
    }

    public void setDir_name(String dir_name) {
        this.dir_name = dir_name;
    }

    public void setDir_type(String dir_type) {
        this.dir_type = dir_type;
    }

    public int getPk() {
        return pk;
    }

    public String getDir_name() {
        return dir_name;
    }

    public String getDir_type() {
        return this.dir_type;
    }



    public Directory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public String getDescription() {
//        return description;
//    }

//    public void setDescription(String description) {
//        this.description = description;
//    }

//    public String getStudio() {
//        return studio;
//    }

//    public void setStudio(String studio) {
//        this.studio = studio;
//    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

//    public String getBackgroundImageUrl() {
//        return bgImageUrl;
//    }

//    public void setBackgroundImageUrl(String bgImageUrl) {
//        this.bgImageUrl = bgImageUrl;
//    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public void setCardImageUrl(String cardImageUrl) {
        this.cardImageUrl = cardImageUrl;
    }

    @Override
    public String toString() {
        return "Directory{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", cardImageUrl='" + cardImageUrl + '\'' +
                ", dirType='" + dir_type + '\'' +
                '}';
    }
}
