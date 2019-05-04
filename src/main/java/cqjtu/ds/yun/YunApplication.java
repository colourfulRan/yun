package cqjtu.ds.yun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.MultipartResolver;

import javax.servlet.MultipartConfigElement;

/*@EnableDubboConfiguration*/
@SpringBootApplication
@EnableScheduling    //开启定时器调度任务
public class YunApplication {

	public static void main(String[] args) {
		SpringApplication.run(YunApplication.class, args);
	}


	/**
	 * 配置上传文件大小
	 * @return
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement(){
		MultipartConfigFactory config=new MultipartConfigFactory();
		config.setMaxFileSize("9000MB");
		config.setMaxRequestSize("9000MB");
		return config.createMultipartConfig();
	}
}
