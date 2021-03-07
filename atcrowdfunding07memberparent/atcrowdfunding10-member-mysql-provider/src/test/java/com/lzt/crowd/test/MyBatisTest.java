package com.lzt.crowd.test;


import com.lzt.crowd.entity.po.MemberPO;
import com.lzt.crowd.entity.vo.DetailProjectVO;
import com.lzt.crowd.mapper.MemberPOMapper;
import com.lzt.crowd.mapper.ProjectPOMapper;
import com.lzt.crowd.service.api.ProjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBatisTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Autowired
    private ProjectPOMapper projectPOMapper;


    @Autowired
    private ProjectService projectService;

    private Logger logger = LoggerFactory.getLogger(MyBatisTest.class);

    @Test
    public void testMapper(){
        MemberPO memberPO = memberPOMapper.selectByPrimaryKey(1);
        logger.debug(memberPO.toString());
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();

        logger.debug(connection.toString());
    }

    @Test
    public void testProject(){
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(5);
        logger.debug(detailProjectVO.toString());
    }

    @Test
    public void testProject2(){
        DetailProjectVO detailProjectVO = projectService.getDetailProjectVO(5);
        logger.debug(detailProjectVO.toString());
    }

}
