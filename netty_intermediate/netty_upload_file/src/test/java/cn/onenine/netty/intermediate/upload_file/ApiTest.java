package cn.onenine.netty.intermediate.upload_file;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.math.BigInteger;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/11 12:44
 */
public class ApiTest {

    @Test
    public void testRandomAccessFile() throws Exception {

        String fileUrl = "E:\\workspaces\\uploadFile\\test.txt";
        RandomAccessFile randomAccessFile = new RandomAccessFile(new File(fileUrl), "r");
        randomAccessFile.seek(0);

        byte[] bytes = new byte[1024];
        int readByte = randomAccessFile.read(bytes);
        System.out.println(fileUrl);
        System.out.println("读取文件长度：" + readByte);
        for (byte b : bytes) {
            System.out.print(new BigInteger(1, new byte[]{b}).toString(16) + " ");
        }
        System.out.println("\r\n");
    }

}
