package com.lzt.crowd.mvc.handler;

import com.lzt.crowd.entity.Admin;
import com.lzt.crowd.entity.Student;
import com.lzt.crowd.service.api.AdminService;
import com.lzt.crowd.util.CrowdUtil;
import com.lzt.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TestHandler {
    @Autowired
    private AdminService adminService;

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @ResponseBody
    @RequestMapping("send/array/one.html")
    public String testReceiveArrayOne(@RequestParam("array[]") List<Integer> array){
        for (Integer number:array) {
            System.out.println(number);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("send/array/two.html")
    public String testReceiveArrayTwo(@RequestParam("array") List<Integer> array){
        for (Integer number:array) {
            System.out.println(number);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("send/array/three.html")
    public String testReceiveArrayThree(@RequestBody List<Integer> array){

        for (Integer number:array) {
            logger.info("number="+number);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("send/compose/object.json")
    public ResultEntity<Student> testReceiveComposeObject(@RequestBody Student student,HttpServletRequest request){

        boolean type = CrowdUtil.judgeRequestType(request);

        logger.info("type"+type);
        logger.info(student.toString());
        return ResultEntity.successWithData(student);
    }

    @RequestMapping("/test/ssm.html")
    public String testSsm(ModelMap modelMap, HttpServletRequest request, HttpSession session){

        boolean type = CrowdUtil.judgeRequestType(request);

        logger.info("type"+type);
        List<Admin> adminList = adminService.getAll();
        modelMap.addAttribute("adminList",adminList);
        session.setAttribute("number", "2222");
        //System.out.println(10/0);
        return "target";
    }
}
