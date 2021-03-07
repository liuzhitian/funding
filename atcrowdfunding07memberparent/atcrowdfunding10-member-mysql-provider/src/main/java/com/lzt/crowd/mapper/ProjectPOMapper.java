package com.lzt.crowd.mapper;

import com.lzt.crowd.entity.po.ProjectPO;
import com.lzt.crowd.entity.po.ProjectPOExample;
import java.util.List;

import com.lzt.crowd.entity.vo.DetailProjectVO;
import com.lzt.crowd.entity.vo.PortalTypeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public interface ProjectPOMapper {
    int countByExample(ProjectPOExample example);

    int deleteByExample(ProjectPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProjectPO record);

    int insertSelective(ProjectPO record);

    List<ProjectPO> selectByExample(ProjectPOExample example);

    ProjectPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

    int updateByExample(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

    int updateByPrimaryKeySelective(ProjectPO record);

    int updateByPrimaryKey(ProjectPO record);

    void saveTypeRelationship(@Param("projectId") Integer projectId,
                              @Param("typeIdList") List<Integer> typeIdList);

    void saveTagRelationship(@Param("projectId")Integer projectId,
                             @Param("tagIdList") List<Integer> tagIdList);

    List<PortalTypeVO> selectPortalTypeVOList();

    DetailProjectVO selectDetailProjectVO(Integer projectId);


}