package com.hql.lightning.util;

import java.nio.charset.CodingErrorAction;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.*;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;

/**
 * http请求类
 */
public class HttpUtil {

    private static Logger logger = Logger.getLogger(HttpUtil.class);

    /**
     * http客户端
     */
    private CloseableHttpClient httpClient;

    /**
     * 连接池管理器
     */
    private PoolingHttpClientConnectionManager connManager;

    /**
     * 连接池连接数量上限
     */
    private int cmMaxTotal = 200;

    /**
     * 每个路由的最大连接数
     */
    private int cmMaxPerRoute = 20;

    /**
     * 设置连接池最大连接数
     *
     * @param val
     */
    public void setCmMaxTotal(int val) {
        this.cmMaxTotal = val;
    }

    /**
     * 设置连接池每个路由的最大连接数
     *
     * @param val
     */
    public void setCmMaxPerRoute(int val) {
        this.cmMaxPerRoute = val;
    }

    private String CHARSET = "UTF-8";

    private static HttpUtil instance = new HttpUtil();

    public static HttpUtil getInstance() {
        return instance;
    }

    /**
     * 创建http客户端
     *
     * @throws Exception
     */
    private void generateHttpClient() throws Exception {
        /*KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        SSLContext sslContext = SSLContexts.custom().useTLS().
                loadTrustMaterial(trustStore, new AnyTrustStrategy()).build();
        LayeredConnectionSocketFactory sslSF = new
                SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslSF)
                .build();

        connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        httpClient = HttpClients.custom().setConnectionManager(connManager).build();

        SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
        connManager.setDefaultSocketConfig(socketConfig);

        MessageConstraints messageConstraints = MessageConstraints.custom()
                .setMaxHeaderCount(200)
                .setMaxLineLength(2000)
                .build();

        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .setMessageConstraints(messageConstraints)
                .build();
        connManager.setDefaultConnectionConfig(connectionConfig);
        connManager.setMaxTotal(cmMaxTotal);
        connManager.setDefaultMaxPerRoute(cmMaxPerRoute);*/
        RequestConfig config = RequestConfig.custom().setConnectTimeout(2000).setSocketTimeout(2000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    /**
     * HTTP Get 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @return    页面内容
     */
    public String doGet(String url,Map<String,String> params) throws Exception{
        if(StringUtils.isBlank(url)){
            return null;
        }

        if (httpClient == null) generateHttpClient();
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        HttpGet httpGet = null;
        try {
            if(params != null && !params.isEmpty()){
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, CHARSET));
            }

            httpGet = new HttpGet(url);
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }

            entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, CHARSET);
            }
            return result;
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if(entity != null) EntityUtils.consume(entity);
            if (response != null) response.close();
            httpGet.releaseConnection();
        }
        return null;
    }

    /**
     * HTTP Post 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @return    页面内容
     */
    public String doPost(String url,Map<String,String> params) throws Exception {
        if(StringUtils.isBlank(url)){
            return null;
        }

        if (httpClient == null) generateHttpClient();
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        HttpPost httpPost = null;
        try {
            List<NameValuePair> pairs = null;
            if(params != null && !params.isEmpty()){
                pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
            }

            httpPost = new HttpPost(url);
            if(pairs != null && pairs.size() > 0){
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
            }

            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }

            entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, CHARSET);
            }
            EntityUtils.consume(entity);
            return result;
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if(entity != null) EntityUtils.consume(entity);
            if (response != null) response.close();
            httpPost.releaseConnection();
        }
        return null;
    }

    /**
     * 密钥信任策略（信任所有）
     */
    class AnyTrustStrategy implements TrustStrategy {

        @Override
        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            return true;
        }

    }
}
