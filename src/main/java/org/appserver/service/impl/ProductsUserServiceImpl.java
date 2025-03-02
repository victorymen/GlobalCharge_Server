package org.appserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.appserver.dao.CardStockDao;
import org.appserver.dao.ProductsUserDao;
import org.appserver.entity.CardStock;
import org.appserver.entity.ProductsUser;
import org.appserver.publicEnum.OrderStatus;
import org.appserver.service.ProductsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单记录(ProductsUser)表服务实现类
 *
 * @author makejava
 * @since 2025-02-18 00:02:32
 */
@Service("productsUserService")
public class ProductsUserServiceImpl extends ServiceImpl<ProductsUserDao, ProductsUser> implements ProductsUserService {
    @Autowired
    ProductsUserDao productsUserDao;
    @Autowired
    CardStockDao cardStockDao;

    public String saveOrder(ProductsUser productsUser) {
        productsUser.setOrderstatus(OrderStatus.PAID.getCode());
        productsUserDao.insert(productsUser);
        if (productsUser.getChargetype().equals(1)) {
            QueryWrapper<CardStock> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sellPice", productsUser.getAmount());
            queryWrapper.eq("isUsed", false);
            queryWrapper.orderByAsc("id");
            CardStock cardStock = new CardStock();
            try {
                cardStock = cardStockDao.selectOne(queryWrapper);
            } catch (Exception e) {
                // TODO 引入日志系统
            }
            if (!StringUtils.isEmpty(cardStock.getCardpassword())) {
                return cardStock.getCardpassword();
            }
        }
        productsUser.setOrderstatus(OrderStatus.CHARGEING.getCode());
        productsUserDao.updateById(productsUser);
        return null;
    }
}

