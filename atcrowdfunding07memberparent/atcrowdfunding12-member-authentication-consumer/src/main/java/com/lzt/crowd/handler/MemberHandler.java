package com.lzt.crowd.handler;

import com.lzt.crowd.api.MySQLRemoteService;
import com.lzt.crowd.api.RedisRemoteService;
import com.lzt.crowd.config.ShortMessageProperties;
import com.lzt.crowd.constant.CrowdConstant;
import com.lzt.crowd.entity.po.MemberPO;
import com.lzt.crowd.entity.vo.MemberLoginVO;
import com.lzt.crowd.entity.vo.MemberVO;
import com.lzt.crowd.util.CrowdUtil;
import com.lzt.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
public class MemberHandler {

    @Autowired
    private ShortMessageProperties shortMessageProperties;

    @Autowired
    private RedisRemoteService redisRemoteService;
    
    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping("/auth/member/do/login")
    public String login(
            @RequestParam("loginacct") String loginacct,
            @RequestParam("userpswd") String userpswd,
            ModelMap modelMap,
            HttpSession session
                        ){
        ResultEntity<MemberPO> resultEntity =
                mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);

        if (ResultEntity.FAILED.equals(resultEntity.getResult())){

            modelMap.addAttribute(
                    CrowdConstant.ATTR_NAME_MESSAGE
                    , resultEntity.getMessage());
            return "member-login";
        }

        MemberPO memberPO = resultEntity.getData();
        if (memberPO == null){
            modelMap.addAttribute(
                    CrowdConstant.ATTR_NAME_MESSAGE
                    , "wrong acct");
            return "member-login";
        }

        String userpswdDatabase = memberPO.getUserpswd();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(userpswd, userpswdDatabase);

        if (!matches){
            modelMap.addAttribute(
                    CrowdConstant.ATTR_NAME_MESSAGE
                    , "wrong password");
            return "member-login";
        }
        MemberLoginVO memberLoginVO = new MemberLoginVO(
                memberPO.getId(),
                memberPO.getUsername(),
                memberPO.getEmail());

        session.setAttribute(
                CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);

        return "redirect:http://localhost/auth/member/to/center/page";
    }

    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap){

        //get phone number
        String phoneNum = memberVO.getPhoneNum();
        //phone key in redis
        String redisKey =CrowdConstant.REDIS_CODE_PREFIX+phoneNum;
        //get code value in redis
        ResultEntity<String> redisValueByKeyRemote = redisRemoteService.getRedisValueByKeyRemote(redisKey);

        String result = redisValueByKeyRemote.getResult();

        if (ResultEntity.FAILED.equals(result)){
            modelMap.addAttribute(
                    CrowdConstant.ATTR_NAME_MESSAGE,
                    redisValueByKeyRemote.getMessage());

            return "member-reg";
        }
        String redisCode = redisValueByKeyRemote.getData();

        if (redisCode ==null){
            modelMap.addAttribute(
                    CrowdConstant.ATTR_NAME_MESSAGE,
                    "code not exist");

            return "member-reg";
        }

        String formCode = memberVO.getCode();

        if (!Objects.equals(redisCode, formCode)) {
            modelMap.addAttribute(
                    CrowdConstant.ATTR_NAME_MESSAGE,
                    "code invalid");

            return "member-reg";
        }

        redisRemoteService.RemoveRedisKeyByKeyRemote(redisKey);

        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        String userpswdBeforeEncode = memberVO.getUserpswd();

        String userpswdAfterEncode = bCrypt.encode(userpswdBeforeEncode);

        memberVO.setUserpswd(userpswdAfterEncode);

        MemberPO memberPO = new MemberPO();

        BeanUtils.copyProperties(memberVO, memberPO);

        ResultEntity<String> saveMemberResultEntity = mySQLRemoteService.saveMember(memberPO);

        if (ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())){
            modelMap.addAttribute(
                    CrowdConstant.ATTR_NAME_MESSAGE,
                    saveMemberResultEntity.getMessage());

            return "member-reg";
        }

        return "redirect:/auth/member/to/login/page";

    }

    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(
        @RequestParam("phoneNum") String phoneNum
    ) {
        ResultEntity<String> sendMessageResultEntity = CrowdUtil.sendShortMessage(phoneNum,
                shortMessageProperties.getAppCode(),
                shortMessageProperties.getSmsSignId(),
                shortMessageProperties.getTemplateId());

        if (ResultEntity.SUCCESS.equals(sendMessageResultEntity.getResult())){

            String code = sendMessageResultEntity.getData();
            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

            //save code in redis
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueWithTimeoutRemote(
                    key,
                    code,
                    5,
                    TimeUnit.MINUTES);

            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())){
                return ResultEntity.successWithoutData();
            }else {
                return saveCodeResultEntity;
            }
        }else {
            return sendMessageResultEntity;
        }



    }
}
