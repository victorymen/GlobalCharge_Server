package org.appserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;

@Data
@TableName("products_card")
public class ProductsCard {
    //产品 id
    private Integer id;

    private String amount;
    //国家
    private String cname;

    private String typename;
    //产品描述
    private String description;
    //产品名
    private String title;

    private String type;
    //国家 id
    private String countryid;
    //优惠价
    @TableField(value = "sellingprice")
    private String yh;
    //原价
    @TableField(value = "purchaseprice")
    private String yj;
    //运营商
    @TableField(value = "operatorname")
    private String yys;
    //有效期
    @TableField(value = "validityperiod")
    private String ysq;
    //币种缩写
    private String currency;
    //服务费
    private double servicerate;
    //充值类型
    private int chargetype;
    //库存
    private BigInteger total;
    //汇率
    private int exchangerate;
}
