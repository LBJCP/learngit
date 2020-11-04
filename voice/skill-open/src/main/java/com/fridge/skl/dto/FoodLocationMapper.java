package com.fridge.skl.dto;

import com.fridge.skl.entity.FoodLocation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface FoodLocationMapper {

    FoodLocation selectFoodlocation(@Param("devicetype") String devicetype, @Param("foodname") String foodname);


}