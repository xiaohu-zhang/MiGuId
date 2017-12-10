package com.cmccmMiGuId.Client.handler;

import com.cmcc.Client;
import com.cmccmMiGuId.Client.ResponseModel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

public class ClientInboundHandler extends SimpleChannelInboundHandler<ByteBuf>{
    


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // TODO Auto-generated method stub
        Long clientId = msg.readLong();
        String recieveId = msg.toString(CharsetUtil.UTF_8);
        if(recieveId.length() != 22){
            throw new RuntimeException();
        }
        ResponseModel responseModel = ((Client) ctx.channel().attr(AttributeKey.valueOf("client")).get()).getCallback().get(clientId);
        responseModel.setResultId(recieveId);
        responseModel.getLatch().countDown();
               
    }

}
