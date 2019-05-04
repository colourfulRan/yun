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
public class DomainFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自定义生成策略（自增策略）
    private Integer fileId;

    private Integer parentId;

    private String fileHash;

    private int typeId;

    private int userId;

    private String fileName;

    private String filePath;

    //上传日期
    private Timestamp updateDate;

    private int fileSize;

    private String secretKey;

    private  boolean isDel;

    private Timestamp  delDate;

    private Integer fileValid;


}
