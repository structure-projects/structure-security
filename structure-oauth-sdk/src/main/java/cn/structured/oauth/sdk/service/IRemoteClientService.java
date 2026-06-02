package cn.structured.oauth.sdk.service;

import cn.structured.oauth.api.dto.client.ClientDTO;

/**
 * 远程调用客户端
 *
 * @author chuck
 * @since JDK1.8
 */
public interface IRemoteClientService {


    /**
     * 注册
     *
     * @param clientDto
     */
    void register(ClientDTO clientDto);

    /**
     * 销毁
     *
     * @param clientId
     */
    void destroy(String clientId);

    /**
     * 变更
     *
     * @param clientDto
     */
    void change(ClientDTO clientDto);

    /**
     * 查询
     *
     * @param clientId
     * @return
     */
    ClientDTO findClientById(String clientId);

}
