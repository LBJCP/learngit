package com.fridge.skl.dto;

import com.fridge.skl.entity.FoodDeviceSpecial;

public interface FoodDeviceSpecialDao {
    int deleteByPrimaryKey(Integer id);

    int insert(FoodDeviceSpecial record);

    int insertSelective(FoodDeviceSpecial record);

    FoodDeviceSpecial selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FoodDeviceSpecial record);

    int updateByPrimaryKey(FoodDeviceSpecial record);
}