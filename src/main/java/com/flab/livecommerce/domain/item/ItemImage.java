package com.flab.livecommerce.domain.item;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemImage {

    private Long id;

    private String name;

    // 썸네일(필수) 이미지 여부
    private boolean basic;
    private String url;

    private Long itemId;


    @Builder
    public ItemImage(Long id, String name, String url, Long itemId) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.itemId = itemId;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
