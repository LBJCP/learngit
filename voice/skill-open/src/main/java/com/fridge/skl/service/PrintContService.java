package com.fridge.skl.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.entity.UnlifeFood;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *
 */
@Component()
public class PrintContService extends BaseService {

    private static final String TYPEID_1 = "200861051c4085040121006180052542410000000000618003514d0000000040";
    private static final String TYPEID_2 = "200861051c408504012100618005394300000000000061800616440000000140";
    private static final String TYPEID_3 = "200861051c408504012800618003474144000000000061800312415100000040";
    //可用的无屏语音冰箱
    private static final String[] typeid = new String[]{
            TYPEID_1, TYPEID_2, TYPEID_3
    };
    @Autowired
    CacheService cacheService;

    /**
     * 打印食材标签
     *
     * @param request 请求
     * @return 回调
     */
    public ResponseObj printfood(RequestObj request) {
        ResponseObj resp = initResporse(request);

        String content = "好的即将为您打印标签";
        String food1 = getSlot(request, "food");
        String food2;
        String food3;
        String num = getSlot(request, "num");
        String chinesenum = getChineseNumFomeSlot(request);
        int relnum;
        if (num != null) {
            relnum = Integer.parseInt(num);
        } else if (chinesenum != null) {
            relnum = Integer.parseInt(chinesenum);
        } else {
            relnum = 1;
        }
        JSONArray arr = new JSONArray();
        if (relnum <= 5) {
            JSONObject food1json = makefoodinfosign(food1, relnum);
            JSONObject food2json;
            JSONObject food3json;

            arr.add(food1json);

            if (hasSlot(request, "food2")) {
                food2 = getSlot(request, "food2");
                food2json = makefoodinfosign(food2, "1");
                arr.add(food2json);

                if (hasSlot(request, "food3")) {
                    food3 = getSlot(request, "food3");
                    food3json = makefoodinfosign(food3, "1");
                    arr.add(food3json);
                }
            }
        } else {
            content = "每次打印不能超过5个标签哦。";
        }
        resp.getResponse().getSpeech().setContent(content);
        resp.getResponse().getCommand().setResults(arr);

        return resp;
    }

    /**
     * 生成打印标签json 注：只有单个食材支持多数量
     *
     * @param food 食材名
     * @param num  数量 string
     * @return 拼接后的json
     */

    private JSONObject makefoodinfosign(String food, String num) {
        JSONObject json = new JSONObject();
        json.put("foodname", food);
        UnlifeFood unlifeFood = cacheService.getPublicFoodInfoByName(food);
        if (unlifeFood != null) {
            Date selfdate = DateUtils.addDay(new Date(), unlifeFood.getShelflife());

            json.put("shelfLife", "建议" + DateUtils.dateToString(selfdate, DateUtils.DATE_FORMAT_WITHPOINT) + "前食用");
            json.put("saveDate", DateUtils.dateToString(new Date(), DateUtils.DATE_FORMAT_WITHPOINT));
            json.put("storeAreaAdvise", "建议存放在" + unlifeFood.getStorearea() + "室");
            json.put("num", num);
        }
        return json;
    }

    /**
     * 兼容数字形式的入口的拼接方法
     *
     * @param food 食材
     * @param num  数量
     * @return 拼接后的json
     */
    private JSONObject makefoodinfosign(String food, int num) {
        return makefoodinfosign(food, String.valueOf(num));
    }
}
