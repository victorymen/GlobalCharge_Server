package org.appserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单记录(ProductsUser)表实体类
 *
 * @author makejava
 * @since 2025-02-18 01:21:16
 */
@Data
@SuppressWarnings("serial")
public class ProductsUser extends Model<ProductsUser> {
    //订单idid
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String amount;
    //国家
    private String cname;

    private String typename;
    //产品描述
    private String description;
    //产品名
    private String title;

    private Integer type;
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
    //用户id
    private String openid;
    //订单编号
    private String sernober;
    //充值号码
    private String rechargeNo;
    //实际支付金额
    private String userpayamount;
    //支付状态
    private String userpaystate;
    //订单状态
    private String sernoberstate;
    //下单时间
    private Date ordertime;
    //支付时间
    private Date paymenttime;
    //产品id
    private Integer proid;
    //平台订单号
    private String orderNo;
    //充值方法
    private String chargetype;
//    //订单状态
//    private String orderstatus;
    //商户订单号
    private String outTradeNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }


    public void setSernober(String sernober) {
        this.sernober = sernober;
    }



    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

