package com.fridge.skl.service;

import com.fridge.skl.dto.FoodLocationMapper;
import com.fridge.skl.dto.UnlifeFoodMapper;
import com.fridge.skl.dto.UnlifeRelativefoodMapper;
import com.fridge.skl.dto.UnlifeTogetherfoodMapper;
import com.fridge.skl.entity.FoodLocation;
import com.fridge.skl.entity.UnlifeFood;
import com.fridge.skl.entity.UnlifeRelativefood;
import com.fridge.skl.entity.UnlifeTogetherfood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CacheService {
    @Autowired
    UnlifeFoodMapper unlifeFoodMapper;
    @Autowired
    UnlifeTogetherfoodMapper unlifeTogetherfoodMapper;
    @Autowired
    UnlifeRelativefoodMapper unlifeRelativefoodMapper;
    @Autowired
    FoodLocationMapper foodLocationMapper;

    @Cacheable(cacheNames = "publicfoodlist", key = "#name", unless = "#result == null")
    public UnlifeFood getPublicFoodInfoByName(String name) {
        UnlifeFood food = new UnlifeFood();
        food.setName(name);
        return unlifeFoodMapper.selectByName(food);
    }

    @Cacheable(cacheNames = "foodtogetherlist", unless = "#result == null")
    public List<UnlifeTogetherfood> getTogetherfood(int id) {

        return unlifeTogetherfoodMapper.selectByFoodid(id);
    }

    @Cacheable(cacheNames = "foodrelativelist", unless = "#result == null")
    public List<UnlifeRelativefood> getRelativefood(int id) {

        return unlifeRelativefoodMapper.selectByFoodid(id);
    }

    @Cacheable(cacheNames = "foodnamelist", unless = "#result == null")
    public List<String> getfoodlist() {

        return unlifeFoodMapper.selectAllfoodname();
    }

    @Cacheable(cacheNames = "foodlocation", unless = "#result == null")
    public FoodLocation getfoodlocation(String devicetype, String foodname) {

        return foodLocationMapper.selectFoodlocation(devicetype, foodname);
    }


}
