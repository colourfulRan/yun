package cqjtu.ds.yun.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="file")
@Getter
@Setter
@ToString
public class DomainFile {

    @Id
    private String fileId;

    private int typeId;

    private int userId;

    private String fileName;

    private String filePath;

    //上传日期
    private Date updateDate;

    private int fileSize;

    private String secretKey;

    private  boolean isDel;

    private Date delDate;


}
