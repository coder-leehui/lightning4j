package com.hql.lightning.core;

import com.hql.lightning.handler.ServerHandler;
import com.hql.lightning.util.ProReaderUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.log4j.Logger;

/**
 * 服务启动类
 *
 * @author lee
 *         2015-1-30
 */
public class Bootstrap {

    private static Logger logger = Logger.getLogger(Bootstrap.class);

    /**
     * 监听端口号
     */
    private int port;

    public Bootstrap() {
        this.port = Integer.parseInt(ProReaderUtil.getInstance().getNettyPro().get("port"));
    }

    /**
     * 初始化服务器设置
     *
     * @throws Exception
     */
    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("http-codec", new HttpServerCodec());
                            ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            ch.pipeline().addLast("readTimeOutHandler",
                                    new ReadTimeoutHandler(Integer.parseInt(
                                            ProReaderUtil.getInstance().getNettyPro().get("heartBeatTimeOut"))));
                            ch.pipeline().addLast("ServerHandler", new ServerHandler());
                        }
                    });

            ChannelFuture f = b.bind(port).sync();
            logger.info("Server started at port:" + port + "......");
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
