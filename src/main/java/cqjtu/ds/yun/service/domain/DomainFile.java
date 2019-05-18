package cqjtu.ds.yun.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


@Entity
@Table(name="file")
@Getter
@Setter
@ToString
public class DomainFile
{
    @Id
    private int fileId;//数据库中fileidw唯一！！！！

    private int parentId;

    private String fileHash;

    private int typeId;

    private int userId;

    private String fileName;

    private String filePath;

    //上传日期
    private Timestamp  updateDate;

    private int fileSize;

    private String secretKey;

    //
    private  int isDel;

    private Timestamp delDate;

    private int fileValid;

    private Timestamp recentDate;


    public int getFileId()
    {
        return fileId;
    }

    public void setFileId(int fileId)
    {
        this.fileId = fileId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId)
    {
        this.parentId = parentId;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash)
    {
        this.fileHash = fileHash;
    }

    public Timestamp getRecentDate() {
        return recentDate;
    }

    public void setRecentDate(Timestamp recentDate) {
        this.recentDate = recentDate;
    }

    public int getFileValid() {
        return fileValid;
    }

    public void setFileValid(int fileValid) {
        this.fileValid = fileValid;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public Timestamp getDelDate() {
        return delDate;
    }

    public void setDelDate(Timestamp delDate) {
        this.delDate = delDate;
    }
}
