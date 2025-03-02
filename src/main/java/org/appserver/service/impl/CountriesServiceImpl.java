package org.appserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.appserver.dao.CountriesDao;
import org.appserver.entity.Countries;
import org.appserver.service.CountriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 国家(Countries)表服务实现类
 *
 * @author makejava
 * @since 2025-02-18 00:02:31
 */
@Service("countriesService")
public class CountriesServiceImpl extends ServiceImpl<CountriesDao, Countries> implements CountriesService {
    @Autowired
    private CountriesDao countriesDao;

    @Override
    public List<Countries> getAllCountry() {

        List<Countries> result = new ArrayList<>();
        List<Countries> countries = countriesDao.selectList(null);
        for (Countries country : countries) {
            if (country.getIsopen() == 1) {
                result.add(country);
            }
        }
        return result;
    }
}

