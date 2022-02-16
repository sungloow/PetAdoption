package com.sunhongbing.petadoption.backstage.exception;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @className: GlobalExceptionHandler
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-02-14 16:20
 */

public class GlobalExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        if (e instanceof UnauthorizedException) { // 未授权 : 403
            ModelAndView mv = new ModelAndView("error/403");
            return mv;
        } else if (e instanceof UnauthenticatedException) { // 未认证 : 401
            ModelAndView mv = new ModelAndView("forestage/index");
            return mv;
        }
        return null;
    }

}
