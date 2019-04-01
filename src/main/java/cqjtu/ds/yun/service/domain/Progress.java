package cqjtu.ds.yun.service.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * 进度条
 */
public class Progress {
    private long bytesRead;
    private long contentLength;
    private long items;
}
