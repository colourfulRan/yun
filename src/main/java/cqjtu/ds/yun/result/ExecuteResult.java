package cqjtu.ds.yun.result;

//统一处理返回对象；封装返回的对象

public class ExecuteResult<T> {

    //执行是否成功
    private boolean isExecuted;
    //操作是否成功
    private boolean isSuccess;
    //结果码
    private String code;

    //处理后的消息提示
    private String message;

    //结果数据
    private T data;

    public boolean isExecuted() {
        return isExecuted;
    }

    public void setExecuted(boolean executed) {
        isExecuted = executed;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ExecuteResult{" +
                "isExecuted=" + isExecuted +
                ", isSuccess=" + isSuccess +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
