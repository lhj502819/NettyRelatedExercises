package cn.onenine.netty.intermediate.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/10 22:34
 */
public class ObjEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    public ObjEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (genericClass.isInstance(msg)){
            byte[] data = SerializationUtil.serialize(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
