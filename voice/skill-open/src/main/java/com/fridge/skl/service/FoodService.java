package com.fridge.skl.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.model.*;
import com.fridge.skl.model.response.Speech;
import com.fridge.skl.model.response.SpeechContent;
import com.fridge.skl.util.CommonUtil;
import com.fridge.skl.util.HttpUtil;
import com.fridge.skl.util.UtilConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class FoodService {

    private static Logger logger = LoggerFactory.getLogger(FoodService.class);



    /**
     * 查询冰箱有哪些快过期食材
     *
     * @param request
     * @return
     */
    public  ResponseObj doQueryKgqFood(RequestObj request) {
        ResponseObj resp = new ResponseObj();
        resp.setVersion("1.0");
        resp.setSession(request.getSession());
        resp.setContext(request.getContext());

        Response response = new Response();
        response.setValid(true);
        Speech speech = new Speech();
        speech.setType(SpeechTypeEnum.text);
        SpeechContent sc = new SpeechContent();
        sc.setType(UtilConstants.Public.REQUEST_TYPE_CONTINUE);
        String deviceId = request.getContext().getDevice().getDeviceId();
        GuessIntent[] intents = request.getRequest().getIntents();
        List<JSONObject> jsons = getFoodDays(deviceId, null);
        System.out.println(jsons);
        String content = "";


        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            Slot[] slots = intent.getSlots();


            if (slots.length > 0) {

                if (slots.length == 1) {

                    if (UtilConstants.Slot.SLOT_DAYS_NAME.equals(slots[0].getName())) {
                        int day = 0;
                        if ("两".equals(slots[0].getValues()[0])) {
                            day = 2;
                        } else {

                            day = CommonUtil.getALBNumFast(slots[0].getValues()[0]);
                        }
                        Set<String> foods = new HashSet<String>();
                        for (int i = 0; i < jsons.size(); i++) {
                            JSONObject o = jsons.get(i);
                            int days = o.getIntValue("days");
                            if (days <= day) {
                                foods.add(o.getString("name"));
                            }

                        }

                        if (foods.size() > 0) {

                            content = day + "天内过期的食材有:" + CommonUtil.list2String(new ArrayList<String>(foods), '，');
                        } else {
                            content = "冰箱中的食材都很新鲜呢";

                        }


                    } else if (UtilConstants.Slot.SLOT_MONTHS_NAME.equals(slots[0].getName())) {

                        int day = 0;
                        if ("半".equals(slots[0].getValues()[0])) {
                            day = 15;
                        } else if ("两".equals(slots[0].getValues()[0])) {
                            day = 60;

                        } else {

                            day = CommonUtil.getALBNumFast(slots[0].getValues()[0]) * 30;
                        }
                        Set<String> foods = new HashSet<String>();
                        for (int i = 0; i < jsons.size(); i++) {
                            JSONObject o = jsons.get(i);
                            int days = o.getIntValue("days");
                            if (days <= day) {
                                foods.add(o.getString("name"));
                            }

                        }

                        if (foods.size() > 0) {

                            content = slots[0].getValues()[0] + "个月内过期的食材有:" + CommonUtil.list2String(new ArrayList<String>(foods), '，');
                        } else {
                            content = "冰箱中的食材都很新鲜呢";

                        }
                    }

                }

            } else {


                Set<String> foods = new HashSet<String>();
                for (int i = 0; i < jsons.size(); i++) {
                    JSONObject o = jsons.get(i);
                    int days = o.getIntValue("days");
                    if (days <= 7) {
                        foods.add(o.getString("name"));
                    }

                }
                if (foods.size() > 0) {

                    content = "7天内过期的食材有:" + CommonUtil.list2String(new ArrayList<String>(foods), '，');
                } else {
                    content = "冰箱中的食材都很新鲜呢";

                }

            }

        }

        speech.setContent(content);
        response.setSpeech(speech);
        response.setShouldEndSession(false);
        resp.setResponse(response);

        return resp;
    }

    /**
     * 查询某食材能存多长时间
     *
     * @param request
     * @return
     */
    public  ResponseObj doQueryTime(RequestObj request) {
        ResponseObj resp = new ResponseObj();
        resp.setVersion("1.0");
        resp.setSession(request.getSession());
        resp.setContext(request.getContext());

        Response response = new Response();
        response.setValid(true);
        Speech speech = new Speech();
        speech.setType(SpeechTypeEnum.text);
        SpeechContent sc = new SpeechContent();
        sc.setType(UtilConstants.Public.REQUEST_TYPE_CONTINUE);
        GuessIntent[] intents = request.getRequest().getIntents();

        String content = "";


        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            Slot[] slots = intent.getSlots();


            if (slots.length > 0) {

                if (slots.length == 1) {

                    if (UtilConstants.Slot.SLOT_SJ_NAME.equals(slots[0].getName())) {

                        String foodName = slots[0].getValues()[0];


                        JSONArray arr = CommonUtil.getFoodJsonSource().get("food");
                        JSONObject foodJson = null;
                        for (int i = 0; i < arr.size(); i++) {

                            if (arr.getJSONObject(i).getString("name").equals(foodName)) {

                                foodJson = arr.getJSONObject(i);
                                break;
                            }
                        }
                        if (foodJson == null) {
                            content = "抱歉，目前食材库没有" + foodName + "的数据";

                        } else {
                            Integer ldDays = foodJson.getIntValue("freezingMinDay");
                            Integer lcDays = foodJson.getIntValue("coldMinDay");
                            if (request.getRequest().getQuery().getContent().indexOf("保质期") > -1) {


                                if (ldDays != 0 && lcDays != 0) {

                                    content = foodName + "冷藏室建议保存" + lcDays + "天，冷冻室建议保存" + ldDays + "天";
                                }
                                if (ldDays == 0 && lcDays != 0) {

                                    content = foodName + "冷藏室建议保存" + lcDays + "天";
                                }
                                if (ldDays != 0 && lcDays == 0) {

                                    content = foodName + "冷冻室建议保存" + ldDays + "天";
                                }
                                if (ldDays == 0 && lcDays == 0) {

                                    content = "抱歉，目前食材库没有" + foodName + "的数据";
                                }


                            }
                            if (request.getRequest().getQuery().getContent().indexOf("冷藏") > -1) {
                                if (lcDays == 0) {
                                    content = foodName + "不建议冷藏";
                                    if (ldDays != 0) {
                                        content = content + "，建议冷冻室保存" + ldDays + "天";
                                    }
                                } else {
                                    content = foodName + "冷藏室建议保存" + lcDays + "天";

                                }


                            }
                            if (request.getRequest().getQuery().getContent().indexOf("冷冻") > -1) {

                                if (ldDays == 0) {
                                    content = foodName + "不建议冷冻";
                                    if (lcDays != 0) {
                                        content = content + "，建议冷藏室保存" + lcDays + "天";
                                    }
                                } else {
                                    content = foodName + "冷冻室建议保存" + ldDays + "天";

                                }
                            }

                        }

                    }

                }

            }

        }


        speech.setContent(content);
        response.setSpeech(speech);
        response.setShouldEndSession(false);
        resp.setResponse(response);

        return resp;
    }

    /**
     * 查询食材是否过期
     *
     * @param request
     * @return
     */
    public  ResponseObj doQueryGqFood(RequestObj request) {
        ResponseObj resp = new ResponseObj();
        resp.setVersion("1.0");
        resp.setSession(request.getSession());
        resp.setContext(request.getContext());

        Response response = new Response();
        response.setValid(true);
        Speech speech = new Speech();
        speech.setType(SpeechTypeEnum.text);
        SpeechContent sc = new SpeechContent();
        sc.setType(UtilConstants.Public.REQUEST_TYPE_CONTINUE);
        String deviceId = request.getContext().getDevice().getDeviceId();
        GuessIntent[] intents = request.getRequest().getIntents();

        String content = "";


        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            Slot[] slots = intent.getSlots();


            if (slots.length > 0) {

                if (slots.length == 1) {

                    if (UtilConstants.Slot.SLOT_SC_NAME.equals(slots[0].getName())) {


                        List<JSONObject> jsons = getFoodDays(deviceId, null);
                        String foodName = slots[0].getValues()[0];

                        List<JSONObject> foods = new ArrayList<JSONObject>();
                        for (int i = 0; i < jsons.size(); i++) {
                            JSONObject o = jsons.get(i);
                            if (foodName.equals(o.getString("name"))) {
                                foods.add(o);
                            }
                        }


                        if (foods.size() > 0) {

                            if (foods.size() == 1) {


                                if (foods.get(0).getIntValue("days") < 1) {
                                    content = "冰箱中" + foodName + "已过期";

                                } else {
                                    content = "冰箱中" + foodName + "的保质期剩余" + foods.get(0).getIntValue("days") + "天";
                                }

                            } else if (foods.size() > 1) {

                                List<JSONObject> gq = new ArrayList<JSONObject>();
                                List<JSONObject> wgq = new ArrayList<JSONObject>();
                                for (int i = 0; i < foods.size(); i++) {
                                    if (foods.get(i).getIntValue("days") < 1) {
                                        gq.add(foods.get(i));

                                    } else {

                                        wgq.add(foods.get(i));
                                    }

                                }

                                if (foods.size() == gq.size()) {
                                    content = "冰箱中有" + foods.size() + "份" + foodName + "，已全部过期";
                                } else if (foods.size() == wgq.size()) {

                                    String day = "";
                                    List<Integer> list = new ArrayList<Integer>();
                                    for (int i = 0; i < wgq.size(); i++) {
                                        list.add(wgq.get(i).getIntValue("days"));
                                    }
                                    Collections.sort(list);
                                    for (int i = 0; i < list.size(); i++) {

                                        day = day + list.get(i) + "天";
                                        if (i != (list.size() - 1)) {
                                            day =day.concat( "，");
                                        }
                                    }


                                    content = "冰箱中有" + foods.size() + "份" + foodName + "，剩余保质期为" + day;
                                } else {


                                    String day = "";
                                    List<Integer> list = new ArrayList<Integer>();
                                    for (int i = 0; i < wgq.size(); i++) {
                                        list.add(wgq.get(i).getIntValue("days"));
                                    }
                                    Collections.sort(list);
                                    for (int i = 0; i < list.size(); i++) {

                                        day = day + list.get(i) + "天";
                                        if (i != (list.size() - 1)) {
                                            day =day.concat( "，");
                                        }
                                    }


                                    content = "冰箱中有" + foods.size() + "份" + foodName + "，" + gq.size() + "份已经过期，其他剩余保质期为" + day;
                                    System.out.println();

                                }


                            }

                            // content = slots[0].getValues()[0] + "7天内过期的食材有:" + CommonUtil.list2String(new ArrayList<String>(foods), '，');
                        } else {
                            content = "冰箱中没有" + slots[0].getValues()[0];

                        }


                    }


                }

            }


        }


        speech.setContent(content);
        response.setSpeech(speech);
        response.setShouldEndSession(false);
        resp.setResponse(response);

        return resp;
    }

    /**
     * 存储日期相关查询
     *
     * @param request
     * @return
     */


    public  ResponseObj doQueryRq(RequestObj request) {
        ResponseObj resp = new ResponseObj();
        resp.setVersion("1.0");
        resp.setSession(request.getSession());
        resp.setContext(request.getContext());

        Response response = new Response();
        response.setValid(true);
        Speech speech = new Speech();
        speech.setType(SpeechTypeEnum.text);
        SpeechContent sc = new SpeechContent();
        sc.setType(UtilConstants.Public.REQUEST_TYPE_CONTINUE);
        String deviceId = request.getContext().getDevice().getDeviceId();
        GuessIntent[] intents = request.getRequest().getIntents();

        String content = "";


        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            Slot[] slots = intent.getSlots();


            if (slots.length > 0) {

                if (slots.length == 1) {

                    if (UtilConstants.Slot.SLOT_CC_NAME.equals(slots[0].getName())) {


                        List<JSONObject> jsons = getFoodDays(deviceId, null);
                        String foodName = slots[0].getValues()[0];

                        List<JSONObject> foods = new ArrayList<JSONObject>();
                        for (int i = 0; i < jsons.size(); i++) {
                            JSONObject o = jsons.get(i);
                            if (foodName.equals(o.getString("name"))) {
                                foods.add(o);
                            }
                        }


                        if (foods.size() > 0) {

                            if (foods.size() == 1) {
                                content = slots[0].getValues()[0] + new SimpleDateFormat("yyyy年M月d日").format(new Date(foods.get(0).getLongValue("createTime"))) + "添加到冰箱，已存放" + (int) (Math.ceil((new Date().getTime() - foods.get(0).getLongValue("createTime")) / (float) (1000 * 3600 * 24))) + "天";
                            } else {

                                String day = "";
                                List<Long> list = new ArrayList<Long>();
                                for (int i = 0; i < foods.size(); i++) {
                                    list.add(foods.get(i).getLongValue("createTime"));
                                }
                                Collections.sort(list);
                                for (int i = 0; i < list.size(); i++) {

                                    day = day + new SimpleDateFormat("yyyy年M月d日").format(new Date(list.get(i)));
                                    if (i != (list.size() - 1)) {
                                        day =day.concat( "，");
                                    }
                                }


                                content = "冰箱中有" + foods.size() + "份" + foodName + "，存入时间为" + day;
                            }
                        } else {

                            content = "冰箱中没有" + slots[0].getValues()[0];

                        }


                    }


                }

            }


        }


        speech.setContent(content);
        response.setSpeech(speech);


        response.setShouldEndSession(false);
        resp.setResponse(response);

        return resp;
    }

    /**
     * 查询舱室有哪些快过期食材
     *
     * @param request
     * @return
     */
    public  ResponseObj doQueryCsKgqFood(RequestObj request) {
        ResponseObj resp = new ResponseObj();
        resp.setVersion("1.0");
        resp.setSession(request.getSession());
        resp.setContext(request.getContext());

        Response response = new Response();
        response.setValid(true);
        Speech speech = new Speech();
        speech.setType(SpeechTypeEnum.text);
        SpeechContent sc = new SpeechContent();
        sc.setType(UtilConstants.Public.REQUEST_TYPE_CONTINUE);
        String deviceId = request.getContext().getDevice().getDeviceId();
        GuessIntent[] intents = request.getRequest().getIntents();

        String content = "";


        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            Slot[] slots = intent.getSlots();


            if (slots.length > 0) {

                if (slots.length == 1) {

                    if (UtilConstants.Slot.SLOT_CS_NAME.equals(slots[0].getName())) {

                        int location = CommonUtil.getFridgeLocation().get(slots[0].getValues()[0]);
                        List<JSONObject> jsons = getFoodDays(deviceId, location);
                        System.out.println(jsons);
                        Set<String> foods = new HashSet<String>();
                        for (int i = 0; i < jsons.size(); i++) {
                            JSONObject o = jsons.get(i);
                            int days = o.getIntValue("days");
                            if (days <= 7) {
                                foods.add(o.getString("name"));
                            }

                        }

                        if (foods.size() > 0) {

                            content = slots[0].getValues()[0] + "7天内过期的食材有:" + CommonUtil.list2String( foods.stream().distinct().collect(Collectors.toList()), '，');
                        } else {
                            content = slots[0].getValues()[0] + "中的食材都很新鲜呢";

                        }


                    }


                }

            }


        }


        speech.setContent(content);
        response.setSpeech(speech);
        response.setShouldEndSession(false);
        resp.setResponse(response);

        return resp;
    }

    public  ResponseObj doQueryExpiredFood(RequestObj request) {

        ResponseObj resp = new ResponseObj();
        resp.setVersion("1.0");
        resp.setSession(request.getSession());
        resp.setContext(request.getContext());

        Response response = new Response();
        response.setValid(true);
        Speech speech = new Speech();
        speech.setType(SpeechTypeEnum.text);
        SpeechContent sc = new SpeechContent();
        sc.setType(UtilConstants.Public.REQUEST_TYPE_CONTINUE);
        String deviceId = request.getContext().getDevice().getDeviceId();
        GuessIntent[] intents = request.getRequest().getIntents();
        List<String> foods = new ArrayList<String>();
        String content = "";
        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            Slot[] slots = intent.getSlots();


            if (slots.length > 0) {

                if (slots.length == 1) {

                    if (UtilConstants.Slot.SLOT_LOCATION_NAME.equals(slots[0].getName())) {

                        foods = getExpireFoodList(deviceId, CommonUtil.getNum(slots[0].getValues()[0]), "1");
                        logger.info("过期食材---" + JSONObject.toJSONString(foods));
                        if (foods.size() > 0) {

                            content = "冰箱中已到期的食材有：" + CommonUtil.list2String( foods.stream().distinct().collect(Collectors.toList()), '，');
                        } else {
                            content = "冰箱中没有过期食材";

                        }


                    } else if (UtilConstants.Slot.SLOT_FOOD_NAME.equals(slots[0].getName())) {

                        foods = getExpireFoodList(deviceId, -1, "1");
                        logger.info("过期食材---" + JSONObject.toJSONString(foods));
                        if (foods.contains(slots[0].getValues()[0])) {

                            content = "过期了";
                        } else {

                            content = "没有过期";
                        }
                    }
                } else {

                    String food = "";
                    String location = "";
                    for (int i = 0; i < slots.length; i++) {
                        if (UtilConstants.Slot.SLOT_FOOD_NAME.equals(slots[i].getName())) {

                            food = slots[i].getValues()[0];
                        }
                        if (UtilConstants.Slot.SLOT_LOCATION_NAME.equals(slots[i].getName())) {

                            location = slots[i].getValues()[0];
                        }

                    }
                    foods = getExpireFoodList(deviceId, CommonUtil.getNum(location), "1");
                    logger.info("过期食材---" + JSONObject.toJSONString(foods));
                    if (foods.contains(food)) {

                        content = "过期了";
                    } else {

                        content = "没有过期";
                    }


                }

            } else {
                if (request.getRequest().getQuery().getContent().indexOf("快过期") > -1) {
                    foods = getExpireFoodList(deviceId, -1, "0");
                    if (foods.size() > 0) {
                        content = CommonUtil.list2String( foods.stream().distinct().collect(Collectors.toList()), '，');
                    } else {
                        content = "没有快过期食材";

                    }

                } else {
                    foods = getExpireFoodList(deviceId, -1, "1");
                    if (foods.size() > 0) {
                        content = "冰箱中已到期的食材有：" + CommonUtil.list2String( foods.stream().distinct().collect(Collectors.toList()), '，');
                    } else {
                        content = "冰箱中没有过期食材";

                    }
                }


            }


        }

        speech.setContent(content);
        response.setSpeech(speech);
        response.setShouldEndSession(false);
        resp.setResponse(response);

        return resp;

    }


    public  ResponseObj doDelExpireFood(RequestObj request) {

        ResponseObj resp = new ResponseObj();
        resp.setVersion("1.0");
        resp.setSession(request.getSession());
        resp.setContext(request.getContext());

        Response response = new Response();
        response.setValid(true);
        Speech speech = new Speech();
        speech.setType(SpeechTypeEnum.text);
        SpeechContent sc = new SpeechContent();
        sc.setType(UtilConstants.Public.REQUEST_TYPE_CONTINUE);
        String deviceId = request.getContext().getDevice().getDeviceId();


        GuessIntent[] intents = request.getRequest().getIntents();
        String content = "";
        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            Slot[] slots = intent.getSlots();


            if (slots.length > 0) {

                if (slots.length == 1) {

                    if (UtilConstants.Slot.SLOT_LOCATION_NAME.equals(slots[0].getName())) {


                        delExpireFoodList(deviceId, CommonUtil.getNum(slots[0].getValues()[0]), null);


                    } else if (UtilConstants.Slot.SLOT_FOOD_NAME.equals(slots[0].getName())) {
                        delExpireFoodList(deviceId, null, slots[0].getValues()[0]);
                    }
                } else {

                    String food = "";
                    String location = "";
                    for (int i = 0; i < slots.length; i++) {
                        if (UtilConstants.Slot.SLOT_FOOD_NAME.equals(slots[i].getName())) {

                            food = slots[i].getValues()[0];
                        }
                        if (UtilConstants.Slot.SLOT_LOCATION_NAME.equals(slots[i].getName())) {

                            location = slots[i].getValues()[0];
                        }

                    }
                    delExpireFoodList(deviceId, CommonUtil.getNum(location), food);


                }

            } else {
                JSONArray arr = delExpireFoodList2(deviceId, null, null);
                if (arr.size() == 0) {
                    content = "冰箱中没有过期食材";
                } else {

                    if (arr.size() > 2) {
                        content = "已经为您删除" + arr.getJSONObject(0).getString("name") + "," + arr.getJSONObject(1).getString("name") + "等" + arr.size() + "种过期食材";

                    } else {

                        content = "已经为您删除过期的" + arr.getJSONObject(0).getString("name") + "," + arr.getJSONObject(1).getString("name");

                    }

                }


            }


        }


        speech.setContent(content);
        response.setSpeech(speech);
        response.setShouldEndSession(false);
        resp.setResponse(response);

        return resp;

    }


    public  List getExpireFoodList(String deviceId, int location, String type) {

        List<String> expireFoods = new ArrayList<String>();
        JSONObject json = new JSONObject();
        json.put("deviceId", deviceId);
        json.put("location", location);
        String res = HttpUtil.postJson("http://api.unilifemedia.com/service/v1/food/getList?access_token=" + HttpUtil.getToken(), json.toJSONString());
        if (!"".equals(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (0 == resJson.getIntValue("errorno")) {
                JSONArray foodArry = resJson.getJSONArray("data");
                System.out.println("所有食材:" + JSONArray.toJSONString(foodArry));
                if (foodArry.size() > 0) {

                    for (int i = 0; i < foodArry.size(); i++) {

                        JSONObject food = foodArry.getJSONObject(i);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(food.getLongValue("createTime"));
                        //判断差一天过期的
                        if ("0".equals(type)) {
                            calendar.add(calendar.DAY_OF_WEEK, food.getIntValue("shelfLife") - 1);
                        } else {
                            calendar.add(calendar.DAY_OF_WEEK, food.getIntValue("shelfLife"));
                        }


                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date expireDate ;
                        try {
                            expireDate = sdf.parse(sdf.format(calendar.getTime()));

                            if (expireDate.before(new Date())) {
                                expireFoods.add(food.getString("name"));

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }




                    }

                }

            }

        }

        return expireFoods;

    }

    public  void delExpireFoodList(String deviceId, Integer location, String foodName) {

        JSONArray expireFoods = new JSONArray();
        JSONObject json = new JSONObject();
        json.put("deviceId", deviceId);
        if (location == null) {
            json.put("location", -1);
        } else {
            json.put("location", location);

        }

        String res = HttpUtil.postJson("http://api.unilifemedia.com/service/v1/food/getList?access_token=" + HttpUtil.getToken(), json.toJSONString());
        if (!"".equals(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (0 == resJson.getIntValue("errorno")) {
                JSONArray foodArry = resJson.getJSONArray("data");

                if (foodArry.size() > 0) {

                    for (int i = 0; i < foodArry.size(); i++) {

                        JSONObject food = foodArry.getJSONObject(i);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(food.getLongValue("createTime"));
                        calendar.add(calendar.DAY_OF_WEEK, food.getIntValue("shelfLife"));


                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date expireDate = null;
                        try {
                            expireDate = sdf.parse(sdf.format(calendar.getTime()));
                            if (expireDate.before(new Date())) {
                                if (foodName != null) {
                                    if (food.getString("name").equals(foodName)) {
                                        expireFoods.add(food);
                                    }
                                } else {
                                    expireFoods.add(food);
                                }


                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }

        }
        System.out.println("要删除的食材" + JSONArray.toJSONString(expireFoods));
        if (expireFoods.size() > 0) {
            JSONObject para = new JSONObject();
            para.put("deviceId", deviceId);
            para.put("delFridgeFoods", expireFoods);
            HttpUtil.postJson("http://api.unilifemedia.com/service/v1/food/batchFridgeFoods?access_token=" + HttpUtil.getToken(), para.toJSONString());

        }


    }

    public  JSONArray delExpireFoodList2(String deviceId, Integer location, String foodName) {

        JSONArray expireFoods = new JSONArray();
        JSONObject json = new JSONObject();
        json.put("deviceId", deviceId);
        if (location == null) {
            json.put("location", -1);
        } else {
            json.put("location", location);

        }

        String res = HttpUtil.postJson("http://api.unilifemedia.com/service/v1/food/getList?access_token=" + HttpUtil.getToken(), json.toJSONString());
        if (!"".equals(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (0 == resJson.getIntValue("errorno")) {
                JSONArray foodArry = resJson.getJSONArray("data");

                if (foodArry.size() > 0) {

                    for (int i = 0; i < foodArry.size(); i++) {

                        JSONObject food = foodArry.getJSONObject(i);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(food.getLongValue("createTime"));
                        calendar.add(calendar.DAY_OF_WEEK, food.getIntValue("shelfLife"));


                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date expireDate = null;
                        try {
                            expireDate = sdf.parse(sdf.format(calendar.getTime()));
                            if (expireDate.before(new Date())) {
                                if (foodName != null) {
                                    if (food.getString("name").equals(foodName)) {
                                        expireFoods.add(food);
                                    }
                                } else {
                                    expireFoods.add(food);
                                }


                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }

            }

        }
        System.out.println("要删除的食材" + JSONArray.toJSONString(expireFoods));
        if (expireFoods.size() > 0) {
            JSONObject para = new JSONObject();
            para.put("deviceId", deviceId);
            para.put("delFridgeFoods", expireFoods);
            HttpUtil.postJson("http://api.unilifemedia.com/service/v1/food/batchFridgeFoods?access_token=" + HttpUtil.getToken(), para.toJSONString());

        }

        return expireFoods;


    }


    /**
     * 返回每个食材还有几天过期
     *
     * @param deviceId
     * @return
     */
    public  List<JSONObject> getFoodDays(String deviceId, Integer location) {

        List<JSONObject> foods = new ArrayList<JSONObject>();
        JSONObject json = new JSONObject();
        json.put("deviceId", deviceId);
        if (location == null) {
            json.put("location", -1);

        } else {

            json.put("location", location);
        }

        String res = HttpUtil.postJson("http://api.unilifemedia.com/service/v1/food/getList?access_token=" + HttpUtil.getToken(), json.toJSONString());
        if (!"".equals(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (0 == resJson.getIntValue("errorno")) {
                JSONArray foodArry = resJson.getJSONArray("data");
                if (foodArry.size() > 0) {

                    for (int i = 0; i < foodArry.size(); i++) {

                        JSONObject food = foodArry.getJSONObject(i);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(food.getLongValue("createTime"));
                        calendar.add(calendar.DAY_OF_WEEK, food.getIntValue("shelfLife"));
                        Date expireDate = calendar.getTime();
                        //正数说明还有几天才能过期，负数说明已经过期了几天
                        int days = compareDays(new Date(), expireDate);
                        food.put("days", days);
                        foods.add(food);

                    }

                }

            }

        }
        List<Map> list = new ArrayList<Map>();
        for (int i = 0; i < foods.size(); i++) {
            Map m = new ConcurrentHashMap();
            m.put("name", foods.get(i).getString("name"));
            m.put("days", foods.get(i).getIntValue("days"));
            m.put("location", foods.get(i).getIntValue("location"));
            list.add(m);

        }
        System.out.println(JSONObject.toJSONString(list));

        return foods;

    }

    public static int compareDays(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);
        if (year1 > year2) {
            int tempyear = year1;
            int tempday = day1;
            day1 = day2;
            day2 = tempday;
            year1 = year2;
            year2 = tempyear;
        }
        if (year1 == year2) {
            return (day2 - day1);
        } else {
            int DayCount = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    DayCount += 366;
                } else {
                    DayCount += 365;
                }
            }
            return DayCount + (day2 - day1);

        }
    }


}
