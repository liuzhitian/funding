package com.lzt.crowd.service.api;

import com.lzt.crowd.entity.vo.AddressVO;
import com.lzt.crowd.entity.vo.OrderProjectVO;
import com.lzt.crowd.entity.vo.OrderVO;

import java.util.List;

public interface OrderService {

    OrderProjectVO getOrderProjectVO(Integer projectId,Integer returnId);

    List<AddressVO> getAddressListVOByMemberId(Integer memberId);

    void saveAddressPO(AddressVO addressVO);

    void saveOrder(OrderVO orderVO);
}
