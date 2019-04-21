package cqjtu.ds.yun.service.domain;

import javax.persistence.*;
import java.util.Date;


@Entity
public class File
{
    @Id//主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自定义生成策略（自增策略）

    @Column(name="file_id")
    private String fileid;

    @Column(name="file_name")
    private String filename;

    @Column(name="file_path")
    private String filepath;

    @Column(name="secret_key")
    private String secretkey;//密钥？？

    @Column(name="file_size")
    private Integer filesize;

    @Column(name="user_id")
    private Integer userid;

    @Column(name="type_id")
    private Integer typeid;

    @Column(name="is_del")
    private Integer isdel;//0未删除，1为放入回收站

    @Column(name="update_date")
    private Date updatedate;

    @Column(name="del_date")
    private Date deldate;


    //无参构造函数
    public File()
    { }

    public Integer getFilesize() {
        return filesize;
    }

    public void setFilesize(Integer filesize) {
        this.filesize = filesize;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public Integer getIsdelete() {
        return isdel;
    }

    public void setIsdelete(Integer isdel) {
        this.isdel = isdel;
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }


    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public Date getDeldate() {
        return deldate;
    }

    public void setDeldate(Date deldate) {
        this.deldate = deldate;
    }

    @Override
    public String toString() {
        return "File{" +
                "fileid='" + fileid + '\'' +
                ", filename='" + filename + '\'' +
                ", filepath='" + filepath + '\'' +
                ", secretkey='" + secretkey + '\'' +
                ", filesize=" + filesize +
                ", userid=" + userid +
                ", typeid=" + typeid +
                ", isdelete=" + isdel +
                ", updatedate=" + updatedate +
                ", deldate=" + deldate +
                '}';
    }
}
