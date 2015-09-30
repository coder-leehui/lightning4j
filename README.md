lightning4j
===========

## 更新日志
### 2015/9/30更新内容
* 加入dbcp2数据库连接池
* 增加工作线程状态监控

### 2015/8/14更新内容
* 模块热更新存在严重bug暂时取消
* netty升级到5.0
* 增加mysql数据库操作类
* 增加了http请求方法
* 修复部分bug及统一异常捕捉日志
* 开源了本人基于lightning4j开发的html5游戏《无尽战争》的服务端作为示例

### 2015/2/13更新内容
* 增加模块加载工具，实现游戏业务模块热更新

## 简介
一个基于Netty网络库的java服务端轻量级开发框架，用于快速开发手游，页游等服务端程序，使用WebSocket通信协议，支持mysql，redis，连接池，全局定时任务，心跳检测，可配置逻辑处理线程等，使用简单，部署方便。
<br><br>**项目网站：**http://www.53hql.com/lightning4j
<br>**项目WiKi：**http://www.53hql.com/lightning4j/wiki

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
	        <version>1.0.7</version>
	    </dependency>
    </dependencies>

* lib目录下为框架的jar包，用于添加到maven仓库
* 建议使用maven私服管理依赖包
* confFile下为项目所需的配置文件、模块文件及log存放目录
* 示例代码请参考test目录
* client.html为测试客户端

## 基本用法
* 在IDEA的project目录下新建一个文件夹例如：conf存放配置文件及log，将源码目录下confFile里的内容拷贝到该文件夹中。
* 修改配置选项为开发者自己的配置
* 简历游戏模块工程（new maven module in IntelliJ IDEA）参考项目结构：
<br>......包<br>
  |--handler（业务逻辑handler）<br>
  |--model（使用的数据dao）<br>
  |--manager(缓存数据管理)<br>
  |--vo（用于映射json的对象）<br>
* 使用maven打包该module为jar文件
* 启动代码示例：<br>

        public class Server {
			public void run() throws Exception {
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
                //5秒检测一次线程状态，若线程终止则重启线程
                TimerTaskUtil.getInstance().run(new WorkerStatusCheckTask(), 5, TimeUnit.SECONDS);
			    new Server().run();
			}
		}
* 更多使用详情请参考示例：demo_server
## 后续更新计划
* 性能测试工具
* 分布式支持

## License

Apache License Version 2.0 see http://www.apache.org/licenses/LICENSE-2.0.html
 