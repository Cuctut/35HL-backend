package com.cuctut.hl.auth;

import lombok.experimental.UtilityClass;

/**
 * 用户信息 持有类
 *
 * @author cuctut
 * @since 2024/10/01
 */
@UtilityClass
public class UserHolder {

    /**
     * 当前线程用户ID
     */
    private static final ThreadLocal<Long> userIdTL = new ThreadLocal<>();

    /**
     * 当前线程用户身份
     */
    private static final ThreadLocal<String> userRoleTL = new ThreadLocal<>();

    public void setUserId(Long userId) {
        userIdTL.set(userId);
    }

    public Long getUserId() {
        return userIdTL.get();
    }

    public void setUserRole(String userRole) {
        userRoleTL.set(userRole);
    }

    public String getUserRole() {
        return userRoleTL.get();
    }

    public void clear() {
        userIdTL.remove();
        userRoleTL.remove();
    }

}
