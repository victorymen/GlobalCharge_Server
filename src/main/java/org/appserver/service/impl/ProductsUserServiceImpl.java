package org.appserver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.appserver.dao.CardStockDao;
import org.appserver.dao.ProductsUserDao;
import org.appserver.entity.CardsStock;
import org.appserver.entity.ProductsUser;
import org.appserver.publicEnum.OrderStatus;
import org.appserver.service.ProductsUserService;
import org.appserver.service.UserinfoService;
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
    @Autowired
    UserinfoService userinfoService;


    public void saveOrder(ProductsUser productsUser) { // 无需设置返回值根据充值成功 后回根据订单号在此查询一次即可
        productsUserDao.insert(productsUser);
        if(StringUtils.isNotEmpty(productsUser.getSernoberstate())){//不为空的话直接返回  用于用户点击充值卡充值按钮 直接调用 证明点击用掉了
            return ;
        }
        productsUser.setSernoberstate(OrderStatus.PAID.getCode());//进来的都是已支付过的订单
        if (StringUtils.isNotEmpty(productsUser.getChargetype())) {//含此字段的为自己维护逻辑及充值方式
            if (productsUser.getChargetype().equals("0")) {//接口充值
            }
            else if (productsUser.getChargetype().equals("1")) {//瓜瓜卡短信
                  chargeByCard(productsUser);
            }
            else if (productsUser.getChargetype().equals("2")) {//瓜瓜卡打电话
                  chargeByCard(productsUser);
            }
        }
        else{//第三方接口充值
            chargeByXiaoLa(productsUser);
        }

    }

    private void chargeByCard(ProductsUser productsUser) {
        int amount = (int) Double.parseDouble(productsUser.getAmount());
        QueryWrapper<CardsStock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sellPice", amount);
        queryWrapper.eq("isused", 1); //是否有效？
        queryWrapper.orderByAsc("id");
        CardsStock cardStock;
        try {
            cardStock = cardStockDao.selectOne(queryWrapper);
            if (cardStock == null) {
                throw new Exception("未找到可用的卡");
            }
        } catch (Exception e) {  //引入日志系统
            System.err.println("查询卡库存时发生错误: " + e.getMessage());
            return ;
        }
        if (!StringUtils.isEmpty(cardStock.getCardpassword())) {
            productsUser.setCardPasswd(cardStock.getCardpassword());
            cardStock.setIsused(0);//用过的卡置为无效
            productsUser.setSernoberstate(OrderStatus.COMPLETED.getCode());//卡密已发放 已完成状态
            cardStockDao.updateById(cardStock);
            productsUserDao.updateById(productsUser);//卡密设置进去
        }

    }

    /*
     * 调用第三方接口充值
     */
    private void chargeByXiaoLa(ProductsUser productsUser) {
        productsUser.setSernober(IDUtils.genItemId() + "");
       JSONObject result = userinfoService.globe_Api("/topup",new JSONObject(){{
            put("recharge_no",productsUser.getRechargeNo());
            put("user_order_no",productsUser.getSernober());
            put("product_code",productsUser.getProid());
        }});
        if(result.getString("code").equals("10000")){
            productsUser.setOrderNo(result.getJSONObject("result").getString("order_no"));
            //需要定时任务回访小啦充值接口 根据订单号查看是否充值成功了    TODO   订单号 order_no 小啦接口需要定时轮询查看充值状态 并且回写到数据库
            productsUser.setSernoberstate(OrderStatus.CHARGEING.getCode());
            productsUserDao.updateById(productsUser);
        }
    }
}

