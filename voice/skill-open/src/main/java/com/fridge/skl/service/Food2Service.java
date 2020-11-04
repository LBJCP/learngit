package com.fridge.skl.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.dto.UnlifeFoodMapper;
import com.fridge.skl.entity.FoodLocation;
import com.fridge.skl.entity.UnlifeFood;
import com.fridge.skl.entity.UnlifeRelativefood;
import com.fridge.skl.entity.UnlifeTogetherfood;
import com.fridge.skl.model.GuessIntent;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.model.Slot;
import com.fridge.skl.model.context.ControlDevices;
import com.fridge.skl.model.unilifemodel.FoodInfo;
import com.fridge.skl.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.fridge.skl.util.URL_Constants.ACTION_GETFOODLIST;
import static com.fridge.skl.util.URL_Constants.URL_UNILIFE;

@Slf4j
@Service
public class Food2Service extends BaseService {
    @Autowired
    UnlifeFoodMapper unlifeFoodMapper;

    @Autowired
    CacheService cacheService;

    @Autowired
    RedisUtil redisUtil;


    /**
     * 查询食材怎么存
     *
     * @param request 上游参数
     * @return
     */
    public ResponseObj getHowtoSave(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";
        String food = getSlot(request, UtilConstants.Slot.SLOT_FOODLIST_NAME);
        UnlifeFood foodinfo = cacheService.getPublicFoodInfoByName(food);
        ControlDevices controlDevices = getControldevice(request);
        if (controlDevices != null && "Fridge".equals(controlDevices.getType())) {
            FoodLocation foodLocation = cacheService.getfoodlocation(controlDevices.getWifiType(), food);
            if (foodLocation != null) {
                content = food + "在您的冰箱中建议存储在"
                        + foodLocation.getLoacationname() + "，建议存储温度：" +
                        foodinfo.getStorec() + "度，保质期" + foodinfo.getShelflife() + "天";
            }
        }
        if (StringUtils.isEmpty(content)) {
            content = food + "建议存储在"
                    + foodinfo.getStorearea() + "，建议存储温度：" +
                    foodinfo.getStorec() + "度，保质期" + foodinfo.getShelflife() + "天";
        }

        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }


    /**
     * 查询食材的保质期
     *
     * @param request 上游参数
     * @return
     */
    public ResponseObj getSelfLife(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "我假装没听见你说话，你就不知道我其实是不知道了";
        GuessIntent[] intents = request.getRequest().getIntents();


        GuessIntent intent = intents[0];
        Slot[] slots = intent.getSlots();

        String food = getSlot(slots, UtilConstants.Slot.SLOT_FOODLIST_NAME);
        if (food != null) {

            content = getRandomAnswer(ANSWERLIST_SELFLIFE, food, cacheService.getPublicFoodInfoByName(food).getShelflife() + "天");
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 查询食材冷冻时间
     *
     * @param request
     * @return
     */
    public ResponseObj getColdTme(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "我假装没听见你说话，你就不知道我其实是不知道了";
        GuessIntent[] intents = request.getRequest().getIntents();


        GuessIntent intent = intents[0];
        Slot[] slots = intent.getSlots();

        String foodname = getSlot(slots, UtilConstants.Slot.SLOT_FOODLIST_NAME);
        if (StringUtils.isEmpty(foodname)) {
            foodname = findword(request.getRequest().getQuery().getContent(), FOODNAMELIST);
        }
        String mode = getSlot(slots, UtilConstants.Slot.SLOT_REFMODE_NAME);
        if (StringUtils.isEmpty(mode)) {
            mode = findword(request.getRequest().getQuery().getContent(), MODEMAP);
        }
        if (!StringUtils.isEmpty(foodname) && !StringUtils.isEmpty(mode)) {

            UnlifeFood unlifeFood = cacheService.getPublicFoodInfoByName(foodname);
            switch (mode) {
                case "冷藏":
                    content = answer4cold(foodname, unlifeFood);
                    break;
                case "冷冻":
                    content = answer4freezing(foodname, unlifeFood);
                    break;
                case "放冰箱":
                default:
                    if (foodname.equals("西兰花") && request.getContext().getControlDevices()[0].getWifiType().equals("200861051c4085040121ad3d3057de000000643c84967e5cda6771250b3e8240")) {
                        resp.getResponse().getSpeech().setContent("小优帮您查了下，您的卡萨帝冰箱存储" + foodname + "建议放在左侧冷藏抽屉，建议存放温度-12度，放7天没问题");

                        return resp;
                    }
                    content = answer4ref(foodname, unlifeFood);
                    break;
            }


        }

        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 查询食材适合人群
     *
     * @param request
     * @return
     */
    public ResponseObj getFitUser(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "我假装没听见你说话，你就不知道我其实是不知道了";
        GuessIntent[] intents = request.getRequest().getIntents();


        GuessIntent intent = intents[0];
        Slot[] slots = intent.getSlots();

        String food = getSlot(slots, UtilConstants.Slot.SLOT_FOODLIST_NAME);
        if (StringUtils.isEmpty(food)) {
            food = findword(request.getRequest().getQuery().getContent(), FOODNAMELIST);
        }
        if (!StringUtils.isEmpty(food)) {
            UnlifeFood unlifeFood = cacheService.getPublicFoodInfoByName(food);
            if (!StringUtils.isEmpty(unlifeFood.getFituser())) {
                content = food + cacheService.getPublicFoodInfoByName(food).getFituser();
            }
        }

        if (!StringUtils.isEmpty(content)) {
            resp.getResponse().getSpeech().setContent(content);
        }
        return resp;
    }

    /**
     * 查询食材饮食禁忌
     *
     * @param request
     * @return
     */
    public ResponseObj getAvoidUser(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";
        GuessIntent[] intents = request.getRequest().getIntents();


        GuessIntent intent = intents[0];
        Slot[] slots = intent.getSlots();

        String foodname = getSlot(slots, UtilConstants.Slot.SLOT_FOODLIST_NAME);
        if (StringUtils.isEmpty(foodname)) {
            foodname = findword(request.getRequest().getQuery().getContent(), FOODNAMELIST);
        }
        String user = getSlot(slots, UtilConstants.Slot.SLOT_USER_NAME);
        if (StringUtils.isEmpty(user)) {
            user = findword(request.getRequest().getQuery().getContent(), USERMAP);
        }
        if (!StringUtils.isEmpty(foodname)) {
            UnlifeFood unlifeFood = cacheService.getPublicFoodInfoByName(foodname);

            if (StringUtils.isEmpty(user)) {
                content = getRandomAnswer(ANSWERLIST_AVOIDUSER, foodname, unlifeFood.getAvoiduser());
            } else {
                String avoiduser = unlifeFood.getAvoiduser();

                int index = avoiduser.indexOf(usersearchword.get(user));
                if (index == -1) {
                    content = "小优帮您查了下，" + user + "并没有" + foodname + "相关的饮食禁忌，" + foodname + unlifeFood.getFituser();
                } else {
                    content = "小优帮您查了下，" + user + "确实应该谨慎食用" + foodname + "，"
                            + foodname + "的饮食禁忌如下：" + unlifeFood.getAvoiduser();
                }

            }

        }


        if (!StringUtils.isEmpty(content)) {
            resp.getResponse().getSpeech().setContent(content);
        }
        return resp;
    }

    /**
     * 查询食材搭配推荐
     *
     * @param request
     * @return
     */
    public ResponseObj getTogetherFood(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";
        GuessIntent[] intents = request.getRequest().getIntents();

        GuessIntent intent = intents[0];
        Slot[] slots = intent.getSlots();

        String foodname = getSlot(slots, UtilConstants.Slot.SLOT_FOODLIST_NAME);
        if (StringUtils.isEmpty(foodname)) {
            foodname = findword(request.getRequest().getQuery().getContent(), FOODNAMELIST);
        }
        if (!StringUtils.isEmpty(foodname)) {
            UnlifeFood unlifeFood = cacheService.getPublicFoodInfoByName(foodname);
            List<UnlifeTogetherfood> unlifeTogetherfoods = cacheService.getTogetherfood(unlifeFood.getId());
            if (unlifeTogetherfoods != null && unlifeTogetherfoods.size() > 0) {
                Random random = new Random();
                UnlifeTogetherfood unlifeTogetherfood = unlifeTogetherfoods.get(random.nextInt(unlifeTogetherfoods.size()));
                content = foodname + "搭配" + unlifeTogetherfood.getTogetherfoodname()
                        + "是不错的选择，有" + unlifeTogetherfood.getReason() + "的功效";
            } else {
                //没有搭配
                content = "小优的数据库中没有查到" + foodname + "适合搭配的食材呢，是不是它太特别了";
            }

        }


        if (!StringUtils.isEmpty(content)) {
            resp.getResponse().getSpeech().setContent(content);
        }
        return resp;
    }

    /**
     * 食材搭配禁忌
     *
     * @param request
     * @return
     */
    public ResponseObj getRelativeFood(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";

        String foodname = getSlot(request, UtilConstants.Slot.SLOT_FOODLIST_NAME);
        if (StringUtils.isEmpty(foodname)) {
            foodname = findword(request.getRequest().getQuery().getContent(), FOODNAMELIST);
        }
        if (!StringUtils.isEmpty(foodname)) {
            UnlifeFood unlifeFood = cacheService.getPublicFoodInfoByName(foodname);
            List<UnlifeRelativefood> unlifeRelativefoods = cacheService.getRelativefood(unlifeFood.getId());
            if (unlifeRelativefoods != null && unlifeRelativefoods.size() > 0) {
                StringBuilder sb = new StringBuilder();
                unlifeRelativefoods.forEach(unlifeRelativefood -> {
                    sb.append(unlifeRelativefood.getRelativefoodname());
                    sb.append("、");
                });
                content = foodname + "搭配" + CommonUtil.cutlaststr(sb, "、") + "等食材的时候要特别注意哦";
            } else {
                //没有搭配
                content = "小优的数据库中没有查到" + foodname + "有特殊的搭配禁忌，可以放心食用";
            }

        }


        if (!StringUtils.isEmpty(content)) {
            resp.getResponse().getSpeech().setContent(content);
        }
        return resp;
    }

    /**
     * 查询两种食材能否一起吃
     *
     * @param request
     * @return
     */
    public ResponseObj getTwoFoodFit(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";

        String foodname = getSlot(request, "food");

        String fitfoodname = getSlot(request, "food1");

        if (!StringUtils.isEmpty(foodname) && !StringUtils.isEmpty(fitfoodname)) {
            UnlifeFood unlifeFood = cacheService.getPublicFoodInfoByName(foodname);
            List<UnlifeRelativefood> unlifeRelativefoods = cacheService.getRelativefood(unlifeFood.getId());
            List<UnlifeTogetherfood> unlifeTogetherfoods = cacheService.getTogetherfood(unlifeFood.getId());
            UnlifeRelativefood relativefood = null;
            UnlifeTogetherfood togetherfood = null;
            if (!CollectionUtils.isEmpty(unlifeRelativefoods)) {
                for (UnlifeRelativefood unlifeRelativefood : unlifeRelativefoods) {
                    if (fitfoodname.equals(unlifeRelativefood.getRelativefoodname())) {
                        relativefood = unlifeRelativefood;
                        break;
                    }
                }

            }
            if (!CollectionUtils.isEmpty(unlifeTogetherfoods)) {
                for (UnlifeTogetherfood unlifeTogetherfood : unlifeTogetherfoods) {
                    if (fitfoodname.equals(unlifeTogetherfood.getTogetherfoodname())) {
                        togetherfood = unlifeTogetherfood;
                        break;
                    }
                }

            }
            if (relativefood != null) {
                content = foodname + "不适合搭配" + fitfoodname + "," + relativefood.getReason();
            } else if (togetherfood != null) {
                content = foodname + "搭配" + fitfoodname + "是不错的选择，一起吃有" + togetherfood.getReason() + "的功效";
            } else {
                content = "小优没有找到" + foodname + "搭配" + fitfoodname + "的禁忌";
            }
        }


        if (!StringUtils.isEmpty(content)) {
            resp.getResponse().getSpeech().setContent(content);
        }
        return resp;
    }

    /**
     * 查询冰箱里某食材存了多久了
     */
    public ResponseObj queryFoodSavedTime(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";
        GuessIntent[] intents = request.getRequest().getIntents();


        GuessIntent intent = intents[0];
        Slot[] slots = intent.getSlots();

        String food = getSlot(slots, UtilConstants.Slot.SLOT_FOODLIST_NAME);
        if (StringUtils.isEmpty(food)) {
            food = findword(request.getRequest().getQuery().getContent(), FOODNAMELIST);
        }
        String deviceid = request.getContext().getDevice().getDeviceId();

        List<FoodInfo> foodlist = getFoodList(deviceid);
        if (!CollectionUtils.isEmpty(foodlist)) {
            int count = 0;
            ConcurrentHashMap<Long, Integer> fooddaysmap = new ConcurrentHashMap<>();
            for (FoodInfo foodinfo : foodlist) {
                if (food.equals(foodinfo.getName())) {
                    Long savedays = DateUtils.getDaysNumBetweenTwoDays(new Date(foodinfo.getCreateTime()), new Date());
                    if (fooddaysmap.containsKey(savedays)) {
                        fooddaysmap.put(savedays, fooddaysmap.get(savedays) + 1);
                    } else {
                        fooddaysmap.put(savedays, 1);
                    }


                    count++;
                }
            }
            StringBuilder contentsb = new StringBuilder();
            if (fooddaysmap.size() == 0) {
                content = getRandomAnswer(ANSWERLIST_NO_FOOD, food);
            } else if (fooddaysmap.size() == 1) {

                contentsb.append("冰箱里有");
                contentsb.append(move2toliang(count));
                contentsb.append("份");
                contentsb.append(food);
                for (Map.Entry<Long, Integer> entry : fooddaysmap.entrySet()) {
                    if (entry.getKey() == 0l) {
                        contentsb.append("今天刚存入");
                    } else {
                        contentsb.append("存了");
                        contentsb.append(move2toliang(entry.getKey()));
                        contentsb.append("天了");

                    }
                }
                content = CommonUtil.cutlaststr(contentsb.toString(), "，");
            } else {
                contentsb.append("您的冰箱里有").append(move2toliang(count)).append("份").append(food).append("，");
                for (Map.Entry<Long, Integer> entry : fooddaysmap.entrySet()) {
                    if (entry.getKey() == 0l) {
                        contentsb.append("今天存入的有");
                        contentsb.append(move2toliang(entry.getValue()));
                        contentsb.append("份，");
                    } else {
                        contentsb.append("存了");
                        contentsb.append(move2toliang(entry.getKey()));
                        contentsb.append("天的有");
                        contentsb.append(move2toliang(entry.getValue()));
                        contentsb.append("份，");

                    }
                }
                content = CommonUtil.cutlaststr(contentsb.toString(), "，");
            }


        } else {
            content = getRandomAnswer(ANSWERLIST_NO_FOOD, food);
        }


        if (!StringUtils.isEmpty(content)) {
            resp.getResponse().getSpeech().setContent(content);
        }
        return resp;
    }

    /**
     * 查询冰箱某食材是否过期
     */
    public ResponseObj queryFoodExpire(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";
        GuessIntent[] intents = request.getRequest().getIntents();


        GuessIntent intent = intents[0];
        Slot[] slots = intent.getSlots();

        String food = getSlot(slots, UtilConstants.Slot.SLOT_FOODLIST_NAME);
        if (StringUtils.isEmpty(food)) {
            food = findword(request.getRequest().getQuery().getContent(), FOODNAMELIST);
        }
        String deviceid = request.getContext().getDevice().getDeviceId();
        if ("0025921943ba".equals(deviceid)) {
            deviceid = "b02bce2a7383";
        }
        List<FoodInfo> foodlist = getFoodList(deviceid);

        if (!CollectionUtils.isEmpty(foodlist)) {
            int count = 0;
            int countexpir = 0;
            for (FoodInfo foodinfo : foodlist) {
                if (food.equals(foodinfo.getName())) {
                    Long savedays = DateUtils.getDaysNumBetweenTwoDays(new Date(foodinfo.getCreateTime()), new Date());
                    if (savedays > foodinfo.getShelfLife()) {
                        countexpir++;
                    }
                    count++;
                }
            }

            if (count == 0) {
                content = getRandomAnswer(ANSWERLIST_NO_FOOD, food);
            } else if (count == 1) {
                if (countexpir == 1) {
                    content = "冰箱里的" + food + "过期了,赶紧处理下吧";
                } else {
                    content = getRandomAnswer(ANSWERLIST_NO_EXPIRFOOD, food);
                }

            } else {
                if (countexpir == 0) {
                    content = "冰箱里有" + move2toliang(count) + "份" + food + "都很新鲜呢";
                } else {
                    content = "冰箱里有" + move2toliang(count) + "份" + food + ",其中有" + move2toliang(countexpir) + "份过期了";
                }
            }
        } else {
            content = getRandomAnswer(ANSWERLIST_NO_FOOD, food);
        }


        if (!StringUtils.isEmpty(content)) {
            resp.getResponse().getSpeech().setContent(content);
        }
        return resp;
    }


    /**
     * 查询冰箱某食材过期时间
     */
    public ResponseObj queryFoodExpireTime(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";
        GuessIntent[] intents = request.getRequest().getIntents();


        GuessIntent intent = intents[0];
        Slot[] slots = intent.getSlots();

        String food = getSlot(slots, UtilConstants.Slot.SLOT_FOODLIST_NAME);

        String deviceid = getControldevice(request).getId();

        List<FoodInfo> foodlist = getFoodList(deviceid);

        Map<Long, Integer> expireddaymap = new HashMap<>();

        if (!CollectionUtils.isEmpty(foodlist)) {
            int count = 0;
            for (FoodInfo foodinfo : foodlist) {
                if (food.equals(foodinfo.getName())) {
                    Long savedays = DateUtils.getDaysNumBetweenTwoDays(new Date(foodinfo.getCreateTime()), new Date());
                    Long expireddays = foodinfo.getShelfLife() - savedays;
                    if (expireddaymap.containsKey(expireddays)) {
                        expireddaymap.put(expireddays, expireddaymap.get(expireddays) + 1);
                    } else {
                        expireddaymap.put(expireddays, 1);
                    }
                    count++;
                }
            }
            StringBuilder contsb = new StringBuilder();
            if (expireddaymap.size() == 0) {
                content = getRandomAnswer(ANSWERLIST_NO_FOOD, food);
            } else if (expireddaymap.size() == 1) {

                expireddaymap.forEach((key, value) -> {
                    contsb.append("冰箱里的");
                    contsb.append(value);
                    contsb.append("份");
                    contsb.append(food);
                    contsb.append("，");
                    if (key > 0) {
                        contsb.append(key);
                        contsb.append("天后过期");
                    } else if (key == 0) {
                        contsb.append("今天过期");
                    } else {
                        contsb.append("已经过期");
                        contsb.append(-key);
                        contsb.append("天");
                    }
                });
                content = CommonUtil.cutlaststr(contsb.toString(), "，");
            } else {
                contsb.append("冰箱里有");
                contsb.append(count);
                contsb.append("份");
                contsb.append(food);
                expireddaymap.forEach((key, value) -> {
                    if (key > 0) {
                        contsb.append(key);
                        contsb.append("天后过期的有");
                        contsb.append(value);
                        contsb.append("份，");
                    } else if (key == 0) {
                        contsb.append("今天过期的");
                        contsb.append(value);
                        contsb.append("份，");
                    } else {
                        contsb.append("已经过期");
                        contsb.append(-key);
                        contsb.append("天的");
                        contsb.append(value);
                        contsb.append("份，");
                    }
                });
                content = CommonUtil.cutlaststr(contsb.toString(), "，");
            }

        } else {
            content = getRandomAnswer(ANSWERLIST_NO_FOOD, food);
        }


        if (!StringUtils.isEmpty(content)) {
            resp.getResponse().getSpeech().setContent(content);
        }
        return resp;
    }


    /**
     * 查询某食材存在哪
     */
    public ResponseObj queryFoodLocation(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";
        GuessIntent[] intents = request.getRequest().getIntents();

        GuessIntent intent = intents[0];
        Slot[] slots = intent.getSlots();

        String food = getSlot(slots, UtilConstants.Slot.SLOT_FOODLIST_NAME);
        if (StringUtils.isEmpty(food)) {
            food = findword(request.getRequest().getQuery().getContent(), FOODNAMELIST);
        }
        // 食材管理采用的都是小写mac
        String deviceid = request.getContext().getDevice().getDeviceId().toLowerCase();
        List<FoodInfo> foodlist = getFoodList(deviceid);

        if (!CollectionUtils.isEmpty(foodlist)) {
            int count = 0;
            ConcurrentHashMap<String, Integer> foodlocation = new ConcurrentHashMap();

            for (FoodInfo foodinfo : foodlist) {
                if (food.equals(foodinfo.getName())) {

                    if (!foodlocation.containsKey(foodinfo.getLocation())) {
                        foodlocation.put(foodinfo.getLocation(), 1);
                    } else {
                        int locationnum = foodlocation.get(foodinfo.getLocation());
                        foodlocation.put(foodinfo.getLocation(), locationnum + 1);
                    }

                    count++;
                }

            }

            if (count == 0) {
                content = getRandomAnswer(ANSWERLIST_NO_FOOD, food);
            } else {
                StringBuilder contentbuilder = new StringBuilder("冰箱里有");
                contentbuilder.append(count);
                contentbuilder.append("份");
                contentbuilder.append(food);
                contentbuilder.append("，");
                if (foodlocation.size() == 1) {
                    if (count > 1) {
                        contentbuilder.append("都");
                    }
                    contentbuilder.append("放在");
                    foodlocation.forEach((key, value) -> {
                        contentbuilder.append(CommonUtil.getKey(CommonUtil.getFridgeLocation(), Integer.valueOf(key)));
                    });
                } else {
                    foodlocation.forEach((key, value) -> {
                        contentbuilder.append(CommonUtil.getKey(CommonUtil.getFridgeLocation(), Integer.valueOf(key)));
                        contentbuilder.append("中有");
                        contentbuilder.append(value);
                        contentbuilder.append("份，");
                    });
                }
                content = CommonUtil.cutlaststr(contentbuilder.toString(), "，");
            }
        } else {
            content = getRandomAnswer(ANSWERLIST_NO_FOOD, food);
        }


        if (!StringUtils.isEmpty(content)) {
            resp.getResponse().getSpeech().setContent(content);
        }
        return resp;
    }


    /**
     * 查询冰箱里某食材那天放进去的
     *
     * @param request 请求
     */
    public ResponseObj queryInputDay(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";
        StringBuilder contentsb = new StringBuilder();
        GuessIntent[] intents = request.getRequest().getIntents();


        GuessIntent intent = intents[0];
        Slot[] slots = intent.getSlots();

        String food = getSlot(slots, UtilConstants.Slot.SLOT_FOODLIST_NAME);
        if (StringUtils.isEmpty(food)) {
            food = findword(request.getRequest().getQuery().getContent(), FOODNAMELIST);
        }
        String deviceid = request.getContext().getDevice().getDeviceId();
        if ("0025921943ba".equals(deviceid)) {
            deviceid = "b02bce2a7383";
        }
        List<FoodInfo> foodlist = getFoodList(deviceid);
        ConcurrentHashMap<String, Integer> fooddaysmap = new ConcurrentHashMap<>();
        if (!CollectionUtils.isEmpty(foodlist)) {
            int count = 0;
            for (FoodInfo foodinfo : foodlist) {
                if (food.equals(foodinfo.getName())) {
                    String saveday = DateUtils.getDayChinsesName(foodinfo.getCreateTime());
                    if (fooddaysmap.containsKey(saveday)) {
                        fooddaysmap.put(saveday, fooddaysmap.get(saveday) + 1);
                    } else {
                        fooddaysmap.put(saveday, 1);
                    }
                    count++;
                }
            }
            if (fooddaysmap.size() == 0) {
                content = getRandomAnswer(ANSWERLIST_NO_FOOD, food);
            } else if (fooddaysmap.size() == 1) {
                contentsb.append("冰箱里有").append(move2toliang(count)).append("份").append(food);
                for (Map.Entry<String, Integer> entry : fooddaysmap.entrySet()) {
                    if (count > 1) {
                        contentsb.append("都是");
                    } else {
                        contentsb.append("是");
                    }
                    contentsb.append(entry.getKey());
                    contentsb.append("存入的");


                }
                content = contentsb.toString();
            } else {
                contentsb.append("您的冰箱里有").append(move2toliang(count)).append("份").append(food).append("，其中");
                for (Map.Entry<String, Integer> entry : fooddaysmap.entrySet()) {

                    contentsb.append(move2toliang(entry.getKey()));
                    contentsb.append("存入的有");
                    contentsb.append(move2toliang(entry.getValue()));
                    contentsb.append("份，");


                }
                content = CommonUtil.cutlaststr(contentsb.toString(), "，");
            }

        } else {
            content = getRandomAnswer(ANSWERLIST_NO_FOOD, food);
        }


        if (!StringUtils.isEmpty(content)) {
            resp.getResponse().getSpeech().setContent(content);
        }
        return resp;
    }

    /**
     * 查询有什么水果（食材分类）
     *
     * @param request
     * @return
     */
    public ResponseObj queryFoodCategory(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";
        GuessIntent[] intents = request.getRequest().getIntents();


        GuessIntent intent = intents[0];
        Slot[] slots = intent.getSlots();
        String foodcategory = getSlot(slots, UtilConstants.Slot.SLOT_FOODCATEGORY_NAME);

        String deviceid = request.getContext().getDevice().getDeviceId();
        if ("0025921943ba".equals(deviceid)) {
            deviceid = "b02bce2a7383";
        }
        List<FoodInfo> foodlist = getFoodList(deviceid);
        UnlifeFood unlifeFood;
        List<String> foodstrlist = new ArrayList<>();
        if (!CollectionUtils.isEmpty(foodlist)) {
            StringBuilder foodstr = new StringBuilder();
            String foodcategid = FOODCATEGORYMAP.get(foodcategory);
            for (FoodInfo foodinfo : foodlist) {

                unlifeFood = cacheService.getPublicFoodInfoByName(foodinfo.getName());

                if (unlifeFood != null && foodcategid.equals(unlifeFood.getClassifyfaceid())) {
                    addOnce(foodstrlist, foodinfo.getName());
                }
            }
            if (!CollectionUtils.isEmpty(foodstrlist)) {
                foodstr.append("冰箱里的").append(foodcategory).append("有:");
                foodstrlist.forEach(foodstr1 -> {
                    foodstr.append(foodstr1).append(",");
                });
                content = CommonUtil.cutlaststr(foodstr.toString(), ",");
            } else {
                content = "冰箱里没有" + foodcategory;
            }


        } else {
            content = "冰箱里没有" + foodcategory;
        }


        if (!StringUtils.isEmpty(content)) {
            resp.getResponse().getSpeech().setContent(content);
        }
        return resp;
    }

    /**
     * 冷藏食材回复
     *
     * @param food       食材名
     * @param unlifeFood 食材知识库
     * @return 回答内容
     */
    private String answer4ref(String food, UnlifeFood unlifeFood) {


        String content = food + "放冰箱建议放在" + unlifeFood.getStorearea() + "室" + ",最多可以存放";
        if ("冷藏".equals(unlifeFood.getStorearea())) {
            content += move2toliang(unlifeFood.getColdmaxday()) + "天";
        } else {
            content += move2toliang(unlifeFood.getFreezingmaxday()) + "天";
        }

        return content;
    }

    /**
     * 冷冻食材回复
     *
     * @param food       食材名
     * @param unlifeFood 食材知识库
     * @return 回答内容
     */
    private String answer4cold(String food, UnlifeFood unlifeFood) {
        String content;

        if ("冷藏".equals(unlifeFood.getNoadvisecabin())
                || unlifeFood.getColdmaxday() == null
                || unlifeFood.getColdmaxday() == 0
        ) {
            content = getRandomAnswer(ANSWERLIST_NOTAVOIDCOLD, food);
        } else {
            content = getRandomAnswer(ANSWERLIST_COLDTIME, food, move2toliang(unlifeFood.getColdmaxday()) + "天");
        }

        return content;
    }

    /**
     * 冰箱存储食材回复
     *
     * @param food       食材名
     * @param unlifeFood 食材知识库
     * @return 回答内容
     */
    private String answer4freezing(String food, UnlifeFood unlifeFood) {
        String content;

        if ("冷冻".equals(unlifeFood.getNoadvisecabin())
                || unlifeFood.getFreezingmaxday() == null
                || unlifeFood.getFreezingmaxday() == 0) {
            content = getRandomAnswer(ANSWERLIST_NOTAVOIDFREEZING, food);
        } else {
            content = getRandomAnswer(ANSWERLIST_FREEZINGTIME, food, move2toliang(unlifeFood.getFreezingmaxday()) + "天");
        }

        return content;
    }

    /**
     * 获取食材列表
     *
     * @param deviceId 冰箱的deviceid
     * @return 回答内容
     */
    public List<FoodInfo> getFoodList(String deviceId) {
        JSONObject json = new JSONObject();
        //食材管理都用的小写
        json.put("deviceId", deviceId.toLowerCase());
        json.put("location", -1);
        String res = HttpUtil.postJson(URL_UNILIFE + ACTION_GETFOODLIST, json.toJSONString());
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (0 == resJson.getIntValue("errorno")) {
                JSONArray foodArry = resJson.getJSONArray("data");
                return JSON.parseArray(foodArry.toJSONString(), FoodInfo.class);
            }

        }
        return null;

    }


    //怎么存
    private static final List<String> ANSWERLIST_WAYTOSAVE = new ArrayList<>();
    //保质期查询
    private static final List<String> ANSWERLIST_SELFLIFE = new ArrayList<>();
    //冷藏时间查询
    private static final List<String> ANSWERLIST_COLDTIME = new ArrayList<>();
    //冷冻时间查询
    private static final List<String> ANSWERLIST_FREEZINGTIME = new ArrayList<>();
    //不适合冷藏
    private static final List<String> ANSWERLIST_NOTAVOIDCOLD = new ArrayList<>();
    //不适合冷冻
    private static final List<String> ANSWERLIST_NOTAVOIDFREEZING = new ArrayList<>();
    //饮食禁忌
    private static final List<String> ANSWERLIST_AVOIDUSER = new ArrayList<>();

    //没有过期食材
    private static final List<String> ANSWERLIST_NO_EXPIRFOOD = new ArrayList<>();
    private static final List<String> ANSWERLIST_NO_FOOD = new ArrayList<>();

    //食材禁忌搜索词
    private static final ConcurrentHashMap<String, String> usersearchword = new ConcurrentHashMap<>();

    //食材列表
    private List<String> FOODNAMELIST = new ArrayList<>();
    private static List<String> MODELIST = new ArrayList<>();
    private static ConcurrentHashMap<String, String> MODEMAP = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> USERMAP = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, String> FOODCATEGORYMAP = new ConcurrentHashMap<>();

    static {
        ANSWERLIST_WAYTOSAVE.add("%s建议放在您家冰箱%s%s，建议存储温度%s-%s度，最多可以放%s天");


        ANSWERLIST_SELFLIFE.add("%s的保质期是%s");
        ANSWERLIST_SELFLIFE.add("小优帮您查到，%s的保质期是%s");
        ANSWERLIST_SELFLIFE.add("我掐指一算，%s的保质期是%s");
        ANSWERLIST_SELFLIFE.add("%s保质期为%s");
        ANSWERLIST_SELFLIFE.add("%s能放%s");
        ANSWERLIST_SELFLIFE.add("%s可以放%s");
        ANSWERLIST_SELFLIFE.add("%s%s内吃掉一般坏不了");

        ANSWERLIST_COLDTIME.add("%s冷藏可以放%s");
        ANSWERLIST_COLDTIME.add("小优帮您查到，%s放冷藏可以放%s");
        ANSWERLIST_COLDTIME.add("我掐指一算，%s冷藏可以放%s");
        ANSWERLIST_COLDTIME.add("%s冷藏能放%s");
        ANSWERLIST_COLDTIME.add("%s%s内放冷藏没问题");

        ANSWERLIST_FREEZINGTIME.add("%s冷冻可以放%s");
        ANSWERLIST_FREEZINGTIME.add("小优帮您查到，%s放冷冻可以放%s");
        ANSWERLIST_FREEZINGTIME.add("我掐指一算，%s冷冻可以放%s");
        ANSWERLIST_FREEZINGTIME.add("%s冷冻能放%s");
        ANSWERLIST_FREEZINGTIME.add("%s%s内放冷冻没问题");

        ANSWERLIST_NOTAVOIDCOLD.add("%s不适合冷藏");
        ANSWERLIST_NOTAVOIDCOLD.add("%s不建议放在冷藏");
        ANSWERLIST_NOTAVOIDCOLD.add("小优不建议把%s放在冷藏室");
        ANSWERLIST_NOTAVOIDCOLD.add("小优觉得%s不适合放在冷藏室");

        ANSWERLIST_NOTAVOIDFREEZING.add("%s不适合冷冻");
        ANSWERLIST_NOTAVOIDFREEZING.add("%不建议放在冷冻");
        ANSWERLIST_NOTAVOIDFREEZING.add("小优不建议把%s放在冷冻室");
        ANSWERLIST_NOTAVOIDFREEZING.add("小优觉得%s不适合放在冷冻室");

        ANSWERLIST_AVOIDUSER.add("小优帮您查到，%s的饮食禁忌如下：%s");
        ANSWERLIST_AVOIDUSER.add("%s的饮食禁忌如下：%s");

        ANSWERLIST_NO_EXPIRFOOD.add("冰箱里的%s还很新鲜呢");
        ANSWERLIST_NO_EXPIRFOOD.add("小优翻箱倒柜找了半天，冰箱里的%s都很新鲜呢，看来是真没有过期的");
        ANSWERLIST_NO_EXPIRFOOD.add("小优没找到冰箱里有过期的%s");
        ANSWERLIST_NO_EXPIRFOOD.add("冰箱里的%s并没有过期哦");

        ANSWERLIST_NO_FOOD.add("冰箱里没有%s");
        ANSWERLIST_NO_FOOD.add("小优找了半天也没找到冰箱里有%s");
        ANSWERLIST_NO_FOOD.add("冰箱里真的没有%s，是不是你偷偷吃了不告诉我");
        ANSWERLIST_NO_FOOD.add("咱家冰箱里真的没有%s");

        usersearchword.put("孕妇", "孕");
        usersearchword.put("老年人", "老");
        usersearchword.put("小孩", "小孩,儿童,婴儿");
        usersearchword.put("哺乳期", "产妇,哺乳,产后");
        usersearchword.put("糖尿病人", "糖尿");


        MODELIST.add("冷藏");
        MODELIST.add("冷冻");
        MODELIST.add("放冰箱");

        MODEMAP.put("冷藏", "保鲜，冷藏室");
        MODEMAP.put("冷冻", "冷冻室,冰冻,冻起来,冰镇");
        MODEMAP.put("放冰箱", "存在冰箱里,存冰箱,在冰箱里");

        USERMAP.put("小孩", "婴儿,小婴儿,小宝贝");
        USERMAP.put("孕妇", "孕妈妈,孕妈");
        USERMAP.put("哺乳期", "哺乳期间,产妇,坐月子,新产妇");
        USERMAP.put("糖尿病人", "糖尿病,有糖尿病");
        USERMAP.put("老年人", "老人,上年纪的人,上了年纪的人");


        FOODCATEGORYMAP.put("肉类", "1");
        FOODCATEGORYMAP.put("水产", "2");
        FOODCATEGORYMAP.put("蔬菜", "3");
        FOODCATEGORYMAP.put("蛋奶", "4");
        FOODCATEGORYMAP.put("水果", "5");
        FOODCATEGORYMAP.put("调味", "6");
        FOODCATEGORYMAP.put("酒水", "7");
        FOODCATEGORYMAP.put("其他", "8");

    }


    //启动加载
    @PostConstruct
    public void initFoodnamelist() {
        FOODNAMELIST = cacheService.getfoodlist();
    }


    private void addOnce(List<String> list, String str) {
        if (!list.contains(str)) {
            list.add(str);
        }
    }


}
