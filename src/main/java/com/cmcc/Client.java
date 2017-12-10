package com.cmcc;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.cmccmMiGuId.Client.ResponseModel;
import com.cmccmMiGuId.Client.handler.NettyChannelPoolHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelHealthChecker;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

//此处做成池化的netty连接线程池
public class Client {
    private int maxConnections = Runtime.getRuntime().availableProcessors();
    //针对一个url,设置等待连接这个url的pendingAcquires
    private int maxPendingAcquires = 2 * maxConnections;
    private String remoteIp = "127.0.0.1";
    private int remotePort = 9886;
    private int Acqurietimeout = -1;
    
     
    ChannelPoolMap<InetSocketAddress, SimpleChannelPool> poolMap;
    
    private Map<Long,ResponseModel> callback = new ConcurrentHashMap<>();
    
    AtomicLong num = new AtomicLong(0);
    
    final EventLoopGroup group = new NioEventLoopGroup();
    
    public void start() throws InterruptedException{
            final Bootstrap strap = new Bootstrap();
            strap.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(remoteIp, remotePort))
            .option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
            strap.attr(AttributeKey.valueOf("client"), this);
            poolMap = new AbstractChannelPoolMap<InetSocketAddress, SimpleChannelPool>() {
                @Override
                protected SimpleChannelPool newPool(InetSocketAddress key) {
                    return new FixedChannelPool(strap.remoteAddress(key), new NettyChannelPoolHandler(), ChannelHealthChecker.ACTIVE, null, Acqurietimeout,maxConnections,maxPendingAcquires);
                }
            };
    }
    
    //远程断开连接的时候，获取到exception需要关闭此链接 
    public String getId() throws InterruptedException{
            final SimpleChannelPool pool = poolMap.get(new InetSocketAddress(remoteIp, remotePort));
            Future<Channel> f = pool.acquire();
            ResponseModel res = new ResponseModel();
            f.addListener((FutureListener<Channel>)f1->{
                if(f1.isSuccess()){
                    Channel c = f1.getNow();
                    try {
                        long clientId = num.incrementAndGet();
                        ByteBuf b = Unpooled.buffer(10);
                        b.writeByte(9);
                        b.writeByte('c');
                        b.writeLong(clientId);
                        res.setClientId(clientId);
                        callback.put(clientId, res);
                        c.writeAndFlush(b);
                    } finally {
                        pool.release(c);
                    }
                }else{
                    System.out.println("failed");
                }
            });
            //这个地方加上超时设置
            res.getLatch().await(1000,TimeUnit.MILLISECONDS);
            callback.remove(res.getClientId());
            if(null == res.getResultId()){
                throw new RuntimeException("failed in get channel");
            }
            return res.getResultId();
        }


    public static void main(String...strings) throws InterruptedException{
        Client client = new Client();
        client.start();
//        System.out.println(client.getId());
        Thread t1Thread = new Thread(()->{
            System.out.println(System.currentTimeMillis());
            for(int i = 0;i < 500_000;++i){
                try {
                    //System.out.println(client.getId());
                    client.getId();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            System.out.println(System.currentTimeMillis());
        });
        
        Thread t2Thread = new Thread(()->{
            System.out.println(System.currentTimeMillis());
            for(int i = 0;i < 500_000;++i){
                try {
                    //System.out.println(client.getId());
                    client.getId();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            System.out.println(System.currentTimeMillis());
        });
        Thread t3Thread = new Thread(()->{
            System.out.println(System.currentTimeMillis());
            for(int i = 0;i < 500_000;++i){
                try {
                    //System.out.println(client.getId());
                    client.getId();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            System.out.println(System.currentTimeMillis());
        });
        t1Thread.start();
        t2Thread.start();
        t3Thread.start();
        
        
    }

    public Map<Long, ResponseModel> getCallback() {
        return callback;
    }
    
    
    
    
    
    
    
    
    
}
