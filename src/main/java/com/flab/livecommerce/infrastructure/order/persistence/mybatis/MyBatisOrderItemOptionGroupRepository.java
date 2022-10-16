package com.flab.livecommerce.infrastructure.order.persistence.mybatis;

import com.flab.livecommerce.domain.order.OrderItemOptionGroup;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyBatisOrderItemOptionGroupRepository {

    void save(OrderItemOptionGroup orderItemOptionGroup);
}
