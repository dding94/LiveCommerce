package com.flab.livecommerce.infrastructure.order.persistence.mybatis;

import com.flab.livecommerce.domain.order.OrderItemOption;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyBatisOrderItemOptionRepository {

    void save(OrderItemOption orderItemOption);
}
