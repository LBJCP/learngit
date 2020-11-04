package com.fridge.skl.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.JSONToken;
import com.fridge.skl.dto.FeedbackMapper;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.model.unilifemodel.FoodInfo;
import com.fridge.skl.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.Math;

import static com.fridge.skl.util.URL_Constants.*;


@Service
@Slf4j
public class FoodUtil extends BaseService {

    @Autowired
    FeedbackMapper feedbackMapper;

    /**
     * 冷柜添加食材
     */


    //添加食材
    public static boolean addfood(String deviceid, String userid, String foodname, Integer location) {
        JSONObject json = new JSONObject();
        if(deviceid.toLowerCase().equals("")){
            return false;
        }else{
            json.put("deviceId", deviceid.toLowerCase());
        }
        if(userid.equals("")){
            return false;
        }else {
            json.put("userId", userid);
        }
        json.put("name", foodname);
        json.put("createTime", new Date().getTime());
        json.put("shelfLife", 1);
        if (location == null) {
            json.put("location", -1);
        } else {
            json.put("location", location);//冷柜添加层数
        }
        String res = HttpUtil.postJson(URL_UNILIFE + ACTION_ADDFOOD, json.toJSONString());
        log.info("addfoodres===========>" + res);
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (0 == resJson.getIntValue("errorno")) {
                return true;
            }

        }
        return false;
    }

    /**
     * 冷柜取出(删除)食材
     */
    //删除食材列表
    public static boolean removefoodlist(String deviceid, Integer location, String foodname) {

        JSONArray foodlists = new JSONArray();
        JSONArray foodlist = new JSONArray();


        JSONObject json = new JSONObject();
        json.put("deviceId", deviceid.toLowerCase());
        if (location == null) {
            json.put("location", -1);
        } else {
            json.put("location", location);//冷柜添加层数
        }
        json.put("name", foodname);
        String res = HttpUtil.postJson(URL_UNILIFE + ACTION_GETFOODLIST, json.toJSONString());
        log.info("removefoodlistres===========>" + res);
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (0 == resJson.getIntValue("errorno")) {
                JSONArray foodArry = resJson.getJSONArray("data");

                for (int index = 0; index < foodArry.size(); index++) {
                    JSONObject food = foodArry.getJSONObject(index);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(food.getLongValue("createTime"));

                    if (food.getString("name").equals(foodname)) {
                        foodlists.add(food);
                        //System.out.println(calendar.getTime());
                    }
                }
                //删除食材
                //System.out.println("foodlists中的第一个元素："+foodlists.get(0));
                if (foodlists.size() > 0) {
                    foodlist.add(foodlists.get(0));
                    System.out.println("要删除的食材" + JSONArray.toJSONString(foodlist));

                    JSONObject para = new JSONObject();
                    para.put("deviceId", deviceid.toLowerCase());
                    para.put("delFridgeFoods", foodlist);

                    HttpUtil.postJson("http://api.unilifemedia.com/service/v1/food/batchFridgeFoods?access_token=" + HttpUtil.getToken(), para.toJSONString());
                    return true;
                } else {
                    return false;
                }

            }

        }
        return false;
    }


    /**
     * 查看食材的位置
     */
    //查看食材列表
    public static String queryfood(String deviceid, Integer location, String foodname) {

        JSONArray foodlists = new JSONArray();
        ArrayList<String> foodlist = new ArrayList<>();
        ArrayList<String> foodlist2 = new ArrayList<>();


        JSONObject json = new JSONObject();
        if(deviceid.toLowerCase().equals("")){
            return "小优无法获取您家冷柜的设备编号，您可以尝试重新连接一下您的冷柜";
        }else{
            json.put("deviceId", deviceid.toLowerCase());
        }
        json.put("name", foodname);
        json.put("location", location);//读取全舱室的食材,以此作为基点来进一步查询其他层数的食材
        String res = HttpUtil.postJson(URL_UNILIFE + ACTION_GETFOODLIST, json.toJSONString());
        log.info("queryfood===========>" + res);
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (0 == resJson.getIntValue("errorno")) {
                JSONArray foodArry = resJson.getJSONArray("data");

                //遍历整个食材列表
                for (int index = 0; index < foodArry.size(); index++) {
                    JSONObject food = foodArry.getJSONObject(index);
                    //当遍历出食材列表中的名字与语义中的名字相同时，将其所在的食材对象添加到foodlists中
                    if (food.getString("name").equals(foodname)) {
                        foodlists.add(food);
                    }
                }
                //System.out.println("foodlists:" + foodlists);
                //遍历foodlists列表，得到各食材对象的location值，并添加到foodlist列表中
                for (int i = 0; i < foodlists.size(); i++) {
                    JSONObject food_new = foodlists.getJSONObject(i);
                    foodlist.add(String.valueOf(food_new.get("location")));

                }
                //判断foodlist的大小，如果大于1，则将foodlist中出现的location重复值删除，只保留第一个不重复值

                    for (int m = 0; m < foodlist.size(); m++) {
                        for (int n = m + 1; n < foodlist.size(); n++) {
                            if (foodlist.get(m).equals(foodlist.get(n))) {
                                foodlist.remove(n);
                                m--;
                                break;
                            }
                        }
                    }
                    System.out.println("删除重复值后的foodlist:" + foodlist);
                    //遍历删除重复元素后的foodlist，如果出现值等于-1的情况，则可判断出其中的一部分食材存储在全舱室
                    Collections.sort(foodlist);
                    System.out.println("排序后的foodlist:" + foodlist);
                    /*为了防止和冰箱语义相冲突而产生识别错误的问题，故将location为 0的食材归为冷柜的全舱室(与实际对应)，
                    这样即使发生语义识别错误也不会影响冷柜的相应操作*/
                if (foodlist.size() > 1) {
                    if(Integer.parseInt(foodlist.get(0))==-1 || Integer.parseInt(foodlist.get(0))==0) {
                        foodlist2.add("全舱室");
                        foodlist.remove(foodlist.get(0));
                        System.out.println("删除-1后的foodlist"+foodlist);
                        if(foodlist.size() > 1  && Integer.parseInt(foodlist.get(0))==0){
                            foodlist.remove(foodlist.get(0));
                            for (String s : foodlist) {
                                foodlist2.add((String) (CommonUtil.getKey(CommonUtil.getFridgeLocation(), Integer.parseInt(s))));
                            }
                        }else if(foodlist.size()>1 && Integer.parseInt(foodlist.get(0))!=0){
                            for (String s : foodlist) {
                                foodlist2.add((String) (CommonUtil.getKey(CommonUtil.getFridgeLocation(), Integer.parseInt(s))));
                            }
                        }else if(foodlist.size()==1 && Integer.parseInt(foodlist.get(0))==0){
                            foodlist.remove(foodlist.get(0));
                        }else{
                            for (String s : foodlist) {
                                foodlist2.add((String) (CommonUtil.getKey(CommonUtil.getFridgeLocation(), Integer.parseInt(s))));
                            }
                        }
                    }else{
                        for(String s:foodlist) {
                            foodlist2.add((String) (CommonUtil.getKey(CommonUtil.getFridgeLocation(), Integer.parseInt(s))));
                        }
                    }

                    System.out.println("foodlist2" + foodlist2);
                    String foodlist_new = foodlist2.toString().replaceAll("(?:\\[|null|\\]| +)", "");
                    System.out.println("foodlist_new=" + foodlist_new);

                    return foodname + "在冷柜的" + foodlist_new;
                }
                else if(foodlist.size()==1){
                    if(Integer.parseInt(foodlist.get(0))==-1 || Integer.parseInt(foodlist.get(0))==0){
                        foodlist2.add("全舱室");
                    }else{
                        foodlist2.add((String) (CommonUtil.getKey(CommonUtil.getFridgeLocation(), Integer.parseInt(foodlist.get(0)))));
                    }

                    String foodlist_new = foodlist2.toString().replaceAll("(?:\\[|null|\\]| +)", "");
                    return foodname + "在冷柜的" + foodlist_new;
                }

            }
        }
        
        return "您的冷柜中没有"+foodname;
    }

}
