package com.lzt.crowd.mvc.config;


import com.google.gson.Gson;
import com.lzt.crowd.constant.CrowdConstant;
import com.lzt.crowd.exception.AccessForbiddenException;
import com.lzt.crowd.exception.LoginAcctAlreadyInUseException;
import com.lzt.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.lzt.crowd.exception.LoginFailedException;
import com.lzt.crowd.util.CrowdUtil;
import com.lzt.crowd.util.ResultEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//
@ControllerAdvice
public class CrowdExceptionResolver {

    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveNullpointerException(LoginAcctAlreadyInUseException exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {

        String viewName = "admin-add";

        return commonResolve(viewName,exception,request,response);
    }

    @ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView resolveNullpointerException(LoginAcctAlreadyInUseForUpdateException exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {

        String viewName = "system-error";

        return commonResolve(viewName,exception,request,response);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullpointerException(NullPointerException exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {

        String viewName = "admin-login";

        return commonResolve(viewName,exception,request,response);
    }
    @ExceptionHandler(value = AccessForbiddenException.class)
    public ModelAndView resolveNullpointerException(AccessForbiddenException exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {

        String viewName = "admin-login";

        return commonResolve(viewName,exception,request,response);
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolveNullpointerException(Exception exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {

        String viewName = "admin-login";

        return commonResolve(viewName,exception,request,response);
    }

    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(LoginFailedException exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) throws IOException {

        String viewName = "admin-login";

        return commonResolve(viewName,exception,request,response);
    }

    // @ExceptionHandler?????????????????????????????????????????????????????????
    private ModelAndView commonResolve(
            // ????????????????????????????????????
            String viewName,
            // ??????????????????????????????
            Exception exception,
            // ??????????????????
            HttpServletRequest request,
            // ??????????????????
            HttpServletResponse response) throws IOException {
        // 1.????????????????????????
        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        // 2.?????????Ajax??????
        if (judgeResult) {
            // 3.??????ResultEntity??????
            ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());
            // 4.??????Gson??????
            Gson gson = new Gson();
            // 5.???ResultEntity???????????????JSON?????????
            String json = gson.toJson(resultEntity);
            // 6.???JSON??????????????????????????????????????????
            response.getWriter().write(json);
            // 7.?????????????????????????????????response???????????????????????????????????????ModelAndView??????
            return null;
        }
        // 8.????????????Ajax???????????????ModelAndView??????
        ModelAndView modelAndView = new ModelAndView();
        // 9.???Exception??????????????????
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
        // 10.???????????????????????????
        modelAndView.setViewName(viewName);
        // 11.??????modelAndView??????
        return modelAndView;
    }

}
