package com.flab.livecommerce.common.interceptor;

import com.flab.livecommerce.common.annotation.LoginCheck;
import com.flab.livecommerce.common.auth.AuthenticatedUser;
import com.flab.livecommerce.domain.user.TokenRepository;
import com.flab.livecommerce.domain.user.exception.InvalidTokenException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {

        if (handler instanceof HandlerMethod == false) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            if (handlerMethod.getMethodAnnotation(LoginCheck.class) == null) {
                return true;
            }
        }

        return true;
    }
}
