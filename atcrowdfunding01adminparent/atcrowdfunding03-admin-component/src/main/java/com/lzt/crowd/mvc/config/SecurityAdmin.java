package com.lzt.crowd.mvc.config;


import com.lzt.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class SecurityAdmin extends User {

    private static final long serialVersionUID = 420L;
    private Admin originalAdmin;

    public SecurityAdmin(Admin originalAdmin, Collection<GrantedAuthority> authorities) {
        super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);

        this.originalAdmin = originalAdmin;
        this.originalAdmin.setUserPswd(null);
    }

    public Admin getOriginalAdmin() {
        return originalAdmin;
    }

}
