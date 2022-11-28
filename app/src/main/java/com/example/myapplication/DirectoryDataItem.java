package com.example.myapplication;

public class DirectoryDataItem {
    String dir_name;
    String dir_type;
    int pk;

    public DirectoryDataItem(String dir_name, String dir_type, int pk) {
        this.dir_name = dir_name;
        this.dir_type = dir_type;
        this.pk = pk;
    }

    public String getDir_name() {
        return dir_name;
    }

    public void setDir_name(String dir_name) {
        this.dir_name = dir_name;
    }

    public String getDir_type() {
        return dir_type;
    }

    public void setDir_type(String dir_type) {
        this.dir_type = dir_type;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }
}
