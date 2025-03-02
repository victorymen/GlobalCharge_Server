package org.appserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
    private String yh;
    //原价
    private String yj;
    //运营商
    private String yys;
    //有效期
    private String yxq;

    private String currency;

    private double servicerate;

    private int chargetype;
}
