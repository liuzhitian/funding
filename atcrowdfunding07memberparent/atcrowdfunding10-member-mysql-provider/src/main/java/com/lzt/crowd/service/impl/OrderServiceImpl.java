package com.lzt.crowd.service.impl;

import com.lzt.crowd.entity.po.AddressPO;
import com.lzt.crowd.entity.po.AddressPOExample;
import com.lzt.crowd.entity.po.OrderPO;
import com.lzt.crowd.entity.po.OrderProjectPO;
import com.lzt.crowd.entity.vo.AddressVO;
import com.lzt.crowd.entity.vo.OrderProjectVO;
import com.lzt.crowd.entity.vo.OrderVO;
import com.lzt.crowd.mapper.AddressPOMapper;
import com.lzt.crowd.mapper.OrderPOMapper;
import com.lzt.crowd.mapper.OrderProjectPOMapper;
import com.lzt.crowd.service.api.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderProjectPOMapper orderProjectPOMapper;

    @Autowired
    private AddressPOMapper addressPOMapper;

    private OrderPOMapper orderPOMapper;


    @Override
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {
        return orderProjectPOMapper.selectOrderProjectVO(projectId,returnId);
    }

    @Override
    public List<AddressVO> getAddressListVOByMemberId(Integer memberId) {

        AddressPOExample example = new AddressPOExample();
        example.createCriteria().andMemberIdEqualTo(memberId);
        List<AddressPO> addressPOList = addressPOMapper.selectByExample(example);

        List<AddressVO> addressVOList = new ArrayList<>();
        for (AddressPO addressPO : addressPOList) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO,addressVO);
            addressVOList.add(addressVO);
        }
        return addressVOList;
    }

    @Transactional(readOnly = false,
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class)
    @Override
    public void saveAddressPO(AddressVO addressVO) {
        AddressPO addressPO = new AddressPO();
        BeanUtils.copyProperties(addressVO,addressPO);
        addressPOMapper.insert(addressPO);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveOrder(OrderVO orderVO) {
        // ??????OrderPO??????
        OrderPO orderPO = new OrderPO();
        // ????????????OrderVO???OrderPO??????
        BeanUtils.copyProperties(orderVO,orderPO);
        // ???OrderPO???????????????
        orderPOMapper.insert(orderPO);
        // ??????????????????????????????order id
        Integer orderId = orderPO.getId();
        // ??????orderProjectVO
        OrderProjectVO orderProjectVO = orderVO.getOrderProjectVO();
        // ??????OrderProjectPO??????
        OrderProjectPO orderProjectPO = new OrderProjectPO();
        // ??????
        BeanUtils.copyProperties(orderProjectVO,orderProjectPO);
        // ???orderProjectPO??????orderId
        orderProjectPO.setOrderId(orderId);
        // ???????????????
        orderProjectPOMapper.insert(orderProjectPO);
    }
}
