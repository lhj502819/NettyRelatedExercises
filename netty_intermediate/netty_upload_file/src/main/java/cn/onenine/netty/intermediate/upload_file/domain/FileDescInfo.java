package cn.onenine.netty.intermediate.upload_file.domain;

/**
 * Description：文件信息
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/11 12:00
 */
public class FileDescInfo {

    private String fileUrl;

    private String fileName;

    private Long fileSize;

    public FileDescInfo(String fileUrl, String fileName, Long fileSize) {
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
