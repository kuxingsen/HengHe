package com.henghe.controller;

import com.henghe.utils.Md5Utils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("interceptor");
        HttpSession session = httpServletRequest.getSession();
        Integer adminId = (Integer)session.getAttribute("adminId");
        String md5 = (String)session.getAttribute("key");
        String requestURI = httpServletRequest.getRequestURI();
        if (requestURI.contains("error")||requestURI.contains("login")||requestURI.contains("adminLogin"))
            return true;
        if(adminId != null && md5.equals(Md5Utils.key(String.valueOf(adminId)))) {
            return true;
        }
        httpServletResponse.sendRedirect("/admin/login");
        return false;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
