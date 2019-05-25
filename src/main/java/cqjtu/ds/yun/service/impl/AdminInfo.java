package cqjtu.ds.yun.service.impl;

import java.io.Serializable;

public class AdminInfo {
    private int adminId;
    private String adminname;
    private String  nickname;

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "AdminInfo{" +
                "adminId=" + adminId +
                ", adminname='" + adminname + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
