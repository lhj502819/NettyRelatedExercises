package cn.onenine.netty.intermediate.future.msg;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/13 21:39
 */
public class Response {

    private String requestId;

    private String param;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
