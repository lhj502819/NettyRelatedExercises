package cn.onenine.netty.intermediate.future.future;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SyncWriteMap {

    public static Map<String, WriteFuture> syncKey = new ConcurrentHashMap<String, WriteFuture>();

}