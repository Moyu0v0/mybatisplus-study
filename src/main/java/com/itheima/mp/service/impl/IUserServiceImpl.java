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
import java.util.List;

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
        List<AddressPO> list = Db.lambdaQuery(AddressPO.class)
                .eq(AddressPO::getUserId, id)
                .list();
        // 3. 包装并返回
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        if (CollUtil.isNotEmpty(list)) {
            userVO.setAddress(BeanUtil.copyToList(list, AddressVO.class));
        }
        return userVO;
    }
}

