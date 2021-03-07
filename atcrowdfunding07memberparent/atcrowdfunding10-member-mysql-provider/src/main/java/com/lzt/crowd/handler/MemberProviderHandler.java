package com.lzt.crowd.handler;

import com.lzt.crowd.constant.CrowdConstant;
import com.lzt.crowd.entity.po.MemberPO;
import com.lzt.crowd.entity.vo.ProjectVO;
import com.lzt.crowd.service.api.MemberService;
import com.lzt.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberProviderHandler {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO){
        try {
            memberService.saveMember(memberPO);
            return ResultEntity.successWithoutData();
        }catch (Exception e){

            if (e instanceof DuplicateKeyException){
                return ResultEntity.failed("username in use");
            }
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/get/member/by/login/acct/remote")
    public ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(
            @RequestParam("loginacct") String loginacct){

        try {
            MemberPO memberPO = memberService.getMemberPOByLoginAcct(loginacct);
            return ResultEntity.successWithData(memberPO);
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }


}
