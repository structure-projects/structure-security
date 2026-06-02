package cn.structured.oauth.sdk.client;

import cn.hutool.core.io.IoUtil;
import cn.structure.common.enums.ResultCodeEnum;
import cn.structure.common.utils.HttpClientUtil;
import cn.structured.oauth.sdk.configuration.AuthClientConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 认证服务client请求
 *
 * @author chuck
 * @since JDK1.8
 */
@Slf4j
public class AuthClient {

    private String accessToken;

    private AuthClientConfig clientConfig;

    private HttpClient httpClient;

    private HttpHost httpHost;

    public AuthClient(AuthClientConfig clientConfig) {
        //认证
        this.clientConfig = clientConfig;
        this.httpClient = HttpClientUtil.getHttpClient();
        this.httpHost = new HttpHost(clientConfig.getHost(), clientConfig.getPort(), clientConfig.getScheme());
        this.token();
    }

    public JSONObject execute(HttpRequest httpRequest) {
        try {
            HttpUriRequest httpUriRequest = (HttpUriRequest) httpRequest;
            log.info("auth client execute begin -> this url is :{}, this is method:{}", httpUriRequest.getURI(), httpUriRequest.getMethod());
            httpRequest.addHeader("Authorization", "Bearer " + accessToken);
            HttpResponse response = httpClient.execute(httpHost, httpRequest);
            InputStream responseStream = response.getEntity().getContent();
            String responseContext = IoUtil.read(responseStream, StandardCharsets.UTF_8);
            log.info("result -> {}", responseContext);
            JSONObject responseObject = JSON.parseObject(responseContext);
            String code = responseObject.getString("code");
            //token 失效重新认证
            if (ResultCodeEnum.UNAUTHORIZED.getCode().equals(code) && token()) {
                return this.execute(httpRequest);
            }
            return responseObject;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 认证
     *
     * @return
     */
    private Boolean token() {
        HttpPost post = new HttpPost("/oauth/token");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("client_id", clientConfig.getClientId()));
        params.add(new BasicNameValuePair("client_secret", clientConfig.getClientSecret()));
        params.add(new BasicNameValuePair("grant_type", "client_credentials"));
        HttpEntity httpEntity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
        post.setEntity(httpEntity);
        try {
            HttpResponse response = this.httpClient.execute(httpHost, post);
            InputStream responseStream = response.getEntity().getContent();
            String responseContext = IoUtil.read(responseStream, StandardCharsets.UTF_8);
            JSONObject responseObject = JSON.parseObject(responseContext);
            this.accessToken = responseObject.getString("value");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
