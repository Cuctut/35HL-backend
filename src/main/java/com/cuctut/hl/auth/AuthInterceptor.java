package com.cuctut.hl.auth;

import com.cuctut.hl.common.ErrorCodeEnum;
import com.cuctut.hl.exception.BusinessException;
import com.cuctut.hl.mapper.UserMapper;
import com.cuctut.hl.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

/**
 * 认证授权 拦截器
 *
 * @author cuctut
 * @since 2024/10/05
 */
@AllArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final UserMapper userMapper;

    /**
     * handle 执行前调用
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        // 获取登录 JWT 并校验用户
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);

        Long userId = JwtUtils.getUserIdFromToken(token);
        if (Objects.isNull(userId)) throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);

        User user = userMapper.selectById(userId);
        if (Objects.isNull(user)) throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);

        // 设置 userId 到当前线程
        UserHolder.setUserId(userId);
        UserHolder.setUserRole(user.getRole());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * handler 执行后调用，出现异常不调用
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * DispatcherServlet 完全处理完请求后调用，出现异常照常调用
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
        // 清理当前线程保存的用户数据
        UserHolder.clear();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
