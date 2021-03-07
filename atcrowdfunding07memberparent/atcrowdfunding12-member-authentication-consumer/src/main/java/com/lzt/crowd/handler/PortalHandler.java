package com.lzt.crowd.handler;

import com.lzt.crowd.api.MySQLRemoteService;
import com.lzt.crowd.constant.CrowdConstant;
import com.lzt.crowd.entity.vo.PortalTypeVO;
import com.lzt.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PortalHandler {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/")
    public String showPortalPage(ModelMap modelMap){

        ResultEntity<List<PortalTypeVO>> resultEntity = mySQLRemoteService.getPortalTypeProjectDataRemote();

        String result = resultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(result)){
            List<PortalTypeVO> list = resultEntity.getData();
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA,list);

        }


        return "portal";
    }
}
