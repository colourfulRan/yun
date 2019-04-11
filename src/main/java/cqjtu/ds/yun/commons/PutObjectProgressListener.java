package cqjtu.ds.yun.commons;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import lombok.Getter;

import javax.servlet.http.HttpSession;

public class PutObjectProgressListener implements ProgressListener{
    private HttpSession session;
    private long bytesWritten=0;
    private long uploadedSize;
    private long fileSize;
    private long totalBytes;
    @Getter
    private boolean succeed=false;
    private  int percent=0;


    public PutObjectProgressListener(){}

    //构造方法中加入session
    public PutObjectProgressListener(HttpSession session,long uploadedSize,long fileSize){
        this.session=session;
        this.uploadedSize=uploadedSize;
        this.fileSize=fileSize;
        session.setAttribute("upload_percent",0);
        session.setAttribute("bytesWritten",0);
        session.setAttribute("totalBytes",0);
   //     System.out.println("========================================");
    }
    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes=progressEvent.getBytes();
        ProgressEventType eventType=progressEvent.getEventType();
        switch (eventType){
            case TRANSFER_STARTED_EVENT:break;
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes=bytes;
                break;
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten+=bytes;
                this.uploadedSize+=bytesWritten;
                if(this.totalBytes!=-1){
                    percent= (int) (this.uploadedSize*100.0/this.fileSize);
                    //将进度percent放入session
                    session.setAttribute("upload_percent",percent);
                    session.setAttribute("uploadedSize",uploadedSize);
                    session.setAttribute("fileSize",fileSize/(1024*1024));
              //      System.out.println(percent+"  "+uploadedSize/(1024*1024)+" "+fileSize/(1024*1024));
                }
                break;
            case TRANSFER_COMPLETED_EVENT:
                this.succeed=true;
                break;
            case TRANSFER_FAILED_EVENT:break;
            default:break;
        }
       // System.out.println("percent:"+percent);
    }
}
