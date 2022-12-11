package cn.onenine.netty.intermediate.upload_file.domain;

/**
 * Description：文件分片指令
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/11 11:10
 */
public class FileBurstInstruct {

    /**
     * Constants.FileStatus{0开始、1中间、2结尾、3完成}
     */
    private Integer status;
    /**
     * 客户端文件URL
     */
    private String clientFileUrl;
    /**
     * 读取位置
     */
    private Integer readPosition;

    public FileBurstInstruct() {
    }

    public FileBurstInstruct(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getClientFileUrl() {
        return clientFileUrl;
    }

    public void setClientFileUrl(String clientFileUrl) {
        this.clientFileUrl = clientFileUrl;
    }

    public Integer getReadPosition() {
        return readPosition;
    }

    public void setReadPosition(Integer readPosition) {
        this.readPosition = readPosition;
    }
}
