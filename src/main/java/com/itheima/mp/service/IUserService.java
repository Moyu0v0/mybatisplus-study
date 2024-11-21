package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.po.UserPO;
import com.itheima.mp.domain.vo.UserVO;

import java.util.List;

public interface IUserService extends IService<UserPO> {
    void deductBalanceById(Long id, Integer money);

    List<UserPO> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance);

    /**
     * 根据id查询用户
     *
     * @param id id
     * @return {@link UserVO }
     */
    UserVO queryUserById(Long id);
}
