package com.lzt.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {

        String urlPath="/auth/member/to/reg/page";

        String viewName="member-reg";

        registry.addViewController(urlPath).setViewName(viewName);
        // 前往登录页面
        registry.addViewController("/auth/member/to/login/page").setViewName("member-login");

        // 前往登录完成后的用户主页面
        registry.addViewController("/auth/member/to/center/page").setViewName("member-center");

        // 前往“我的众筹”页面
        registry.addViewController("/member/my/crowd").setViewName("member-crowd");
    }
}
