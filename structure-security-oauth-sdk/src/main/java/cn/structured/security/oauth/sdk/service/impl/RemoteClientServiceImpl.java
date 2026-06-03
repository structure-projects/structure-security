package cn.structured.security.oauth.sdk.service.impl;

import cn.structured.security.common.dto.client.ClientDTO;
import cn.structured.security.oauth.sdk.client.AuthClient;
import cn.structured.security.oauth.sdk.service.IRemoteClientService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 远程调用客户端
 *
 * @author chuck
 * @since JDK1.8
 */
public class RemoteClientServiceImpl implements IRemoteClientService {

    @Resource
    private AuthClient authClient;

    @Override
    public void register(ClientDTO clientDto) {
        HttpPost post = new HttpPost("/client");
        HttpEntity httpEntity = postParams(clientDto);
        post.setEntity(httpEntity);
        post.setEntity(httpEntity);
        authClient.execute(post);
    }

    @Override
    public void destroy(String clientId) {
        HttpDelete delete = new HttpDelete("/client/" + clientId);
        authClient.execute(delete);
    }

    @Override
    public void change(ClientDTO clientDto) {
        HttpPost post = new HttpPost("/client/change");
        HttpEntity httpEntity = postParams(clientDto);
        post.setEntity(httpEntity);
        authClient.execute(post);
    }

    @Override
    public ClientDTO findClientById(String clientId) {
        HttpGet get = new HttpGet("/client/" + clientId);
        JSONObject execute = authClient.execute(get);
        return JSON.parseObject(JSON.toJSONString(execute), ClientDTO.class);
    }

    private HttpEntity postParams(ClientDTO clientDto) {
        JSONObject jsonParams = JSON.parseObject(JSON.toJSONString(clientDto));
        List<NameValuePair> params = new ArrayList<>();
        jsonParams.forEach((k, v) -> params.add(new BasicNameValuePair(k, v.toString())));
        return new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
    }
}
