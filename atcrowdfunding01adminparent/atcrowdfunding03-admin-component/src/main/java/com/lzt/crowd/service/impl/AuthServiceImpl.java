package com.lzt.crowd.service.impl;

import com.lzt.crowd.entity.Auth;
import com.lzt.crowd.entity.AuthExample;
import com.lzt.crowd.mapper.AuthMapper;
import com.lzt.crowd.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthMapper authMapper;

    @Override
    public List<Auth> getAll() {
        return authMapper.selectByExample(new AuthExample());
    }

    @Override
    public List<Integer> getAssignedAuthIdByRole(Integer roleId) {

        return authMapper.selectAssignedAuthIdByRole(roleId);
    }

    @Override
    public void saveRoleAuthRelationship(Map<String, List<Integer>> map) {
        List<Integer> roleIdList = map.get("roleId");
        Integer roleId = roleIdList.get(0);

        authMapper.deleteOldRelationship(roleId);

        List<Integer> authIdList = map.get("authIdArray");

        if (authIdList!=null && authIdList.size()>0){
            authMapper.insertNewRelationship(roleId,authIdList);
        }
    }

    @Override
    public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
        return authMapper.selectAssignedAuthNameByAdminId(adminId);
    }
}
