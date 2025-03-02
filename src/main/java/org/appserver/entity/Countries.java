package org.appserver.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * 国家(Countries)表实体类
 *
 * @author makejava
 * @since 2025-02-18 00:02:19
 */
@SuppressWarnings("serial")
@Data
public class Countries extends Model<Countries> {
    //国家 id
    private Integer id;
    //国家
    private String cname;
    //国家英文名称
    private String ename;
    //货币单位
    private String currencycode;
    //国家编码
    private String countrycode;
    //国家号码区号
    private String areanumber;
    // 开关控制
    private int isopen;
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

