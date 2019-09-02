package cn.gy4j.monitor.sniffer.core.remote;

import cn.gy4j.monitor.sniffer.core.config.AgentConfig;
import cn.gy4j.monitor.sniffer.core.logging.LoggerFactory;
import cn.gy4j.monitor.sniffer.core.logging.api.ILogger;
import cn.gy4j.monitor.sniffer.core.util.StringUtil;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

/**
 * author   gy4j
 * Email    76429197@qq.com
 * Date     2019-08-18
 */
public class RemoteManager {
    private static final ILogger logger = LoggerFactory.getLogger(RemoteManager.class);

    private static final int MAX_TOTAL = 100;
    private static final int CONNECT_TIMEOUT = 3000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 1000;
    private static final int SOCKET_TIMEOUT = 1000;
    private static final long HEART_BEAT_SPACE = 1000;//心跳间隔
    private static final int HEART_BEAT_COUNT = 5;//心跳异常阈值
    private static AtomicInteger heartBeatFailCount = new AtomicInteger(0);

    private static boolean isActive = false;
    private static PoolingHttpClientConnectionManager connectionManager = null;

    /**
     * 初始化.
     */
    public static void init() {
        if (StringUtil.isEmpty(AgentConfig.Remote.COLLECTOR_URL)) {
            return;
        }
        initClient();
        start();
    }

    private static void initClient() {
        LayeredConnectionSocketFactory connectionSocketFactory = null;
        try {
            connectionSocketFactory = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            logger.error("创建SSL连接失败...");
        }
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", connectionSocketFactory)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        connectionManager = new PoolingHttpClientConnectionManager(registry);
        // 设置最大的连接数
        connectionManager.setMaxTotal(MAX_TOTAL);
        connectionManager.setDefaultMaxPerRoute(MAX_TOTAL);
    }

    private static void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //处理心跳
                while (true) {
                    heartBeat();
                    try {
                        sleep(HEART_BEAT_SPACE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 获取httpclient.
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
        return httpClient;
    }

    /**
     * 心跳.
     */
    public static void heartBeat() {
        if (post(AgentConfig.Remote.COLLECTOR_URL, RemoteEvent.HEART_BEAT)) {
            isActive = true;
            heartBeatFailCount.set(0);
        } else {
            int count = heartBeatFailCount.incrementAndGet();
            if (count >= HEART_BEAT_COUNT) {
                isActive = false;
                heartBeatFailCount.set(0);
            }
        }
    }

    /**
     * 数据采集.
     *
     * @param content 采集内容
     */
    public static void send(String content) {
        if (isActive) {
            post(AgentConfig.Remote.COLLECTOR_URL, RemoteEvent.TRACER, content);
        } else {
            logger.warn("采集服务不可用：" + content);
        }
    }

    /**
     * post请求.
     *
     * @param url         请求地址
     * @param remoteEvent 事件类型
     * @return
     */
    private static boolean post(String url, RemoteEvent remoteEvent) {
        return post(url, remoteEvent, "");
    }

    /**
     * post请求.
     *
     * @param url         请求地址
     * @param remoteEvent 事件类型
     * @param content     请求参数
     * @return
     */
    private static boolean post(String url, RemoteEvent remoteEvent, String content) {
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = getHttpClient();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setRedirectsEnabled(true).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        //装配post请求参数
        List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("content", content));  //请求参数
        list.add(new BasicNameValuePair("event", remoteEvent.toString()));  //请求事件类型
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
            //设置post求情参数
            httpPost.setEntity(entity);
            httpResponse = httpClient.execute(httpPost);
            if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200) {
                return true;
            }
        } catch (Exception e) {
            if (remoteEvent != RemoteEvent.HEART_BEAT) {
                logger.error(e, "采集异常：" + remoteEvent + "," + e.getMessage());
            }
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    logger.warn(e, e.getMessage());
                }
            }
        }
        if (remoteEvent != RemoteEvent.HEART_BEAT) {
            logger.warn("采集异常：" + remoteEvent + "," + content);
        }
        return false;
    }
}
