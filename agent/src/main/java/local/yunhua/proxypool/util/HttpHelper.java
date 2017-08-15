package local.yunhua.proxypool.util;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpHelper {
    private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class);

    public String get(String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0");

        CloseableHttpResponse response;
        try {
            response = client.execute(httpGet);
        } catch (IOException e) {
            logger.error(String.format("Exception occurs when GET %s", url));
            logger.error(ExceptionUtils.getFullStackTrace(e));
            return null;
        }

        HttpEntity entity = response.getEntity();
        String responseBody = null;
        try {
            responseBody = EntityUtils.toString(entity);
            response.close();
        } catch (IOException e) {
            logger.error(ExceptionUtils.getFullStackTrace(e));
        }
        return responseBody;
    }


    public String post(String url, String requestBody) {
        return post(url, requestBody, ContentType.APPLICATION_JSON);
    }


    public String post(String url, String requestBody, ContentType contentType) {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", "Mozilla/5.0");
        StringEntity requestEntity;
        if (null == contentType) {
            contentType = ContentType.TEXT_PLAIN;
        }
        requestEntity = new StringEntity(requestBody, contentType);

        httpPost.setEntity(requestEntity);

        CloseableHttpResponse response;
        try {
            response = client.execute(httpPost);
        } catch (IOException e) {
            logger.error(ExceptionUtils.getFullStackTrace(e));
            return null;
        }

        HttpEntity entity = response.getEntity();
        String responseBody = null;
        try {
            responseBody = EntityUtils.toString(entity);
            response.close();
        } catch (IOException e) {
            logger.error(ExceptionUtils.getFullStackTrace(e));
        }
        return responseBody;
    }

}
