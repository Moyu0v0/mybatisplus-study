package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * UserController
 *
 * @author sundae
 * @date 2024/11/20
 */
@RestController
@RequestMapping("/users")
@Api(tags = "用户管理接口")
public class UserController {
    @Resource
    IUserService userService;

    /**
     * 新增用户
     *
     * @param userFormDTO 用户表单dto
     */
    @PostMapping
    @ApiOperation("新增用户")
    public void addUser(@ApiParam("用户表单实体") @RequestBody UserFormDTO userFormDTO) {
        User user = BeanUtil.copyProperties(userFormDTO, User.class);
        userService.save(user);
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除用户")
    public void deleteUser(@ApiParam("用户id") @PathVariable("id") Long id) {
        userService.removeById(id);
    }

    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return {@link UserVO }
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询用户")
    public UserVO queryById(@ApiParam("用户id") @PathVariable("id") Long id) {
        User user = userService.getById(id);
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    /**
     * 根据id批量查询用户
     *
     * @param ids 用户id
     * @return {@link List }<{@link UserVO }>
     */
    @GetMapping
    @ApiOperation("根据id批量查询用户")
    public List<UserVO> queryBatchById(@ApiParam("用户id集合") @RequestParam("ids") List<Long> ids) {
        List<User> users = userService.listByIds(ids);
        return BeanUtil.copyToList(users, UserVO.class);
    }

    /**
     * 根据id扣除余额
     *
     * @param id    用户id
     * @param money 扣减金额
     */
    @PutMapping("/{id}/deduction/{money}")
    @ApiOperation("根据id扣减余额")
    public void deductBalanceById(@ApiParam("用户id") @PathVariable("id") Long id,
                                  @ApiParam("扣减金额") @PathVariable("money") Integer money) {
        userService.deductBalanceById(id, money);
    }
}

