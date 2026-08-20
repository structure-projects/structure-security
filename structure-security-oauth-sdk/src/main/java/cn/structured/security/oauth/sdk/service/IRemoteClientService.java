/*
Copyright 2023 Structure Projects

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package cn.structured.security.oauth.sdk.service;

import cn.structured.security.common.dto.client.ClientDTO;

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
