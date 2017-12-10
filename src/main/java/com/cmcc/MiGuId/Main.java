package com.cmcc.MiGuId;

import com.cmcc.MiGuId.Gen.Generator;
import com.cmcc.MiGuId.Gen.SuffixThread;
import com.cmcc.MiGuId.Server.handler.ChannelServerInitializerImpl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Main {

    private static Generator g = new Generator();

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        new Main();

        EventLoopGroup parentG = new NioEventLoopGroup(1);
        EventLoopGroup child = new NioEventLoopGroup(1);
        ServerBootstrap b = new ServerBootstrap();
        b.childOption(ChannelOption.TCP_NODELAY, true);
        b.childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            ChannelServerInitializerImpl c = new ChannelServerInitializerImpl();
            b.group(parentG, child).channel(NioServerSocketChannel.class).localAddress(9886).childHandler(c);
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        } finally {
            parentG.shutdownGracefully().sync();
            child.shutdownGracefully().sync();
        }

    }

    public Main() {
        SuffixThread s = new SuffixThread(g);
        Thread t = new Thread(s);
        t.setDaemon(true);
        t.start();
    }

    public static Generator getG() {
        return g;
    }

}
