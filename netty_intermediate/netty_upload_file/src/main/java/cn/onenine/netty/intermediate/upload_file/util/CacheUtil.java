
package cn.onenine.netty.intermediate.upload_file.util;

import cn.onenine.netty.intermediate.upload_file.domain.FileBurstInstruct;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheUtil {

    public static Map<String, FileBurstInstruct> burstDataMap = new ConcurrentHashMap<>();

}