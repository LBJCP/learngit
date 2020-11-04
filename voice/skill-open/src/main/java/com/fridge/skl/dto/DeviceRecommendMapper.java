package com.fridge.skl.dto;

import com.fridge.skl.entity.DeviceRecommend;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface DeviceRecommendMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceRecommend record);

    int insertSelective(DeviceRecommend record);

    DeviceRecommend selectByPrimaryKey(Integer id);

    DeviceRecommend selectByDeviceName(String device);

    DeviceRecommend selectByDeviceCode(String devicecode);

    int updateByPrimaryKeySelective(DeviceRecommend record);

    int updateByPrimaryKey(DeviceRecommend record);
}