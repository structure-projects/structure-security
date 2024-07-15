package cn.structured.security.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;


/**
 * 用户的账号
 *
 * @author chuck
 */
@Data
public class StructureAuthUser implements UserDetails, Serializable {

    private Serializable id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否启用 启用 未启用
     */
    private Boolean enable;

    /**
     * 是否锁定 锁定 :未锁定
     */
    private Boolean unlocked;

    /**
     * 是否过期 过期 未过期
     */
    private Boolean unexpired;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    private transient Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.unexpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.unlocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }

}
