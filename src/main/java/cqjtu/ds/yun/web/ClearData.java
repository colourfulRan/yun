package cqjtu.ds.yun.web;
import cqjtu.ds.yun.service.FileService;
import cqjtu.ds.yun.service.domain.DomainFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Component
public class ClearData
{
    private Logger logger = LoggerFactory.getLogger(ClearData.class);
   @Autowired
    private FileService fileService;


    //每五分钟触发一次
    @Scheduled(cron = "0 0/1 * * * ? ")
   public void DelRecycle()
   {
       //logger.info("---------定时任务开始执行---------"+new Timestamp(System.currentTimeMillis()));

       List<DomainFile> listAll=fileService.findAllbyisdel(true);
       //SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
       if (listAll != null)
       {
           Date curDate =new Date(System.currentTimeMillis());//获取当前时间
           //logger.info("当前时间："+curDate.getTime());
           for (DomainFile file: listAll)
           {
               //logger.info("获取时间："+file.getDelDate().getTime());
               long yy = curDate.getTime() - file.getDelDate().getTime();
                long d=yy/1000/60;//相差多少分钟
              // logger.info("相差多少分钟:"+d);
               if(d>=30)
               {
                   fileService.RemoveFile(file.getFileId());
               }
               else
               {
                   DomainFile oldfile=fileService.findbyid(file.getFileId());
                  // oldfile.setDel(true);
                   oldfile.setFileValid((int) (30-d));
                   fileService.SaveFile(oldfile);
               }
           }
           }
   }
}
