package org.appserver.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.appserver.dao.CountriesDao;
import org.appserver.dao.ProductsCardDao;
import org.appserver.dao.ProductsDao;
import org.appserver.dao.RechargeTypesDao;
import org.appserver.entity.Products;
import org.appserver.entity.ProductsCard;
import org.appserver.entity.RechargeTypes;
import org.appserver.service.ProductsService;
import org.appserver.service.UserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.appserver.utils.utils.removeEmptyFields;

/**
 * 产品(Products)表服务实现类
 *
 * @author makejava
 * @since 2025-02-18 00:02:31
 */
@Service("productsService")
public class ProductsServiceImpl extends ServiceImpl<ProductsDao, Products> implements ProductsService {
    @Autowired
    private ProductsCardDao productsCardDao;

    @Autowired
    RechargeTypesDao rechargeTypesDao;
    @Resource
    private UserinfoService userinfoService;

    /**
     * 根据产品类型查询产品列表 数据库版本
     *
     * @param countryId
     * @return
     */
    @Override
    public JSONObject findProducts(String countryId) {
        String sqlStr = "SELECT DISTINCT rt.*  FROM recharge_types rt WHERE EXISTS ( SELECT 1 FROM products s  WHERE s.countryId = '" + countryId + "'  AND s.`type` = rt.`type` ) ORDER BY rt.`type`;";
        ArrayList<RechargeTypes> arrayMap = rechargeTypesDao.findArrayMap(sqlStr);
        JSONArray array = removeEmptyFields(arrayMap);
        JSONObject result = new JSONObject();
        for (Object item : array) {
            if (!result.containsKey("reType")) {
                result.put("reType", 0);
            }
            JSONObject object = (JSONObject) item;
            object.put("content", findProducts(countryId, object.getString("type"), result));
        }
        result.put("content", array);
        return result;
    }

    private JSONArray findProducts(String countryId, String type, JSONObject items) {

        String sqlStr = " select yys from  products s where s.countryId ='" + countryId + "' and  s.`type`= '" + type + "' group  by yys";
        ArrayList<Products> arrayMap = baseMapper.findArrayMap(sqlStr);
        JSONArray array = removeEmptyFields(arrayMap);
        for (Object item : array) {
            if (!items.containsKey("yysType")) {
                items.put("yysType", 0);
            }
            JSONObject object = (JSONObject) item;
            object.put("content", products(countryId, type, object.getString("yys"), items));
        }
        return array;
    }

    private JSONArray products(String countryId, String type, String yys, JSONObject items) {
        String sqlStr = " select * from  products s where s.countryId ='" + countryId + "' and  s.`type`= '" + type + "' and  s.yys= '" + yys + "' ";
        ArrayList<Products> arrayMap = baseMapper.findArrayMap(sqlStr);
        JSONArray array = removeEmptyFields(arrayMap);
        if (array.size() > 0)
            items.put("proType", 0);
        return array;
    }


    /**
     * 根据产品类型查询产品列表 接口版本
     *
     * @param countryId
     * @return
     */
    public JSONObject products(String countryId) {
        //对应机构 先获取自己从产品的充值类型  无则从接口获取
        JSONObject jsonObject = userinfoService.getObject("/recharge_types", new JSONObject() {{
            put("country", countryId);
        }});
        JSONObject result = new JSONObject();
        JSONArray jsonArray = jsonObject.getJSONArray("types");
        for (int i = 0; i < jsonArray.size(); i++) { //充值类型获取 话费 流程 密码 等充值方式 接口返回的类型
            JSONObject object = jsonArray.getJSONObject(i);
            if (!result.containsKey("reType")) {
                result.put("reType", 0);
            }
            object.put("content", jsonObject(countryId, object.getString("type"),object.getString("name"), result));
        }
        aaa(countryId,jsonArray);
        result.put("content", jsonArray);
        return result;
    }

    private JSONArray jsonObject(String countryId, String type,String typeName, JSONObject result) {
        JSONObject jsonObject = userinfoService.getObject("/products", new JSONObject() {{
            put("country", countryId);
            put("type", type);
        }});
        JSONArray jsonArray = jsonObject.getJSONArray("products");
        return groupByYys(jsonArray, result,typeName, countryId);
    }

    private JSONArray groupByYys(JSONArray jsonArray, JSONObject result1,String typeName, String countryId) {
        JSONObject groupedResult = new JSONObject();
        // 遍历 jsonArray
        for (int i = 0; i < jsonArray.size(); i++) {
            if (!result1.containsKey("yysType")) {
                result1.put("yysType", 0);
            }
            JSONObject item = jsonArray.getJSONObject(i);
            item.put("typeName", typeName);
            String yysValue = item.getString("yys");
            // 创建一个内容数组，如果 yysValue 不存在则初始化
            if (!groupedResult.containsKey(yysValue)) {
                groupedResult.put(yysValue, new JSONArray());
            }
            // 将当前项添加到对应的 yysValue 数组中
            groupedResult.getJSONArray(yysValue).add(item);
        }
        JSONArray result = new JSONArray();
        for (String key : groupedResult.keySet()) {
            result.add(new JSONObject() {{
                put("type", key);
                put("content", groupedResult.getJSONArray(key));
            }});
        }
//        getProductCard(result, countryId);
        return result;
    }

    private void getProductCard(JSONArray products, String coutryId) {
        QueryWrapper<ProductsCard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("countryId", coutryId);
        List<ProductsCard> productsCard = productsCardDao.selectList(queryWrapper);
        for (int i = 0; i < products.size(); i++) {
            JSONObject product = products.getJSONObject(i);
            JSONArray contents = new JSONArray();
            for (ProductsCard productCard : productsCard) {
                if (product.getString("type").trim().equals(productCard.getYys().trim())) {
                    contents.add(productCard);
                    product.put("content", contents);
                }
            }
        }
    }


    private void aaa(String coutryId,JSONArray items1) {
        String sqlStr = " select type_name typeName  from products_card where  countryId='" + coutryId + "'  GROUP BY type_name";
        ArrayList<ProductsCard> arrayMap = productsCardDao.findArrayMap(sqlStr);

            arrayMap.forEach(items -> {
                JSONArray array = compareArray(items.getTypeName(), items1);
                JSONObject jsonObject = new JSONObject() {{
                    put("type", items.getTypeName());
                    put("content", bbb(coutryId, items.getTypeName(),array));
                }};
                if(array==null){
                    items1.add(jsonObject);
                }
            });

    }

    private JSONArray bbb(String coutryId, String typeName,JSONArray items1) {
        String sqlStr = " select operatorname yys from products_card where  countryId='" + coutryId + "' and type_name='" + typeName + "'  GROUP BY operatorName";
        ArrayList<ProductsCard> arrayMap = productsCardDao.findArrayMap(sqlStr);

        return new JSONArray() {{
            arrayMap.forEach(items -> {
                JSONObject jsonObject = compareObject(items.getYys(), items1);
                List<ProductsCard> ccc = ccc(coutryId, typeName, items.getYys());
                if(jsonObject==null){
                    jsonObject.put("name", items.getYys());
                    jsonObject.put("content", ccc);
                }else{
                    jsonObject.getJSONArray("content").clear();
                    jsonObject.getJSONArray("content").addAll(ccc);
                }
            });
        }};

    }

    private List<ProductsCard> ccc(String coutryId, String typeName, String operatorName) {
//        String sqlStr = " select * from products_card where  countryId='" + coutryId + "' and typeName='" + typeName + "' and operatorName='" + operatorName + "' ";
        QueryWrapper<ProductsCard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("countryId", coutryId);
        queryWrapper.eq("type_name", typeName);
        queryWrapper.eq("operatorName", operatorName);
        List<ProductsCard> productsCard = productsCardDao.selectList(queryWrapper);
        return productsCard;
    }

    private JSONArray compareArray(String name,JSONArray items) {
        for (int i = 0; i < items.size(); i++) {
            JSONObject jsonObject = items.getJSONObject(i);
            if(name.equals(jsonObject.getString("name"))){
                return jsonObject.getJSONArray("content");
            }
        }
        return null;
    }

    private JSONObject compareObject(String name,JSONArray items) {
        for (int i = 0; i < items.size(); i++) {
            JSONObject jsonObject = items.getJSONObject(i);
            if(name.equals(jsonObject.getString("type"))){
                return jsonObject;
            }
        }
        return null;
    }

}

