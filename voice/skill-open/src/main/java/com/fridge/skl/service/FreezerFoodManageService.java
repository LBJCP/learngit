package com.fridge.skl.service;

import com.fridge.skl.model.GuessIntent;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.model.Slot;
import com.fridge.skl.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
@Slf4j
public class FreezerFoodManageService extends BaseService {


    private static final String[] typeid = {
            "200861051c408504012100618006754500000000000061800351440000000040",
            "200861051c408504160400718001990000000000000071800162420000000040",
            "200861051c408504160400718001994400000000000071800162470000000040",
            "200861051c408504160400718001994600000000000071800162480000000040",
            "200861051c408504160400718002124400000000000071800192c00000000040"};


    /**
     * 添加食材
     */
    //添加食材到指定层数
    public ResponseObj addFoodWithTier(RequestObj request) {
        ResponseObj resp = initResporse(request);


        //获得语义槽
        Integer num = getNumSlot(request);
        String food = getSlot(request, UtilConstants.Slot.SLOT_FOOD_NAME);


        //获取设备型号，如果设备不是指定型号，则认为该冷柜只有全舱室，则默认添加到全舱室
        if (controllDevcieInList1(request)) {
            if (FoodUtil.addfood(getControldevice(request).getId()
                    , request.getContext().getUser().getUserId(), food, -1)) {
                setResponse(resp, "已添加" + food + "到冷柜");
            } else {
                setResponse(resp, "抱歉，由于小优无法获取到您家冷柜的设备编号或者用户编号，因此导致" + food +
                        "没有添加成功，您可以尝试重新连接一下您的冷柜");
            }
            return resp;
        }
        //如果该设备是指定型号的话，则开始判断，不同型号的冷柜可以将食物放到不同的层上
        else {
            switch (request.getContext().getControlDevices()[0].getWifiType()) {
                //BCD-332WDGRU1
                case "200861051c408504012100618006754500000000000061800351440000000040":
                    //BD-331WG
                case "200861051c408504160400718001990000000000000071800162420000000040":
                    //BD-330WEPTU1
                case "200861051c408504160400718001994400000000000071800162470000000040": {
                    int tier_max = 1007;//定义冷柜层数，共7层

                    if (!("".equals(food))) {
                        //如果添加食材不说层数，则默认添加到冷柜的全舱室
                        if (num == null) {
                            if (FoodUtil.addfood(getControldevice(request).getId()
                                    , request.getContext().getUser().getUserId(), food, -1)) {
                                setResponse(resp, "已添加" + food + "到冷柜");
                            } else {
                                setResponse(resp, "抱歉，由于小优无法获取到您家冷柜的设备编号或者用户编号" +
                                        "，因此导致" + food +
                                        "没有添加成功，您可以尝试重新连接一下您的冷柜");
                            }
                        } else {
                            //记录冷柜层数，不是冰箱的冷藏冷冻室;
                            if (num <= tier_max && num > 0) {
                                //int tier = 1000 + num;
                                if (FoodUtil.addfood(getControldevice(request).getId()
                                        , request.getContext().getUser().getUserId(), food, num)) {//添加食物
                                    setResponse(resp, "已添加" + food + "到冷柜"
                                            + CommonUtil.getKey(CommonUtil.getFridgeLocation(), num));
                                }
                            } else if (num == 0) {
                                setResponse(resp, "抱歉，您的冷柜中没有这一层");
                            }
                        }
                    } else {
                        setResponse(resp, "您没有说出想要添加到冷柜中的食材");
                    }

                }
                break;

                //BD-226WEGLU1冷柜
                case "200861051c408504160400718001994600000000000071800162480000000040": {
                    int tier_max = 1006;
                    if (!("".equals(food))) {
                        if (num == null) {
                            if (FoodUtil.addfood(getControldevice(request).getId()
                                    , request.getContext().getUser().getUserId(), food, -1)) {
                                setResponse(resp, "已添加" + food + "到冷柜");
                            } else {
                                setResponse(resp, "抱歉，由于小优无法获取到您家冷柜的设备编号或者用户编号" +
                                        "，因此导致" + food +
                                        "没有添加成功，您可以尝试重新连接一下您的冷柜");
                            }
                        } else {

                            if (num <= tier_max && num > 0) {
                                //int tier = 1000 + num;
                                if (FoodUtil.addfood(getControldevice(request).getId()
                                        , request.getContext().getUser().getUserId(), food, num)) {
                                    setResponse(resp, "已添加" + food + "到冷柜"
                                            + CommonUtil.getKey(CommonUtil.getFridgeLocation(), num));
                                }

                            } else if (num == 1007 || num == 0) {
                                setResponse(resp, "抱歉，您的冷柜中没有这一层");
                            }
                        }
                    } else {
                        setResponse(resp, "您没有说出想要添加到冷柜中的食材");
                    }
                }
                break;

                //BC/BD-103WEGKU1
                case "200861051c408504160400718002124400000000000071800192c00000000040": {
                    int tier_max = 1002;
                    if (!("".equals(food))) {
                        if (num == null) {
                            if (FoodUtil.addfood(getControldevice(request).getId()
                                    , request.getContext().getUser().getUserId(), food, -1)) {
                                setResponse(resp, "已添加" + food + "到冷柜");
                            } else {
                                setResponse(resp, "抱歉，由于小优无法获取到您家冷柜的设备编号或者用户编号" +
                                        "，因此导致" + food +
                                        "没有添加成功，您可以尝试重新连接一下您的冷柜");
                            }

                        } else {
                            if (num <= tier_max && num > 0) {
                                //int tier = 1000 + num;
                                if (FoodUtil.addfood(getControldevice(request).getId()
                                        , request.getContext().getUser().getUserId(), food, num)) {
                                    setResponse(resp, "已添加" + food + "到冷柜"
                                            + CommonUtil.getKey(CommonUtil.getFridgeLocation(), num));
                                }

                            } else if (num == 1007 || num == 1006 || num == 1005 || num == 1004 || num == 1003 || num == 0) {
                                setResponse(resp, "抱歉，您的冷柜中没有这一层");

                            }
                        }
                    } else {
                        setResponse(resp, "您没有说出想要添加到冷柜中的食物");
                    }
                }
                break;
            }
        }
        return resp;

    }


    /**
     * 取出食材
     */
    //取出食材指定层数
    public ResponseObj removeFoodWithTier(RequestObj request) {
        ResponseObj resp = initResporse(request);


        //获得语义槽
        Integer num = getNumSlot(request);
        String food = getSlot(request, UtilConstants.Slot.SLOT_FOOD_NAME);


        //获取设备相应型号如果不是上述型号，则默认食材只有全舱室，则默认从全舱室中取出食材
        if (controllDevcieInList1(request)) {
            if (FoodUtil.removefoodlist(getControldevice(request).getId(), -1, food)) {
                setResponse(resp, "冷柜已取出" + food);
            } else {
                setResponse(resp, "抱歉，您的冷柜中没有" + food);
            }
            return resp;
        }

        //设备型号为指定型号，则可以指定取出不同层数的食材
        else {

            switch (request.getContext().getControlDevices()[0].getWifiType()) {
                case "200861051c408504012100618006754500000000000061800351440000000040":
                case "200861051c408504160400718001990000000000000071800162420000000040":
                case "200861051c408504160400718001994400000000000071800162470000000040": {
                    int tier_max = 1007;//定义冷柜层数，共7层
                    if (!("".equals(food))) {
                        if (num == null) {
                            if (FoodUtil.removefoodlist(getControldevice(request).getId(), -1, food)) {
                                setResponse(resp, "冷柜已取出" + food);
                            } else {
                                setResponse(resp, "抱歉，您的冷柜中没有" + food);
                            }
                        } else {
                            if (num <= tier_max && num > 0) {

                                //取出食材
                                //int tier = 1000 + num;
                                if (FoodUtil.removefoodlist(getControldevice(request).getId(), num, food)) {
                                    setResponse(resp, "已从冷柜" + CommonUtil.getKey(CommonUtil.getFridgeLocation(), num) + "取出" + food);
                                } else {
                                    setResponse(resp, "抱歉，您的冷柜" + CommonUtil.getKey(CommonUtil.getFridgeLocation(), num) + "没有" + food);
                                }

                            } else {
                                setResponse(resp, "抱歉，您的冷柜中没有这一层");
                            }
                        }
                    } else {
                        setResponse(resp, "您没有说出想要从冷柜中取出的食材");
                    }

                }
                break;

                //BD-226WEGLU1冷柜
                case "200861051c408504160400718001994600000000000071800162480000000040": {
                    int tier_max = 1006;
                    if (!("".equals(food))) {
                        if (num == null) {
                            if (FoodUtil.removefoodlist(getControldevice(request).getId(), -1, food)) {
                                setResponse(resp, "冷柜已取出" + food);
                            } else {
                                setResponse(resp, "抱歉，您的冷柜中没有" + food);
                            }
                        } else {
                            if (num <= tier_max && num > 0) {

                                //int tier=1000+num;
                                if (FoodUtil.removefoodlist(getControldevice(request).getId(), num, food)) {
                                    setResponse(resp, "已从冷柜" + CommonUtil.getKey(CommonUtil.getFridgeLocation(), num) + "取出" + food);
                                } else {
                                    setResponse(resp, "抱歉，您的冷柜" + CommonUtil.getKey(CommonUtil.getFridgeLocation(), num) + "没有" + food);
                                }
                            } else {
                                setResponse(resp, "抱歉，您的冷柜中没有这一层");
                            }
                        }
                    } else {
                        setResponse(resp, "您没有说出想要从冷柜中取出的食材");
                    }
                }
                break;

                case "200861051c408504160400718002124400000000000071800192c00000000040": {
                    int tier_max = 1002;
                    if (!("".equals(food))) {
                        if (num == null) {
                            if (FoodUtil.removefoodlist(getControldevice(request).getId(), -1, food)) {
                                setResponse(resp, "冷柜已取出" + food);
                            } else {
                                setResponse(resp, "抱歉，您的冷柜中没有" + food);
                            }
                        } else {
                            if (num <= tier_max && num > 0) {
                                //int tier=1000+num;

                                if (FoodUtil.removefoodlist(getControldevice(request).getId(), num, food)) {
                                    setResponse(resp, "已从冷柜" + CommonUtil.getKey(CommonUtil.getFridgeLocation(), num) + "取出" + food);
                                } else {
                                    setResponse(resp, "抱歉，您的冷柜" + CommonUtil.getKey(CommonUtil.getFridgeLocation(), num) + "没有" + food);
                                }
                            } else {
                                setResponse(resp, "抱歉，您的冷柜中没有这一层");

                            }
                        }
                    } else {
                        setResponse(resp, "您没有说出想要从冷柜中取出的食材");
                    }
                }
                break;
            }

            return resp;
        }

    }




    /**
     * 查询冷柜食材存在哪几层
     */
    //查询食材
    public ResponseObj queryFood(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String food = getSlot(request, UtilConstants.Slot.SLOT_FOOD_NAME);
        String queryFood = FoodUtil.queryfood(getControldevice(request).getId(), -1, food);

        setResponse(resp, queryFood);
        return resp;
    }


    /**
     * 自定义函数
     */

    //提取设备的wifitype是否与typeid相同
    private static boolean controllDevcieInList1(RequestObj request) {
        return !(Arrays.asList(typeid).contains(request.getContext().getControlDevices()[0].getWifiType()));
    }

    //获取语音中的数字,并将其转换成整型
    private static Integer getNumSlot(RequestObj request) {
        GuessIntent intent = request.getRequest().getIntents()[0];
        Slot[] slots = intent.getSlots();
        if (slots == null || slots.length == 0) {
            return null;
        }
        for (Slot slot : slots) {
            if (UtilConstants.Slot.SLOT_FOOD_NUMBER.equals(slot.getName())) {
                if ("1".equals(slot.getValues()[0])) {
                    return 1001;
                } else if ( "2".equals(slot.getValues()[0])) {
                    return 1002;
                } else if ( "3".equals(slot.getValues()[0])) {
                    return 1003;
                } else if ( "4".equals(slot.getValues()[0])) {
                    return 1004;
                } else if ( "5".equals(slot.getValues()[0])) {
                    return 1005;
                } else if ("6".equals(slot.getValues()[0])) {
                    return 1006;
                } else if ("7".equals(slot.getValues()[0])) {
                    return 1007;
                } else if ("".equals(slot.getValues()[0])) {
                    return null;
                } else {
                    return 0;
                }
            }

        }
        return null;
    }

}
