package com.cmccmMiGuId.Client.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class NettyChannelPoolHandler extends AbstractChannelPoolHandler{

    @Override
    public void channelCreated(Channel ch) throws Exception {
        // TODO Auto-generated method stub
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(31,0,1,0,1));
        pipeline.addLast(new ClientInboundHandler());
    }

    
}
