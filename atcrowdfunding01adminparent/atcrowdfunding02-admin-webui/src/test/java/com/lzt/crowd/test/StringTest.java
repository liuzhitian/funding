package com.lzt.crowd.test;

import com.lzt.crowd.util.CrowdUtil;
import org.junit.Test;

public class StringTest {

    @Test
    public void testMd5(){
        String source = "123123";
        String encode = CrowdUtil.md5(source);
        System.out.println(encode);

    }
}
