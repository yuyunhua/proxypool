package local.yunhua.proxypool.util;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    //对上一个方法的重载，使用本机ip进行网站爬取
    public static String getHtml(String url) {
        String responseBody = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //设置超时处理
//        RequestConfig config = RequestConfig.custom().setConnectTimeout(3000).
//                setSocketTimeout(3000).build();
        HttpGet httpGet = new HttpGet(url);
//        httpGet.setConfig(config);
        httpGet.setHeader("User-Agent", "Mozilla/5.0");


        try {
            //客户端执行httpGet方法，返回响应
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            int responseCode = httpResponse.getStatusLine().getStatusCode();
            responseBody = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
            //得到服务响应状态码
            if (responseCode != 200) {
                logger.error(String.format("Failed to get html from url: %s", url));
                logger.error(String.format("Response code: %d", responseCode));
                logger.error(String.format("Response body:\n%s", responseBody));
                return null;
            }

            httpResponse.close();
            httpClient.close();
        } catch (IOException e) {
            logger.error(ExceptionUtils.getFullStackTrace(e));
        }

        return responseBody;
    }
}
