package cqjtu.ds.yun.service.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="user")
@Getter
@Setter
public class User implements Serializable {

    @Id//主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自定义生成策略（自增策略）

    //可加入bean验证

    private Integer userId;
    private String username;
    private String password;
    private String photo;
    private Date birthday;
    private String sex;
    private String blood_type;
    private String birth_place;
    private String live_place;


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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
