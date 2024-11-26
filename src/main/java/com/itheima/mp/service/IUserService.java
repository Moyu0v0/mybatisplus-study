package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.po.UserPO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;

import java.util.List;

public interface IUserService extends IService<UserPO> {
    /**
     * 根据id扣减余额
     *
     * @param id    用户id
     * @param money 扣减金额
     */
    void deductBalanceById(Long id, Integer money);

    /**
     * 根据复杂条件查询用户
     *
     * @param name       名字
     * @param status     状态
     * @param minBalance 最小余额
     * @param maxBalance 最大余额
     * @return {@link List }<{@link UserPO }>
     */
    List<UserPO> queryUsers(String name, UserStatus status, Integer minBalance, Integer maxBalance);

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return {@link UserVO }
     */
    UserVO queryUserById(Long id);

    /**
     * 根据id批量查询用户
     *
     * @param ids 用户id集合
     * @return {@link List }<{@link UserVO }>
     */
    List<UserVO> queryUsersByIds(List<Long> ids);
}
