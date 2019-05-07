package cqjtu.ds.yun.service.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "admin")
@Getter
@Setter

public class Admin  {
    @Id//主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自定义生成策略（自增策略）

    //可加入bean验证

    private Integer adminId;
    private String adminname;
    private String password;
    private String sex;
    private String photo;
    private  String  email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", adminname='" + adminname + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", photo='" + photo + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
