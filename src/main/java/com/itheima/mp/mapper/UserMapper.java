package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.mp.domain.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<UserPO> {
    @Update("UPDATE user SET balance = balance - #{money} WHERE id = #{id}")
    void deductBalanceById(@Param("id") Long id, @Param("money") Integer money);
}
