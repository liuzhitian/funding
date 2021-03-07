package com.lzt.crowd.test;

import com.lzt.crowd.entity.Admin;
import com.lzt.crowd.mapper.AdminMapper;
import com.lzt.crowd.service.api.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Test
    public void testTx(){
        //Admin admin = new Admin(null, "jerry", "1233123", "jerrrry", "jerry@qq.com", null);
        //adminService.saveAdmin(admin);
    }

    @Test
    public void logTest(){

        Logger logger = LoggerFactory.getLogger(CrowdTest.class);
        logger.debug("dddddebug");
        logger.info("iiinfo");
        logger.warn("wwwwarn");
        logger.error("eeeeerror");

    }


    @Test
    public void testMapper(){

        Admin admin = adminMapper.selectByPrimaryKey(1);
        System.out.println(admin);
    }


    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);

    }

}
