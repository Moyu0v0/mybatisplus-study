package com.itheima.mp.mapper;

import com.itheima.mp.domain.po.UserPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsert() {
        UserPO user = new UserPO();
        // user.setId(5L);
        user.setUsername("Lucy");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        // userMapper.save(user);
        userMapper.insert(user);
    }

    @Test
    void testSelectById() {
        // User user = userMapper.queryUserById(5L);
        UserPO user1 = userMapper.selectById(5L);
        System.out.println("user = " + user1);
    }


    @Test
    void testQueryByIds() {
        // List<User> users = userMapper.queryUserByIds(List.of(1L, 2L, 3L, 4L));
        List<UserPO> users = userMapper.selectBatchIds(List.of(1L, 2L, 3L, 4L));
        users.forEach(System.out::println);

    }

    @Test
    void testUpdateById() {
        UserPO user = new UserPO();
        user.setId(5L);
        user.setBalance(20000);
        userMapper.updateById(user);
        // userMapper.updateUser(user);
    }

    @Test
    void testDeleteUser() {
        userMapper.deleteById(5L);
        // userMapper.deleteUser(5L);
    }
}