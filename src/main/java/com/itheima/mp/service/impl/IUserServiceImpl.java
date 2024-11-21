package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.po.AddressPO;
import com.itheima.mp.domain.po.UserPO;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * IUserServiceImpl
 *
 * @author sundae
 * @date 2024/11/20
 */
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements IUserService {
    @Resource
    UserMapper userMapper;

    @Override
    @Transactional // 记得在启动类加上@EnableTransactionManagement注解
    public void deductBalanceById(Long id, Integer money) {
        // 1. 查询用户
        UserPO user = getById(id);
        // 2. 校验用户状态
        if (user == null || user.getStatus() == 2) {
            throw new RuntimeException("用户状态异常！");
        }
        // 3. 校验余额是否充足
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足！");
        }
        // 4. 扣减余额
        // userMapper.deductBalanceById(id, money);
        // 4. 如果扣减后余额为0，则将用户status修改为冻结状态（2）
        int remainBalance = user.getBalance() - money;
        lambdaUpdate()
                .set(UserPO::getBalance, remainBalance)
                .set(remainBalance == 0, UserPO::getStatus, 2) // 这里应该避免魔法值
                .eq(UserPO::getId, id)
                .eq(UserPO::getBalance, user.getBalance()) // 乐观锁：先比较后更新
                .update(); // 别忘了最后加上update()，否则不执行
    }

    @Override
    public List<UserPO> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance) {
        return lambdaQuery()
                .like(name != null, UserPO::getUsername, name)
                .eq(status != null, UserPO::getStatus, status)
                .ge(minBalance != null, UserPO::getBalance, minBalance)
                .le(maxBalance != null, UserPO::getBalance, maxBalance)
                .list();
    }

    @Override
    public UserVO queryUserById(Long id) {
        // 1，查用户
        UserPO user = getById(id);
        if (user == null || user.getStatus() == 2) {
            throw new RuntimeException("用户状态异常！");
        }
        // 2. 查地址
        List<AddressPO> addresses = Db.lambdaQuery(AddressPO.class) // Db静态工具可以避免service的相互依赖
                .eq(AddressPO::getUserId, id)
                .list();
        // 3. 包装并返回
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        if (CollUtil.isNotEmpty(addresses)) {
            userVO.setAddresses(BeanUtil.copyToList(addresses, AddressVO.class));
        }
        return userVO;
    }

    @Override
    public List<UserVO> queryUsersByIds(List<Long> ids) {
        // 1. 查用户
        List<UserPO> userPOList = listByIds(ids);
        if (CollUtil.isEmpty(userPOList)) {
            return Collections.emptyList();
        }
        // 2. 获取合法的用户id集合
        List<Long> userIds = userPOList.stream().map(UserPO::getId).collect(Collectors.toList());
        // 3. 查所有用户的地址（在这里使用 in 关键字查询而不是在循环中依次查询的好处是：只需要查数据库一次）
        List<AddressPO> addressPOList = Db.lambdaQuery(AddressPO.class).in(AddressPO::getUserId, userIds).list();
        // 4. 转换地址VO
        List<AddressVO> addressVOList = BeanUtil.copyToList(addressPOList, AddressVO.class);
        // 5. 用户地址集合分类处理：相同用户的地址放入同一个集合中
        Map<Long, List<AddressVO>> addressMap = new HashMap<>();
        if (CollUtil.isNotEmpty(addressVOList)) {
            addressMap = addressVOList.stream().collect(Collectors.groupingBy(AddressVO::getUserId));
        }
        // 6. 包装并返回
        List<UserVO> userVOList = new ArrayList<>(userPOList.size());
        for (UserPO userPO : userPOList) {
            // 6.1 将用户PO转VO
            UserVO userVO = BeanUtil.copyProperties(userPO, UserVO.class);
            // 6.2 设置用户VO的地址VO
            userVO.setAddresses(addressMap.get(userVO.getId()));
            // 6.3 将单个用户加入集合
            userVOList.add(userVO);
        }
        return userVOList;
    }
}

