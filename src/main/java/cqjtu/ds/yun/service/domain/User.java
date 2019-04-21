package cqjtu.ds.yun.service.domain;

import javax.persistence.*;


@Entity//标注user是个实体
//@Table(name="user")
public class User {

    @Id//主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自定义生成策略（自增策略）

    //可加入bean验证

    @Column(name="user_id")
    private Integer userid;
    private String username;
    private String password;
    private String photo;

    //无参构造函数
    public User()
    { }

    public Integer getUserid()
    { return userid; }

    public void setUserid(Integer userid)
    { this.userid = userid; }

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

    @Override
    public String toString() {
        return "User{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
