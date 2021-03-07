package com.lzt.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.lzt.crowd.entity.Admin;
import com.lzt.crowd.entity.AdminExample;
import com.lzt.crowd.exception.LoginAcctAlreadyInUseException;
import com.lzt.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.lzt.crowd.exception.LoginFailedException;
import com.lzt.crowd.mapper.AdminMapper;
import com.lzt.crowd.service.api.AdminService;
import com.lzt.crowd.util.CrowdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    public void saveAdmin(Admin admin) {

        String userPswd = admin.getUserPswd();
        //userPswd = CrowdUtil.md5(userPswd);
        userPswd = bCryptPasswordEncoder.encode(userPswd);
        admin.setUserPswd(userPswd);

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(date);
        admin.setCreateTime(createTime);

        try {
            adminMapper.insert(admin);
        }catch (Exception e){
            //e.printStackTrace();
            logger.info("qaunleiming"+e.getClass().getName());

            if(e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseException("acct already exist");
            }

        }

    }

    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {

        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();

        criteria.andLoginAcctEqualTo(loginAcct);
        List<Admin> list =  adminMapper.selectByExample(adminExample);

        if(list==null || list.size()==0){
            throw new LoginFailedException("login failed");
        }

        if (list.size()>1){
            throw new RuntimeException("login not unique");
        }

        Admin admin = list.get(0);
        if (admin==null){
            throw new LoginFailedException("login failed");
        }

        String userPswdDB = admin.getUserPswd();
        String userPswdForm = CrowdUtil.md5(userPswd);

        if (!Objects.equals(userPswdDB, userPswdForm)){
            throw new LoginFailedException("login failed");
        }

        return admin;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        List<Admin> adminList = adminMapper.selectAdminByKeyword(keyword);

        return new PageInfo<>(adminList);
    }

    @Override
    public void removeAdmin(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);

    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void update(Admin admin) {
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        }catch (Exception e){
            if (e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseForUpdateException("already exist");
            }
        }


    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        adminMapper.deleteOldRelationship(adminId);

        if(roleIdList!=null || roleIdList.size()>0){
            adminMapper.insertNewRelationship(adminId,roleIdList);
        }

    }

    @Override
    public Admin getAdminByLoginAcct(String username) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(username);

        List<Admin> adminList = adminMapper.selectByExample(adminExample);
        Admin admin = adminList.get(0);
        return admin;
    }

}
