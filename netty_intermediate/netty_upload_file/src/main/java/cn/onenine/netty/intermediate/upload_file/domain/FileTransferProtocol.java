package cn.onenine.netty.intermediate.upload_file.domain;

/**
 * Description：文件传输协议
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/11 11:03
 */
public class FileTransferProtocol {

    /**
     * 0：请求传输文件、1：文件传输指令、2：文件传输数据
     */
    private Integer transferType;
    /**
     * 数据对象；(0)FileDescInfo、(1)FileBurstInstruct、(2)FileBurstData
     */
    private Object transferObj;

    public Integer getTransferType() {
        return transferType;
    }

    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
    }

    public Object getTransferObj() {
        return transferObj;
    }

    public void setTransferObj(Object transferObj) {
        this.transferObj = transferObj;
    }
}
