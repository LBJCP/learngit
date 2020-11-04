package com.fridge.skl.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.entity.Feedback;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.model.recipe.SmartRecipeFuzzySearchResponse;
import com.fridge.skl.model.unilifemodel.FoodInfo;
import com.fridge.skl.util.HttpUtil;
import com.fridge.skl.util.RecipeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.fridge.skl.util.URL_Constants.*;
import static com.fridge.skl.util.URL_Constants.ACTION_GETFOODLIST;

public class RecipeService extends BaseService {
    @Autowired
    Food2Service food2Service;

    public ResponseObj getRecipe(RequestObj request) {
        ResponseObj resp = initResporse(request);

        String deviceId = request.getContext().getDevice().getDeviceId();
        String devicetype = request.getContext().getDevice().getDeviceType();

        String userdid = request.getContext().getUser().getUserId();
        Feedback feedback = new Feedback();
        feedback.setUserid(userdid);
        feedback.setDeviceid(deviceId);
        feedback.setContent(request.getRequest().getQuery().getContent());
        feedback.setTeiminal(devicetype);
        String content = "";

        List<FoodInfo> foodlist = food2Service.getFoodList(request.getContext().getDevice().getDeviceId());
        List<String> foods = new ArrayList<>();
        if (!CollectionUtils.isEmpty(foodlist)) {
            for (FoodInfo foodinfo : foodlist) {
                foods.add(foodinfo.getName());
            }
        }


        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    private void getRecipeByIngr(){
        JSONObject json = new JSONObject();
        json.put("location", -1);
        String res = HttpUtil.postJson(URL_UNILIFE + ACTION_SEARCHRECIPEBYINGR, json.toJSONString());
    }

    private void getLinkCookRecipe() {

        SmartRecipeFuzzySearchResponse response = new SmartRecipeFuzzySearchResponse();
        response.setCurrentPage(1);
        response.setFridgeType("");
        response.setMacId("");
        response.setPageSize(2);
        response.setUserId("");
        List<String> kws = new ArrayList<>();


        kws.add("黄瓜");
        kws.add("沙拉酱");
        kws.add("茄子");
        kws.add("兔肉");
        kws.add("黄瓜");
        kws.add("螃蟹");
        response.setKw(kws);

        StringBuilder sb = new StringBuilder();
        sb.append("黄瓜");

        List<String> equipmentType = new ArrayList<>();
        response.setEquipmentType(equipmentType);


        RecipeUtils.postByAuthorization(ACTION_RECPIR_FINDRECIPE, JSON.toJSONString(response));

    }

}
