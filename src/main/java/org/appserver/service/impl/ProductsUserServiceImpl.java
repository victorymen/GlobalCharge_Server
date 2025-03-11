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
import org.appserver.utils.IDUtils;
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
        if (productsUser.getChargetype() == "0") {
            chargeByXiaoLa(productsUser);
        } else if (productsUser.getChargetype() == "1") {
            chargeByCard(productsUser);
        }
        productsUser.setOrderstatus(OrderStatus.CHARGEING.getCode());
        productsUserDao.updateById(productsUser);
        return null;
    }

    private String chargeByCard(ProductsUser productsUser) {
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
        return null;
    }

    /*
     * 调用第三方接口充值
     *
     */
    private void chargeByXiaoLa(ProductsUser productsUser) {
        productsUser.setSernober(IDUtils.genItemId() + "");
//       JSONObject result = userinfoService.globe_Api("/topup",new JSONObject(){{
//            put("recharge_no",productsUser.getRechargeNo());
//            put("user_order_no",productsUser.getSernober());
//            put("product_code",productsUser.getProid());
//        }});
//        System.out.println(result.toString());
//        if(result.getString("code").equals("10000")){
//            productsUser.setOrderNo(result.getJSONObject("result").getString("order_no"));
//        }
    }
}

