package cn.structured.security.common.dto.social;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户社交绑定DTO
 * 用于返回用户与社交平台的绑定信息
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSocialBindDTO {

    private Long id;

    private String platformCode;

    private String platformUserId;

    private String nickname;

    private String avatar;

    private LocalDateTime bindTime;

    private Boolean enabled;
}