package cqjtu.ds.yun.service.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="user")
@Getter
@Setter
public class User {

    @Id//主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自定义生成策略（自增策略）

    //可加入bean验证

    private Integer userId;
    private String username;
    private String password;
    private String sex;
    private String bloodType;
    private String livePlace;
    private String brithPlace;
    private Date brithday;
    private String photo;




    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getLivePlace() {
        return livePlace;
    }

    public void setLivePlace(String livePlace) {
        this.livePlace = livePlace;
    }

    public String getBrithPlace() {
        return brithPlace;
    }

    public void setBrithPlace(String brithPlace) {
        this.brithPlace = brithPlace;
    }

    public Date getBrithday() {
        return brithday;
    }

    public void setBrithday(Date brithday) {
        this.brithday = brithday;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", livePlace='" + livePlace + '\'' +
                ", brithPlace='" + brithPlace + '\'' +
                ", brithday=" + brithday +
                ", photo='" + photo + '\'' +
                '}';
    }
}

