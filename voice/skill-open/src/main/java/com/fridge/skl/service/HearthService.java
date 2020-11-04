package com.fridge.skl.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.model.GuessIntent;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.model.Slot;
import com.fridge.skl.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

import static com.fridge.skl.util.URL_Constants.*;
import static com.fridge.skl.util.UtilConstants.Slot.*;

/**
 * Created by zhangn
 */
@Service
public class HearthService extends BaseService {
    public static final String GENDER_WOMAN = "female";
    public static final String GENDER_MAN = "male";
    @Autowired
    IDService idService;


    /**
     * 查询步数信息
     */
    public ResponseObj doQueryStep(RequestObj request) {
        ResponseObj resp = initResporse(request);

        String deviceId = request.getContext().getDevice().getDeviceId();
        String clientid = request.getContext().getDevice().getClientId();
        String accessToken = request.getContext().getDevice().getAccessToken();
        GuessIntent[] intents = request.getRequest().getIntents();

        String content = "对不起，没有查询到您的步数信息";


        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            Slot[] slots = intent.getSlots();

            String time = "今天";
            if (slots.length > 0) {

                if (slots.length == 1) {

                    if (UtilConstants.Slot.SLOT_TIMEBEF_NAME.equals(slots[0].getName())) {
                        time = slots[0].getValues()[0];

                    }

                }

            }
            //getUserid   获取用户中心的userid
            String userCenterUserid = idService.getUserCenterID(deviceId, clientid, accessToken);

            if (null != userCenterUserid) {


                JSONObject res = getStep(userCenterUserid, DateUtils.getDatebefore(time));
                if (res != null && res.containsKey("data") && res.getJSONArray("data").size() > 0) {
                    JSONArray data = res.getJSONArray("data");
                    String stepcount = data.getJSONObject(0).getString("stepcount");
                    String calory = data.getJSONObject(0).getString("calory");
                    content = "您" + time + "走了" + stepcount + "步，消耗了" + calory + "卡路里";
                }
            }
        }


        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 查询健康状况-单条
     */
    public ResponseObj doQueryIndex(RequestObj request) {
        ResponseObj resp = initResporse(request);


        String deviceId = request.getContext().getDevice().getDeviceId();
        String clinetId = request.getContext().getDevice().getClientId();
        String accessToken = request.getContext().getDevice().getAccessToken();
        GuessIntent[] intents = request.getRequest().getIntents();
        String userCenterToken = "";

        String content = "";
        String nickname = "";
        String index = "";
        JSONObject healthinfo = null;
        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            userCenterToken = idService.getUserCenterToken(deviceId, clinetId, accessToken);
            if (!StringUtils.isEmpty(userCenterToken)) {

                Slot[] slots = intent.getSlots();
                /**
                 * mode
                 * 1:我的index是多少
                 * 2：persion的index是多少
                 * */
                int mode = 1;
                if (slots.length == 1) {
                    if (SLOT_INDEX_NAME.equals(slots[0].getName())) {
                        index = slots[0].getValues()[0];
                        mode = 1;
                    }
                } else if (slots.length == 2) {
                    if (SLOT_INDEX_NAME.equals(slots[0].getName())) {
                        index = slots[0].getValues()[0];
                        if (SLOT_PERSION_NAME_ME.equals(slots[1].getValues()[0])) {
                            mode = 1;
                        } else {
                            mode = 2;
                            nickname = slots[1].getValues()[0];
                        }
                    } else if (SLOT_PERSION_NAME.equals(slots[0].getName())) {
                        index = slots[1].getValues()[0];
                        if (SLOT_PERSION_NAME_ME.equals(slots[0].getValues()[0])) {
                            mode = 1;
                        } else {
                            mode = 2;
                            nickname = slots[0].getValues()[0];
                        }
                    }
                }
                if (mode == 1) {
                    healthinfo = getUserHearthInfo(userCenterToken);
                    if (healthinfo != null && healthinfo.containsKey(CommonUtil.getBodyindex().get(index))) {
                        String res = healthinfo.getString(CommonUtil.getBodyindex().get(index));
                        content = "您的" + index + "为" + res + CommonUtil.getBodyunit().get(index);
                    } else {
                        content = "对不起，没有查询到您的" + index + "，请在海尔智家APP中初始化健康信息";
                    }
                } else {
                    healthinfo = getFictitiousUserInfo(userCenterToken, nickname);
                    if (healthinfo != null && healthinfo.containsKey(CommonUtil.getBodyindex().get(index))) {
                        String res = healthinfo.getString(CommonUtil.getBodyindex().get(index));
                        content = nickname + "的" + index + "为" + res + CommonUtil.getBodyunit().get(index);
                    } else {
                        content = "对不起，没有查询到" + nickname + "的" + index + "，请在海尔智家APP中初始化健康信息";
                    }
                }
            } else {
                content = "对不起，没有查询到您的健康信息";
            }
        }

        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 查询健康状况-全部信息
     */
    public ResponseObj doQueryHealth(RequestObj request) {
        ResponseObj resp = initResporse(request);

        String deviceId = request.getContext().getDevice().getDeviceId();
        String clinetId = request.getContext().getDevice().getClientId();
        String accessToken = request.getContext().getDevice().getAccessToken();

        GuessIntent[] intents = request.getRequest().getIntents();

        String content = "";
        String nickname = "";

        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            String userCenterToken = idService.getUserCenterToken(deviceId, clinetId, accessToken);
            if (!StringUtils.isEmpty(userCenterToken)) {

                Slot[] slots = intent.getSlots();
                JSONObject userherarinfo = null;
                if (slots.length == 0 || (slots.length == 1 && "我".equals(slots[0].getValues()[0]))) {
                    content = "对不起，没有查询到您的健康信息";
                    userherarinfo = getUserHearthInfo(userCenterToken);
                } else if (slots.length == 1) {
                    nickname = slots[0].getValues()[0];
                    content = "对不起，没有查询到" + nickname + "的健康信息";
                    userherarinfo = getFictitiousUserInfo(userCenterToken, nickname);

                }

                if (userherarinfo != null) {
                    if (StringUtils.isEmpty(nickname)) {
                        content = "您的";
                    } else {
                        content = nickname + "的";
                    }
                    if (userherarinfo.containsKey("BMID")) {
                        content += "健康综合评分为" + userherarinfo.getString("BMID") + "分,";
                    }
                    if (userherarinfo.containsKey("BMI_Grade")) {
                        content += "健康评级" + userherarinfo.getString("BMI_Grade") + "级,";
                    }
                    if (userherarinfo.containsKey("weight")) {
                        content += "体重" + userherarinfo.getString("weight") + "千克,";
                    }
                    if (userherarinfo.containsKey("bodystatus")) {
                        //身体状态 肥胖，健康，偏瘦
                        content += userherarinfo.getString("bodystatus") + ",";
                    }
                    if (userherarinfo.containsKey("fat")) {
                        content += "体脂率" + userherarinfo.getString("fat") + "%,";
                    }
                    if (userherarinfo.containsKey("height")) {
                        content += "身高" + userherarinfo.getString("height") + "米,";
                    }
                    if (userherarinfo.containsKey("BMI")) {
                        content += "BMI指数" + userherarinfo.getString("BMI") + ",";
                    }
                    if (userherarinfo.containsKey("heat")) {
                        content += "预算热量：" + userherarinfo.getString("heat") + "千卡";
                    }
                }
            }

        }


        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 查询饮食计划
     */
    public ResponseObj doQueryDietPlan(RequestObj request) {
        ResponseObj resp = initResporse(request);


        String deviceId = request.getContext().getDevice().getDeviceId();
        String clientid = request.getContext().getDevice().getClientId();
        String accessToken = request.getContext().getDevice().getAccessToken();
        GuessIntent[] intents = request.getRequest().getIntents();

        String content = "";

        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            Slot[] slots = intent.getSlots();
            //1:全天，2早餐，3午餐，4晚餐
            int time = 1;
            String persion = "我";


            if (slots.length == 1) {
                if (SLOT_PERSION_NAME.equals(slots[0].getName())) {
                    persion = slots[0].getValues()[0];
                } else if (SLOT_TIMEBEF_NAME.equals(slots[0].getName())) {
                    time = CommonUtil.getMealstime().get(slots[0].getValues()[0]);
                }
            } else if (slots.length == 2) {
                if (SLOT_PERSION_NAME.equals(slots[0].getName())) {
                    persion = slots[0].getValues()[0];
                    time = CommonUtil.getMealstime().get(slots[1].getValues()[0]);
                } else {
                    persion = slots[1].getValues()[0];
                    time = CommonUtil.getMealstime().get(slots[0].getValues()[0]);
                }
            }

            //getUserid   获取用户中心的userid
            String userCenterUserid = idService.getUserCenterID(deviceId, clientid, accessToken);

            //虚拟用户操作
            if (!SLOT_PERSION_NAME_ME.equals(persion)) {
                String userCenterToken = idService.getUserCenterToken(deviceId, clientid, accessToken);
                userCenterUserid = insertInfoAndGetdietPlan(userCenterToken, persion);
                content = "对不起，没有查询到" + persion + "的饮食计划，请在海尔智家APP中初始化" + persion + "的身体信息";
            }
            if (null != userCenterUserid) {
                content = (SLOT_PERSION_NAME_ME.equals(persion) ? "您" : persion) + "的";
                JSONObject plan = getDietPlan(userCenterUserid);
                if (plan != null) {
                    if (time == 1 || time == 2) {
                        if (plan.containsKey("breakfast")) {
                            JSONArray breakfastlist = plan.getJSONArray("breakfast");
                            if (breakfastlist != null && breakfastlist.size() > 0) {
                                content += "早餐推荐：";
                                for (int i = 0; i < breakfastlist.size(); i++) {
                                    JSONObject breakfast = breakfastlist.getJSONObject(i);
                                    if (breakfast != null) {
                                        content += breakfast.getString("name") + ",";
                                        //content += breakfast.getString("amount");
                                        //content += breakfast.getString("unitid") + ",";
                                        content += breakfast.getString("kcal");
                                        content = content.concat("卡路里 ");
                                    }
                                }
                            }
                        }
                    }
                    if (time == 1 || time == 3) {
                        if (plan.containsKey("lunch")) {
                            JSONArray lunchlist = plan.getJSONArray("lunch");
                            if (lunchlist != null && lunchlist.size() > 0) {
                                content += "午餐推荐：";
                                for (int i = 0; i < lunchlist.size(); i++) {
                                    JSONObject lunch = lunchlist.getJSONObject(i);
                                    if (lunch != null) {
                                        content += lunch.getString("name") + ",";
                                        //       content += lunch.getString("amount");
                                        //     content += lunch.getString("unitid") + ",";
                                        content += lunch.getString("kcal");
                                        content = content.concat("卡路里 ");
                                    }
                                }
                            }
                        }
                    }
                    if (time == 1 || time == 4) {
                        if (plan.containsKey("dinner")) {
                            JSONArray dinnerlist = plan.getJSONArray("dinner");
                            if (dinnerlist != null && dinnerlist.size() > 0) {
                                content += "晚餐推荐：";
                                for (int i = 0; i < dinnerlist.size(); i++) {
                                    JSONObject dinner = dinnerlist.getJSONObject(i);
                                    if (dinner != null) {
                                        content += dinner.getString("name") + ",";
                                        //content += dinner.getString("amount");
                                        //content += dinner.getString("unitid") + ",";
                                        content += dinner.getString("kcal");
                                        content = content.concat("卡路里 ");
                                    }
                                }
                            }
                        }
                    }
                }

            }


        }
        if (StringUtils.isEmpty(content)) {
            content = "对不起，没有查询到推荐的饮食计划";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }


    /**
     * 获得虚拟用户的信息
     *
     * @param token 用户中心token
     */
    public JSONObject getFictitiousUserInfo(String token, String nikename) {
        JSONObject userinfo = getUserFictitious(token, nikename);
        if (userinfo == null) {
            return null;
        }
        JSONObject userdetailinfo = getUserDetailInfo(userinfo);
        userdetailinfo.put("userid", userinfo.getString("userId"));
        userdetailinfo.put("id", userinfo.getString("id"));
        return userdetailinfo;
    }

    /**
     * 获得用户的信息
     *
     * @param token 用户中心token
     */
    public JSONObject getUserHearthInfo(String token) {
        JSONObject userinfo = getUserbaseInfoformUsercenter(token);
        return getUserDetailInfo(userinfo);
    }

    /**
     * 获取用户全部健康信息
     *
     * @param userinfo 用户基本信息
     */
    public JSONObject getUserDetailInfo(JSONObject userinfo) {

        if (userinfo != null) {
            //生日转年龄
            int age = 0;
            if (userinfo.containsKey("birthday")) {
                String birthday = userinfo.getString("birthday");
                if (!StringUtils.isEmpty(birthday)) {

                    Date date = DateUtils.stringToDate(birthday);
                    age = DateUtils.getYearByMinusDate(date, new Date());
                    //TODO age为0的时候接口错误
                    if (0 == age) {
                        age = 1;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }

            //性别
            String sex = "1";
            if (userinfo.containsKey("gender")) {
                if (GENDER_MAN.equals(userinfo.getString("gender"))) {
                    sex = "1";
                } else {
                    sex = "2";
                }
            } else if (userinfo.containsKey("sex")) {
                sex = userinfo.getString("sex");
            } else {
                return null;
            }

            //身高预处理
            Double height = 0d;
            if (userinfo.containsKey("height") && userinfo.containsKey("weight")) {
                height = userinfo.getDouble("height") / 100;
            } else {
                return null;
            }

            JSONObject json = new JSONObject();
            //json.put("userId", userid);
            json.put("userId", userinfo.get("userId"));
            json.put("weight", userinfo.getString("weight"));
            json.put("height", height);
            json.put("age", age);
            json.put("sex", sex);
            String res = HttpUtil.postJson(URL_Constants.URL_HEALTH + ACTION_GETHEALTHYINFO, json.toJSONString());
            if (JsonUtil.isJsonObject(res)) {
                JSONObject resJson = JSONObject.parseObject(res);
                if (resJson.containsKey("ok") && resJson.getBooleanValue("ok")) {
                    JSONObject retjsonobj = resJson.getJSONObject("data");

                    retjsonobj.put("weight", userinfo.getString("weight"));
                    retjsonobj.put("height", height);
                    retjsonobj.put("age", age);
                    retjsonobj.put("sex", sex);
                    return retjsonobj;
                }
            }
        }
        return null;

    }


    /**
     * 获取用户基础数据数据 --（接口停用）
     */
    @Deprecated
    public JSONObject getUserbaseInfo(String userid) {
        JSONObject json = new JSONObject();
        json.put("userId", userid);
        String res = HttpUtil.postJson(URL_HEALTH + ACTION_GETHISTORYINFO, json.toJSONString());
        if (JsonUtil.isJsonObject(res)) {
            System.out.println("res==" + res);
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("ok") && resJson.getBooleanValue("ok")) {
                JSONArray userinfos = resJson.getJSONObject("data").getJSONArray("data");

                JSONObject saveuserINfo = null;
                Date newdate = null;
                if (userinfos.size() > 0) {

                    for (int i = 0; i < userinfos.size(); i++) {
                        JSONObject userinfo = userinfos.getJSONObject(i);
                        if (newdate == null) {
                            saveuserINfo = userinfo;
                            newdate = DateUtils.stringToDate(userinfo.getString("createTime"));
                        } else {

                            Date date = DateUtils.stringToDate(userinfo.getString("createTime"));
                            if (date.after(newdate)) {
                                saveuserINfo = userinfo;
                                newdate = date;
                            }
                        }
                    }
                    return saveuserINfo;

                }
            }
        } else {
            return null;
        }
        return null;
    }

    /**
     * 获取用户中心数据
     * 需要用户中心token
     */
    public JSONObject getUserbaseInfoformUsercenter(String token) {
        JSONObject json = new JSONObject();
        String res = HttpUtil.postByAuthorization(URL_HEALTH_USERCENTER + ACTION_GETUSERINFO, json.toJSONString(), token);
        if (JsonUtil.isJsonObject(res)) {
            return JSONObject.parseObject(res);
        }
        return null;
    }

    /**
     * 获取用户中心虚拟用户数据
     *
     * @param token 用户中心token
     */
    public JSONObject getUserFictitious(String token, String nickname) {
        JSONObject json = new JSONObject();
        String res = HttpUtil.postByAuthorization(URL_HEALTH_USERCENTER + ACTION_FICTITIOUS, json.toJSONString(), token);
        if (JsonUtil.isJsonObject(res)) {
            JSONObject resjson = JSONObject.parseObject(res);
            if (resjson.containsKey("success") && resjson.getBoolean("success") && resjson.containsKey("data")) {
                JSONArray userlist = resjson.getJSONArray("data");
                for (int i = 0; i < userlist.size(); i++) {
                    JSONObject user = userlist.getJSONObject(i);
                    if (nickname.equals(user.getString("nickName"))) {
                        return user;
                    }
                }
                return null;
            }
        }
        return null;
    }

    /**
     * 虚拟用户 获取饮食计划
     */
    public String insertInfoAndGetdietPlan(String token, String nikename) {
        JSONObject userherarinfo = getFictitiousUserInfo(token, nikename);
        if (userherarinfo == null) {
            return null;
        }
        if (insertFictitiousUser2HearthSys(userherarinfo)) {

            return userherarinfo.getString("userid") + userherarinfo.getString("id");
        }
        return null;
    }

    /**
     * 获取饮食计划
     */
    public JSONObject getDietPlan(String userid) {
        JSONObject json = new JSONObject();
        json.put("userId", userid);
        String res = HttpUtil.postJson(URL_Constants.URL_HEALTH + ACTION_GETDIETPLAN, json.toJSONString());
        if (JsonUtil.isJsonObject(res)) {
            System.out.println("res==" + res);
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("ok") && resJson.getBooleanValue("ok")) {
                JSONArray planAllWeek = resJson.getJSONArray("data");
                if (planAllWeek != null && planAllWeek.size() != 0) {
                    for (int i = 0; i < planAllWeek.size(); i++) {
                        JSONObject dateplan = planAllWeek.getJSONObject(i);
                        String now = DateUtils.dateToString(new Date());
                        if (dateplan.containsKey("date")
                                && now.equals(dateplan.getString("date"))
                                && dateplan.containsKey("diet")) {
                            return dateplan.getJSONObject("diet");
                        }
                    }
                }
                return null;
            }
        }
        return null;
    }

    /**
     * 获取步数
     */
    public JSONObject getStep(String userid, String date) {
        JSONObject json = new JSONObject();
        json.put("userId", userid);
        json.put("startDate", date);
        json.put("pageSize", 20);
        json.put("pageNo", 1);
        json.put("endDate", date);
        String res = HttpUtil.postJson(URL_HEALTH + ACTION_GETSTEP, json.toJSONString());
        if (!"".equals(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("ok") && resJson.getBooleanValue("ok")) {
                return resJson.getJSONObject("data");
            }
        }

        return null;
    }

    /**
     * 更新或者插入虚拟用户数据到 健康系统当中
     */
    public boolean insertFictitiousUser2HearthSys(JSONObject userinfo) {
        JSONObject json = new JSONObject();
        String userid = userinfo.getString("userId") + userinfo.getString("id");
        json.put("userId", userid);
        json.put("userIdCenter", userid);
        json.put("weight", userinfo.getString("weight"));
        json.put("height", userinfo.getString("height"));
        json.put("age", userinfo.getString("age"));
        json.put("fat", userinfo.getString("fat"));
        json.put("sex", userinfo.getString("sex"));
        json.put("createTime", DateUtils.dateToString(new Date()));
        String res = HttpUtil.postJson(URL_HEALTH + ACTION_SAVEUSERINFO, json.toJSONString());
        if (JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("ok")) {
                return resJson.getBoolean("ok");
            }
        }
        return false;
    }

    /**
     * TODO 没必要查询
     */
    public void getFUserFromHearth(JSONObject userinfo) {
        JSONObject json = new JSONObject();
        json.put("userId", userinfo.getString("userid"));
        String res = HttpUtil.postJson(URL_HEALTH + ACTION_GETUSERINFO_HEARTH, json.toJSONString());
        if (JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("ok")) {
            }
        }
    }

    /**
     * 生日转年龄
     */
    public int birthday2age(String birthday) {
        Date date = DateUtils.stringToDate(birthday);
        int age = DateUtils.getYearByMinusDate(date, new Date());
        //TODO age为0的时候接口错误
        if (0 == age) {
            age = 1;
        }
        return age;
    }


    public void main(String[] args) {
     /*   JSONObject js = getUserHearthInfo("7235c10a-1ff7-410a-bf1d-9ef12a3f84c3");
        System.out.println((js == null) ? "null" : js.toJSONString());*/


        String j = insertInfoAndGetdietPlan("f1b74e91-a99d-442b-b543-9de69ef05d02", "爸爸");
        System.out.println(j);
    }


}
