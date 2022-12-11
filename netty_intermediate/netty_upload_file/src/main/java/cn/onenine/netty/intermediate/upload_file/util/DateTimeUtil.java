package cn.onenine.netty.intermediate.upload_file.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.LocalDateTime;

/**
 * Description：日期时间工具类
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/6 23:00
 */
public class DateTimeUtil {
    
    public static String getNowStr(){
        return LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_PATTERN);
    }
    
}
