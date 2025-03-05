package org.appserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.appserver.entity.Countries;
import org.appserver.entity.Products;
import org.appserver.entity.ProductsCard;

import java.util.ArrayList;
import java.util.List;

public interface ProductsCardDao extends BaseMapper<ProductsCard> {

    @Select("${sqlStr}")
    ArrayList<ProductsCard> findArrayMap(@Param(value = "sqlStr") String sqlStr);
}
