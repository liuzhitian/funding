package com.lzt.crowd.handler;

import com.lzt.crowd.api.MySQLRemoteService;
import com.lzt.crowd.config.OSSProperties;
import com.lzt.crowd.constant.CrowdConstant;
import com.lzt.crowd.entity.vo.*;
import com.lzt.crowd.util.CrowdUtil;
import com.lzt.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectConsumerHandler {

    @Autowired
    private OSSProperties ossProperties;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/create/confirm")
    public String saveConfirm(
            HttpSession session,
            MemberConfirmInfoVO memberConfirmInfoVO,
            ModelMap modelMap
    ){
        ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

        if (projectVO == null){
            throw new RuntimeException("temp projectVO not found");
        }

        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);

        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        Integer memberId = memberLoginVO.getId();

        ResultEntity<String> saveResultEntity =  mySQLRemoteService.saveProjectVORemote(projectVO,memberId);

        String result = saveResultEntity.getResult();
        if (ResultEntity.FAILED.equals(result)){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE
                    ,saveResultEntity.getMessage());
            return "project-confirm";
        }

        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
        return "redirect:http://localhost/project/create/success";

    }


    @ResponseBody
    @RequestMapping("/create/save/return.json")
    public ResultEntity<String> saveReturn(
            ReturnVO returnVO,
            HttpSession session
    ){
        try {
            ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

            if (projectVO == null){
                return ResultEntity.failed("no temp project");
            }

            List<ReturnVO> returnVOList = projectVO.getReturnVOList();

            if (returnVOList == null || returnVOList.size()==0){
                returnVOList = new ArrayList<ReturnVO>();
                projectVO.setReturnVOList(returnVOList);
            }

            returnVOList.add(returnVO);

            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);

            return ResultEntity.successWithoutData();
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }

    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultEntity<String> uploadReturnPicture(
            @RequestParam("returnPicture") MultipartFile returnPicture) throws IOException {

        ResultEntity<String> uploadReturnPictureResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                returnPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                returnPicture.getOriginalFilename());

        return uploadReturnPictureResultEntity;
    }

    @RequestMapping("/create/project/information")
    public String saveProjectBasicInfo(
            ProjectVO projectVO,
            MultipartFile headerPicture,
            List<MultipartFile> detailPictureList,
            HttpSession session,
            ModelMap modelMap) throws IOException {


        boolean headerPictureIsEmpty = headerPicture.isEmpty();

        if (headerPictureIsEmpty){

            modelMap.addAttribute(
                    CrowdConstant.ATTR_NAME_MESSAGE,
                    "no header picture found");

            return "project-launch";

        }

        ResultEntity<String> uploadHeaderPicResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                headerPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                headerPicture.getOriginalFilename());

        String result = uploadHeaderPicResultEntity.getResult();

        if (ResultEntity.SUCCESS.equals(result)) {
            String headerPicturePath = uploadHeaderPicResultEntity.getData();
            projectVO.setHeaderPicturePath(headerPicturePath);
        }else {
            modelMap.addAttribute(
                    CrowdConstant.ATTR_NAME_MESSAGE,
                    "header picture upload failed");

            return "project-launch";
        }

        List<String> detailPicturePathList = new ArrayList<String>();

        if (detailPictureList == null || detailPictureList.size()==0){
            modelMap.addAttribute(
                    CrowdConstant.ATTR_NAME_MESSAGE,
                    "no detail picture found");

            return "project-launch";
        }

        for (MultipartFile detailPicture:detailPictureList) {
            if (!detailPicture.isEmpty()){
                ResultEntity<String> detailUploadResultEntity = CrowdUtil.uploadFileToOss(
                        ossProperties.getEndPoint(),
                        ossProperties.getAccessKeyId(),
                        ossProperties.getAccessKeySecret(),
                        detailPicture.getInputStream(),
                        ossProperties.getBucketName(),
                        ossProperties.getBucketDomain(),
                        detailPicture.getOriginalFilename());

                String detailUploadResult = detailUploadResultEntity.getResult();

                if (ResultEntity.SUCCESS.equals(detailUploadResult)){
                    String detailPicturePath = detailUploadResultEntity.getData();
                    detailPicturePathList.add(detailPicturePath);
                }
            }
        }

        projectVO.setDetailPicturePathList(detailPicturePathList);

        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);
        return "redirect:http://localhost/project/return/info/page";
    }

    @RequestMapping("/get/project/detail/{projectId}")
    public String getDetailProject(@PathVariable("projectId") Integer projectId, ModelMap modelMap){
        ResultEntity<DetailProjectVO> resultEntity = mySQLRemoteService.getDetailProjectVORemote(projectId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            DetailProjectVO detailProjectVO = resultEntity.getData();
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_DETAIL_PROJECT,detailProjectVO);
        }
        return "project-show-detail";
    }

}
