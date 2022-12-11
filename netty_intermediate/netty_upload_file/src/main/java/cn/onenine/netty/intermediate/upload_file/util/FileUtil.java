package cn.onenine.netty.intermediate.upload_file.util;

import cn.onenine.netty.intermediate.upload_file.constant.Constants;
import cn.onenine.netty.intermediate.upload_file.domain.FileBurstData;
import cn.onenine.netty.intermediate.upload_file.domain.FileBurstInstruct;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Description：文件读写工具
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/11 11:20
 */
public class FileUtil {

    public static FileBurstData readFile(String fileUrl, Integer readPosition) throws Exception {
        File file = new File(fileUrl);
        //r:只读模式  rw:读写模式
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        randomAccessFile.seek(readPosition);
        byte[] bytes = new byte[1024];
        int readSize = randomAccessFile.read(bytes);
        if (readSize <= 0) {
            randomAccessFile.close();
            return new FileBurstData(Constants.FileStatus.COMPLETE);
        }
        FileBurstData fileInfo = new FileBurstData();
        fileInfo.setFileUrl(fileUrl);
        fileInfo.setFileName(file.getName());
        fileInfo.setBeginPos(readPosition);
        fileInfo.setEndPos(readPosition + readSize);
        //如果readSize不是1024,则需要去掉空字符
        if (readSize < 1024) {
            byte[] copy = new byte[readSize];
            System.arraycopy(bytes, 0, copy, 0, readSize);
            fileInfo.setBytes(copy);
            fileInfo.setStatus(Constants.FileStatus.END);
        }else {
            fileInfo.setBytes(bytes);
            fileInfo.setStatus(Constants.FileStatus.CENTER);
        }
        randomAccessFile.close();
        return fileInfo;
    }

    public static FileBurstInstruct writeFile(String baseUrl, FileBurstData fileBurstData) throws Exception{
        if (Constants.FileStatus.COMPLETE == fileBurstData.getStatus()) {
            return new FileBurstInstruct(Constants.FileStatus.COMPLETE);
        }
        File file = new File(baseUrl + "/" + fileBurstData.getFileName());
        //r:只读模式  rw:读写模式
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        //移动文件记录指针的位置，程序从这个位置开始读/写
        randomAccessFile.seek(fileBurstData.getBeginPos());
        randomAccessFile.write(fileBurstData.getBytes());
        randomAccessFile.close();

        if (Constants.FileStatus.END == fileBurstData.getStatus()) {
            return new FileBurstInstruct(Constants.FileStatus.COMPLETE);
        }

        //文件分片传输指令
        FileBurstInstruct fileBurstInstruct = new FileBurstInstruct();
        fileBurstInstruct.setStatus(Constants.FileStatus.CENTER);
        //客户端文件URL
        fileBurstInstruct.setClientFileUrl(fileBurstData.getFileUrl());
        //读取位置
        fileBurstInstruct.setReadPosition(fileBurstData.getEndPos() + 1);
        return fileBurstInstruct;
    }

}
