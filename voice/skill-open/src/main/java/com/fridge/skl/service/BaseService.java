package com.fridge.skl.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.model.*;
import com.fridge.skl.model.context.ControlDevices;
import com.fridge.skl.model.exception.IntentException;
import com.fridge.skl.model.response.Command;
import com.fridge.skl.model.response.Speech;
import com.fridge.skl.model.response.SpeechContent;
import com.fridge.skl.model.slots.Time;
import com.fridge.skl.util.Base64Util;
import com.fridge.skl.util.UtilConstants;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseService {
    /**
     * 初始化接口返回
     * @param request
     */
    protected ResponseObj initResporse(RequestObj request) {

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

        String content = "好吧，小优的神经网络又捕获到一个不知道怎么回答的问题呢，我很快会变聪明的";
        response.setCommand(new Command());
        speech.setContent(content);
        response.setSpeech(speech);
        response.setShouldEndSession(false);
        resp.setResponse(response);

        return resp;
    }

    protected static String getRandomAnswer(List<String> strs, Object... args) {
        Random random = new Random();
        if(random.nextInt(strs.size())==0){
           return null;
        }
           return String.format(strs.get(random.nextInt(strs.size())), args);

    }

    protected static String getSlot(Slot[] slots, String slotname) {
        if (slots == null || slots.length == 0) {
            return null;
        }
        for (Slot slot : slots) {
            if (slotname.equals(slot.getName())) {
                //TODO 日后据说会用到数据其他值，但是暂时没有
                return slot.getValues()[0];
            }
        }


        return null;
    }

    protected static String getOriginalSlot(Slot[] slots, String slotname) {
        if (slots == null || slots.length == 0) {
            return null;
        }
        for (Slot slot : slots) {
            if (slotname.equals(slot.getName())) {
                //TODO 日后据说会用到数据其他值，但是暂时没有
                return slot.getOriginalValues()[0];
            }
        }


        return null;
    }


    protected static String getOriginalSlot(RequestObj request, String slotname) {
        GuessIntent intent = request.getRequest().getIntents()[0];
        Slot[] slots = intent.getSlots();
        if (slots == null || slots.length == 0) {
            return null;
        }
        if (!StringUtils.isEmpty(slotname)) {

            for (Slot slot : slots) {
                if (slotname.equals(slot.getName())) {
                    return slot.getOriginalValues()[0];
                }
            }
        } else {
            return slots[0].getOriginalValues()[0];
        }
        return null;
    }

    protected static GuessIntent getIntent(RequestObj request) throws IntentException {
        GuessIntent[] intents = request.getRequest().getIntents();
        if (intents != null && intents.length > 0) {
            return intents[0];
        } else {
            throw new IntentException();
        }
    }

    protected static String getSlot(RequestObj request, String slotname) {
        //getSlot()方法用来提取用户的语义槽
        GuessIntent intent = request.getRequest().getIntents()[0];//取用户的第一个意图
        Slot[] slots = intent.getSlots();//获取用户的所有语义，
        // 包括name(food、num)、
        // values("牛肉"、“1”)、
        // originalValues("牛肉"、“1”)
        if (slots == null || slots.length == 0) {
            return null;
        }
        if (!StringUtils.isEmpty(slotname)) {

            //for (Slot slot : slots)的意思就是说，遍历slots数组，每次遍历的对象用slot这个对象去接收。
            for (Slot slot : slots) {
                if (slotname.equals(slot.getName())) {
                    return slot.getValues()[0];
                }
            }
        } else {
            return slots[0].getValues()[0];
        }
        return null;
    }

//   /*
//    * 添加XX食材到第X层
//    * */
//    protected static String getAddFoodSlot(RequestObj request, String[] slotname) {
//        //getAddFoodSlot()方法用来提取用户的语义槽
//        GuessIntent intent = request.getRequest().getIntents()[0];//取用户的第一个意图
//        Slot[] slots = intent.getSlots();//获取用户的所有语义，
//        // 包括name(food、num)、
//        // values("牛肉"、“1”)、
//        // originalValues("牛肉"、“1”)
//        if (slots == null || slots.length == 0) {
//            return null;
//        }
//        if (!StringUtils.isEmpty(slotname[0])) {
//
//            //for (Slot slot : slots)的意思就是说，遍历slots数组，每次遍历的对象用slot这个对象去接收。
//            for (Slot slot : slots) {
//                if (slotname[0].equals(slot.getName())) {
//                    return slot.getValues()[0];
//                }
//            }
//        } else {
//            return slots[0].getValues()[0];
//        }
//        return null;
//    }


    /**
     * 获取中文数字slot
     */
    protected static String getChineseNumFomeSlot(RequestObj request) {
        String slotname = "chinesenum";
        GuessIntent intent = request.getRequest().getIntents()[0];
        Slot[] slots = intent.getSlots();
        if (slots == null || slots.length == 0) {
            return null;
        }

        for (Slot slot : slots) {
            if (slotname.equals(slot.getName())) {
                if ("两".equals(slot.getValues()[0])) {
                    return "2";
                } else if ("仨".equals(slot.getValues()[0])) {
                    return "3";
                }
                return null;
            }
        }

        return null;

    }


    protected static boolean hasSlot(RequestObj request, String slotname) {
        GuessIntent intent = request.getRequest().getIntents()[0];
        Slot[] slots = intent.getSlots();
        if (slots == null || slots.length == 0) {
            return false;
        }
        if (!StringUtils.isEmpty(slotname)) {

            for (Slot slot : slots) {
                if (slotname.equals(slot.getName())) {
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }


    protected static List<DeviceRoomInfos> getDevice(RequestObj request) {
        String slotname = "device";
        List<DeviceRoomInfos> deviceRoomInfos = new ArrayList<>();
        String decode = "";
        GuessIntent intent = request.getRequest().getIntents()[0];
        Slot[] slots = intent.getSlots();
        if (slots == null || slots.length == 0) {
            return null;
        }

        for (Slot slot : slots) {
            if (slotname.equals(slot.getName())) {
                decode = slot.getValues()[0];
                JSONObject deviceinfos = JSONObject.parseObject(Base64Util.decodeStr(decode));
                JSONObject data = deviceinfos.getJSONObject("data");
                deviceRoomInfos = JSONObject.parseArray(data.getString("deviceRoomInfos"), DeviceRoomInfos.class);
                return deviceRoomInfos;

            }
        }

        return null;
    }

    protected Date getSlotTime(RequestObj request) {
        String slotname = "mytime";
        String timestr = "";
        Time time;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        GuessIntent intent = request.getRequest().getIntents()[0];
        Slot[] slots = intent.getSlots();
        if (slots == null || slots.length == 0) {
            return null;
        }
        for (Slot slot : slots) {
            if (slotname.equals(slot.getName())) {
                timestr = slot.getValues()[0];
                time = JSON.parseObject(timestr, Time.class);
                if (time.getYear() != null) {
                    calendar.set(Calendar.YEAR, time.getYear());
                }
                if (time.getMonth() != null) {
                    calendar.set(Calendar.MONTH, time.getMonth() - 1);
                }
                if (time.getDate() != null) {
                    calendar.set(Calendar.DATE, time.getDate());
                }
                if (time.getHour() != null) {
                    calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
                }
                if (time.getMinute() != null) {
                    calendar.set(Calendar.MINUTE, time.getMinute());
                }
                if (time.getTimemark() != null) {
                    int flg;
                    if (time.getTimemark().equals("after")) {
                        flg = 1;
                    } else if (time.getTimemark().equals("before")) {
                        flg = -1;
                    }
                }
                return calendar.getTime();

            }
        }


        return null;
    }

    /**
     * 获取 slot实际描述的时间
     *
     * @param request 请求
     * @return 时间的文字表述
     */
    protected String getoriginalTime(RequestObj request) {
        String slotname = "mytime";
        String timestr = "";
        Time time;
        GuessIntent intent = request.getRequest().getIntents()[0];
        Slot[] slots = intent.getSlots();
        if (slots == null || slots.length == 0) {
            return null;
        }
        for (Slot slot : slots) {
            if (slotname.equals(slot.getName())) {
                return slot.getOriginalValues()[0];
            }
        }
        return "";
    }

    /**
     * 获取sn
     *
     * @param request 请求
     * @return sn
     */

    protected String getSn(RequestObj request) {
        return request.getRequest().getRequestId();
    }

    /**
     * 判断当前选择设备是否支持该功能
     *
     * @param request    请求
     * @param typeidlist 支持当前功能的typeid列表
     * @return 是否支持
     */
    protected boolean controllDevcieInList(RequestObj request, String[] typeidlist) {
        return Arrays.asList(typeidlist).stream().anyMatch(typeid -> getControldevice(request).getWifiType().equals(typeid));


    }


    protected Date getSlotTimeHour(RequestObj request) {
        String slotname = "mytime";
        String timestr = "";
        Time time;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        GuessIntent intent = request.getRequest().getIntents()[0];
        Slot[] slots = intent.getSlots();
        if (slots == null || slots.length == 0) {
            return null;
        }
        for (Slot slot : slots) {
            if (slotname.equals(slot.getName())) {
                timestr = slot.getValues()[0];
                time = JSON.parseObject(timestr, Time.class);
                if (time.getHour() == null) {
                    return null;
                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
                }

                if (time.getYear() != null) {
                    calendar.set(Calendar.YEAR, time.getYear());
                }
                if (time.getMonth() != null) {
                    calendar.set(Calendar.MONTH, time.getMonth() - 1);
                }
                if (time.getDate() != null) {
                    calendar.set(Calendar.DATE, time.getDate());
                }
                if (time.getIntervalDay() != null) {
                    calendar.add(Calendar.DATE, time.getIntervalDay());
                }
                if (time.getNextMonth() != null) {
                    calendar.add(Calendar.MONTH, time.getNextMonth());
                }
                if (time.getMinute() != null) {
                    calendar.set(Calendar.MINUTE, time.getMinute());
                }

                return calendar.getTime();

            }
        }


        return null;
    }


    protected String findword(String word, List<String> list) {
        for (String key : list) {
            if (word.contains(key)) {
                return key;
            }
        }
        return null;
    }

    protected String findtwoword(String word, String key1, List<String> list) {
        word = word.replace(key1, "");
        for (String key : list) {
            if (word.contains(key)) {
                return key;
            }
        }
        return null;
    }


    protected String findword(String word, ConcurrentHashMap<String, String> alimap) {
        for (Map.Entry<String, String> entry : alimap.entrySet()) {

            if (word.contains(entry.getKey())) {
                return entry.getKey();
            }
            String[] als = entry.getValue().split(",");
            for (String al : als) {
                if (word.contains(al)) {
                    return entry.getKey();
                }
            }

        }

        return null;
    }

    public static String move2toliang(int obj) {
        if (obj == 2) {
            return "两";
        } else {
            return String.valueOf(obj);
        }
    }

    public static String move2toliang(long obj) {
        if (obj == 2) {
            return "两";
        } else {
            return String.valueOf(obj);
        }
    }

    public static String move2toliang(String obj) {
        if ("2".equals(obj)) {
            return "两";
        } else {
            return obj;
        }
    }

    protected static String getWifiType(RequestObj request) {
        return request.getContext().getControlDevices()[0].getWifiType();
    }

    protected static ControlDevices getControldevice(RequestObj request) {
        return request.getContext().getControlDevices()[0];
    }

    protected static ControlDevices matchDevice(RequestObj request, String[] typids) {
        ControlDevices selectdevice = request.getContext().getControlDevices()[0];
        for (String typeid : typids) {
            if (selectdevice.getWifiType().equals(typeid)) {
                return selectdevice;
            }
        }
        return null;
    }

    protected static String getUserId(RequestObj request) {
        return request.getContext().getUser().getUserId();
    }

    protected static String getDeviceId(RequestObj request) {
        return request.getContext().getDevice().getDeviceId();
    }

    protected static String getAccessToken(RequestObj request) {
        return request.getContext().getDevice().getAccessToken();
    }

    protected static String getClientId(RequestObj request) {
        return request.getContext().getDevice().getClientId();
    }

    protected static boolean inTypeidList(String[] typeids, String typeid) {
        if (typeids != null && typeids.length != 0) {
            for (String type : typeids) {
                if (type.equals(typeid)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected static void setResponse(ResponseObj responseObj, String content) {
        responseObj.getResponse().getSpeech().setContent(content);
    }

}
