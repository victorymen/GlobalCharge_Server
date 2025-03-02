package org.appserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.appserver.entity.ProductsUser;
import org.appserver.service.ProductsUserService;
import org.appserver.service.UserinfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * 订单记录(ProductsUser)表控制层
 *
 * @author makejava
 * @since 2025-02-18 00:02:32
 */
@RestController
@RequestMapping("/api/productsUser")
public class ProductsUserController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ProductsUserService productsUserService;

    /**
     * 分页查询所有数据
     *
     * @param page         分页对象
     * @param productsUser 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<ProductsUser> page, ProductsUser productsUser) {
        return success(this.productsUserService.page(page, new QueryWrapper<>(productsUser)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.productsUserService.getById(id));
    }


    @Resource
    private UserinfoService userinfoService;

    /**
     * 新增数据 创建支付订单
     *
     * @param productsUser 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody ProductsUser productsUser) {
        return success(this.productsUserService.saveOrder(productsUser));
    }

    /**
     * 修改数据
     *
     * @param productsUser 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody ProductsUser productsUser) {
        return success(this.productsUserService.updateById(productsUser));
    }


}

