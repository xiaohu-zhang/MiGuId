package com.cmcc.MiGuId.Server.handler;

import com.cmcc.MiGuId.Main;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class GeneratorHandler extends SimpleChannelInboundHandler<ByteBuf> {
    
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // TODO Auto-generated method stub
        int numbytes = msg.readableBytes();
        if(numbytes != 9){
            throw new RuntimeException("do not supported");
        }else{
            if(msg.readByte() == 'c'){
                ByteBuf b = Unpooled.buffer(22);
                b.writeCharSequence(Main.getG().generate(),CharsetUtil.UTF_8);
                ctx.write(Unpooled.buffer(1).writeByte(30));
                ctx.write(msg.copy());
                ctx.writeAndFlush(b);
            }
        }
    }

//    private void X(ChannelHandlerContext ctx) {
//        if(m == 0){
//            m++;
//            Thread x1 = new Thread(()->{
//                b.retain();
//                b.writeCharSequence(Main.getG().generate(),CharsetUtil.UTF_8);
//                final CountDownLatch notified2 = new CountDownLatch(1);
//                System.out.println(notified2.hashCode());
//                ctx.writeAndFlush(b);
//                b.clear();
//            });
//            x1.start();
//        }else{
//            Thread x1 = new Thread(()->{
//                b.retain();
//                b.writeCharSequence(/*Main.getG().generate()*/"2\n",CharsetUtil.UTF_8);
//                ctx.writeAndFlush(b);
//                b.clear();
//            });
//            x1.start();
//        }
//
//    }
    
}
