lightning4j
===========

## 简介
一个基于Netty网络库的java服务端轻量级开发框架，用于快速开发手游，页游等服务端程序，使用WebSocket通信协议，支持mysql，redis，连接池，全局定时任务，心跳检测，可配置逻辑处理线程等，使用简单，部署方便。

## 使用（基于IntelliJ IDEA）
新建一个maven project或在已有project中新建一个maven module，编辑pom.xml中的依赖包：

    <dependencies>
	    <dependency>
	        <groupId>netty</groupId>
	        <artifactId>netty</artifactId>
	        <version>4.0.25.Final</version>
	    </dependency>
	    <dependency>
	        <groupId>mysql</groupId>
	        <artifactId>mysql-connector-java</artifactId>
	        <version>5.1.30</version>
	    </dependency>
	    <dependency>
	        <groupId>redis.clients</groupId>
	        <artifactId>jedis</artifactId>
	        <version>2.4.0</version>
	    </dependency>
	    <dependency>
	        <groupId>c3p0</groupId>
	        <artifactId>c3p0</artifactId>
	        <version>0.9.1.2</version>
	    </dependency>
	    <dependency>
	        <groupId>org.slf4j</groupId>
	        <artifactId>slf4j-log4j12</artifactId>
	        <version>1.7.6</version>
	    </dependency>
	    <dependency>
	        <groupId>log4j</groupId>
	        <artifactId>log4j</artifactId>
	        <version>1.2.17</version>
	    </dependency>
	    <dependency>
	        <groupId>dom4j</groupId>
	        <artifactId>dom4j</artifactId>
	        <version>1.6.1</version>
	    </dependency>
	    <dependency>
	        <groupId>org.mybatis</groupId>
	        <artifactId>mybatis</artifactId>
	        <version>3.2.3</version>
	    </dependency>
	    <dependency>
	        <groupId>junit</groupId>
	        <artifactId>junit</artifactId>
	        <version>4.11</version>
	        <scope>test</scope>
	    </dependency>
	    <dependency>
	        <groupId>com.alibaba</groupId>
	        <artifactId>fastjson</artifactId>
	        <version>1.1.41</version>
	    </dependency>
	    <dependency>
	        <groupId>com.hql</groupId>
	        <artifactId>lightning4j</artifactId>
	        <version>1.0.0</version>
	    </dependency>
    </dependencies>

* lib目录下为框架的jar包，用于添加到maven仓库
* 建议使用maven私服管理依赖包
* confFile下为项目所需的配置文件及log存放目录
* 示例代码请参考test目录
* client.html为测试客户端

## 基本用法
* 在IDEA的project目录下新建一个文件夹例如：conf存放配置文件及log，将源码目录下confFile里的内容拷贝到该文件夹中。
* 修改配置选项为开发者自己的配置
* 在初始化代码中设置配置文件根路径：`ServerInit.getInstance().initConfPath("conf");`
* 参考项目结构：
<br>......包<br>
  |--handler（业务逻辑handler）<br>
  |--model（MyBatis使用的数据dao）<br>
  |--manager(缓存数据管理)<br>
  |--vo（用于映射json的对象）<br>
  |--util（项目工具集）<br><br>
* 启动代码示例：<br>

        public class Server {
			public void run() throws Exception {
			    ServerInit.getInstance().initConfPath("conf");
			    ServerInit.getInstance().initLog4j();
			    ServerInit.getInstance().initGameWorkers();
			
			    GameBoss.getInstance().boot(new GameUpProcessor() {
			        @Override
			        public void process(GameUpBuffer buffer) {
			            GameWorkerManager.getInstance().pushDataToWorker(buffer);
			        }
			    });
			}
			
			public static void main(String[] args) throws Exception {
			    GameHandlerManager.getInstance().register(HandlerTest.class, "test");
			    GameHandlerManager.getInstance().register(DisconnectHandlerTest.class, "onDisconnect");
			    new Server().run();
			}
		}

## 后续更新计划
* 服务器热更新
* 性能测试工具
* 分布式支持

## License

Apache License Version 2.0 see http://www.apache.org/licenses/LICENSE-2.0.html
 