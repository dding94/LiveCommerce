package com.flab.livecommerce.domain.item;

import org.springframework.stereotype.Repository;

@Repository
public interface ItemOptionGroupRepository {

    ItemOptionGroup save(ItemOptionGroup itemOptionGroup);

    ItemOptionGroup update(ItemOptionGroup optionGroup, Long optionGroupId);

    ItemOptionGroup findById(Long itemOptionGroupId);
}
