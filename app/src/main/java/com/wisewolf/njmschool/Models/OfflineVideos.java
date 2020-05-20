package com.wisewolf.njmschool.Models;

public class OfflineVideos {
    private String name;
    private String salt;
    private String dir;
    private String inv;
    private String locat;
    private String userid;
    private String extra1;
    private String extra2;

    public OfflineVideos(String name, String salt, String dir, String inv, String locat, String userid, String extra1, String extra2) {
        this.name = name;
        this.salt = salt;
        this.dir = dir;
        this.inv = inv;
        this.locat = locat;
        this.userid = userid;
        this.extra1 = extra1;
        this.extra2 = extra2;
    }

    public String getName() {
        return name;
    }

    public String getSalt() {
        return salt;
    }

    public String getDir() {
        return dir;
    }

    public String getInv() {
        return inv;
    }

    public String getLocat() {
        return locat;
    }

    public String getUserid() {
        return userid;
    }

    public String getExtra1() {
        return extra1;
    }

    public String getExtra2() {
        return extra2;
    }
}
