package org.appserver.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import org.appserver.entity.Products;
import org.appserver.service.ProductsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 产品(Products)表控制层
 *
 * @author makejava
 * @since 2025-02-18 00:02:31
 */
@RestController
@RequestMapping("/api/products")
public class ProductsController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ProductsService productsService;


    @PostMapping()
    public R selectOne(@RequestBody Products products ) {
        return success(productsService.products(products.getCountryid()));
    }







}

