package com.itheima.mp.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class AddressMapperTest {
    @Resource
    private AddressMapper addressMapper;

    @Test
    void testLogicDeleteAddress() {
        addressMapper.deleteById(59L);
    }
}