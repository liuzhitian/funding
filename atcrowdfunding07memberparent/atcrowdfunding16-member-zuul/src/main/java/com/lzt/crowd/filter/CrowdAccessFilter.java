package com.lzt.crowd.filter;

import com.lzt.crowd.constant.AccessPassResources;
import com.lzt.crowd.constant.CrowdConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class CrowdAccessFilter extends ZuulFilter {

    public String filterType() {
        return "pre";
    }

    public int filterOrder() {
        return 0;
    }

    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();

        HttpServletRequest request = currentContext.getRequest();

        String servletPath = request.getServletPath();

        boolean isPassContains  = AccessPassResources.PASS_RES_SET.contains(servletPath);
        if (isPassContains){
            return false;
        }

        boolean judgeResult = AccessPassResources.judgeCurrentServletPathWetherStaticResource(servletPath);

        return !judgeResult;
    }

    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();

        HttpServletRequest request = currentContext.getRequest();

        HttpSession session = request.getSession();

        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        if (loginMember == null){

            HttpServletResponse response = currentContext.getResponse();

            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE, "please login");


            try {
                response.sendRedirect("/auth/member/to/login/page");
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return null;
    }
}
