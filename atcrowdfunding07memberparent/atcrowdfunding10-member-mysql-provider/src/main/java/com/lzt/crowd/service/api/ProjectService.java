package com.lzt.crowd.service.api;

import com.lzt.crowd.entity.vo.DetailProjectVO;
import com.lzt.crowd.entity.vo.PortalTypeVO;
import com.lzt.crowd.entity.vo.ProjectVO;

import java.util.List;

public interface ProjectService {
    void saveProject(ProjectVO projectVO, Integer memberId);

    List<PortalTypeVO> getPortalTypeVOList();

    DetailProjectVO getDetailProjectVO(Integer projectId);
}
