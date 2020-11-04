package com.fridge.skl.service;

import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.dto.*;
import com.fridge.skl.entity.*;
import com.fridge.skl.model.unilifemodel.*;
import com.fridge.skl.util.BeanUtils;
import com.fridge.skl.util.HttpUtil;
import com.fridge.skl.util.RedisUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.fridge.skl.util.URL_Constants.ACTION_GETNEWPUBLICFOODLIST;
import static com.fridge.skl.util.URL_Constants.URL_UNILIFEMEDIA;

@Service
public class AsynfoodService {
    //当前db当中的食材列表版本
    //private static String foodlistversion = "1587031430000";

    @Autowired
    UnlifeFoodMapper unlifeFoodMapper;

    @Autowired
    UnlifeElementMapper unlifeElementMapper;

    @Autowired
    UnlifeTogetherfoodMapper unlifeTogetherfoodMapper;

    @Autowired
    UnlifeRelativefoodMapper unlifeRelativefoodMapper;

    @Autowired
    UnlifeFoodAdditionalMapper unlifeFoodAdditionalMapper;

    @Autowired
    RedisUtil redisUtil;

    public int getFoodInfolist() {

        JSONObject json = new JSONObject();
        if (redisUtil.hasKey("foodlistversion")) {
            json.put("updateDate", redisUtil.get("foodlistversion"));
        }
        String res = HttpUtil.postJson(URL_UNILIFEMEDIA + ACTION_GETNEWPUBLICFOODLIST + "?access_token=" + HttpUtil.getToken(), json.toJSONString());
        Gson gson = new Gson();
        int count = 0;
        if (!StringUtils.isEmpty(res)) {
            FoodListResponse foodlist = gson.fromJson(res, FoodListResponse.class);
            if (foodlist != null && "0".equals(foodlist.getErrorno())) {
                UnlifeFood unlifeFood;
                UnlifeElement unlifeElement;
                UnlifeRelativefood unlifeRelativefood;
                UnlifeTogetherfood unlifeTogetherfood;
                UnlifeFoodAdditional unlifeFoodAdditional;
                if (foodlist.getTotal() != 0) {
                    //foodlistversion = foodlist.getVersion();
                    redisUtil.set("foodlistversion", foodlist.getVersion());
                    //永久化
                    redisUtil.persist("foodlistversion");
                    for (Food food : foodlist.getData()) {
                        unlifeFood = new UnlifeFood();
                        unlifeElement = new UnlifeElement();
                        unlifeTogetherfood = new UnlifeTogetherfood();
                        unlifeRelativefood = new UnlifeRelativefood();
                        unlifeFoodAdditional = new UnlifeFoodAdditional();
                        //插入食材列表
                        BeanUtils.copyPropertiesASM(food, unlifeFood);
                        count += unlifeFoodMapper.inserorupdate(unlifeFood);



                        //插入营养成分
                        for (Element element : food.getElement()) {
                            BeanUtils.copyPropertiesASM(element, unlifeElement);
                            count += unlifeElementMapper.updateOrUpdate(unlifeElement);
                        }
                        //插入搭配食材
                        for (TogetherFood togetherFood : food.getTogetherFood()) {
                            BeanUtils.copyPropertiesASM(togetherFood, unlifeTogetherfood);
                            unlifeTogetherfood.setFoodid(food.getId());
                            count += unlifeTogetherfoodMapper.insertOrUpadte(unlifeTogetherfood);
                        }
                        //插入食材相克
                        for (RelativeFood relativeFood : food.getRelativeFood()) {
                            BeanUtils.copyPropertiesASM(relativeFood, unlifeRelativefood);
                            unlifeRelativefood.setFoodid(food.getId());
                            count += unlifeRelativefoodMapper.insertOrUpadte(unlifeRelativefood);
                        }
                        //附加信息
                        for (Additional additional : food.getAdditional()) {
                            unlifeFoodAdditional.setFoodid(food.getId());
                            unlifeFoodAdditional.setKey(additional.getKey());
                            unlifeFoodAdditional.setValue(additional.getValue());
                            unlifeFoodAdditionalMapper.insertOrUpdate(unlifeFoodAdditional);
                        }

                    }
                }
            }

        }
        return count;
    }

}
