package cn.structured.oauth.api.dto.social;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 社交渠道DTO
 * 用于返回社交渠道信息
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialChannelDTO {

    private String channelCode;

    private String channelName;

    private String authUrl;

    private String scope;
}