package com.lzt.crowd.entity.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberLoginVO implements Serializable {

    public static final long serialVersionUID = 1L;
    private Integer id;
    private String username;
    private String email;

}
