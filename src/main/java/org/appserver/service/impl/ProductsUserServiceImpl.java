package org.appserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.appserver.dao.CardStockDao;
import org.appserver.dao.ProductsUserDao;
import org.appserver.entity.CardsStock;
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
        productsUserDao.insert(productsUser);
        if(StringUtils.isNotEmpty(productsUser.getSernoberstate())){
            return null;
        }
        productsUser.setSernoberstate(OrderStatus.PAID.getCode());
        if (StringUtils.isNotEmpty(productsUser.getChargetype())) {//含此字段的为自己维护逻辑及充值方式
            if (productsUser.getChargetype().equals("0")) {//接口充值
            } else if (productsUser.getChargetype().equals("1")) {//瓜瓜卡短信
                return  chargeByCard(productsUser);
            } else if (productsUser.getChargetype().equals("2")) {//瓜瓜卡打电话
                return  chargeByCard(productsUser);
            }
        }else{//第三方接口充值
            chargeByXiaoLa(productsUser);
//            productsUser.setSernoberstate(OrderStatus.COMPLETED.getCode());
        }
        productsUserDao.updateById(productsUser);
        return null;
    }

    private String chargeByCard(ProductsUser productsUser) {
        int amount = (int) Double.parseDouble(productsUser.getAmount());
        QueryWrapper<CardsStock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sellPice", amount);
        queryWrapper.eq("isused", 1);
        queryWrapper.orderByAsc("id");
        CardsStock cardStock;
        try {
            cardStock = cardStockDao.selectOne(queryWrapper);
            if (cardStock == null) {
                throw new Exception("未找到可用的卡");
            }
        } catch (Exception e) {
            // 引入日志系统
            System.err.println("查询卡库存时发生错误: " + e.getMessage());
            return null;
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

