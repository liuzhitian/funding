package com.lzt.crowd.mvc.config;


import com.lzt.crowd.entity.Admin;
import com.lzt.crowd.entity.Role;
import com.lzt.crowd.service.api.AdminService;
import com.lzt.crowd.service.api.AuthService;
import com.lzt.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CrowdUserDetailService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin admin = adminService.getAdminByLoginAcct(username);

        Integer adminId = admin.getId();

        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);

        List<String> authNameList = authService.getAssignedAuthNameByAdminId(adminId);

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role:assignedRoleList
             ) {
            String roleName = "ROLE_" + role.getName();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
            authorities.add(authority);
        }

        for (String authName:authNameList
             ) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(authName);
            authorities.add(authority);
        }
        SecurityAdmin securityAdmin = new SecurityAdmin(admin, authorities);
        return securityAdmin;
    }
}
