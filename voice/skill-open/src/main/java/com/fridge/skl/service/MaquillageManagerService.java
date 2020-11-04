package com.fridge.skl.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.dto.FeedbackMapper;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.model.context.ControlDevices;
import com.fridge.skl.model.maquillage.Maquillage;
import com.fridge.skl.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.fridge.skl.util.URL_Constants.*;

@Service
@Slf4j
public class MaquillageManagerService extends BaseService {
    //支持此功能的typeid
    private static final String TYPEID_1 = "200861051c408504012100618005255700000000000061800896000000000040";
    private static final String TYPEID_2 = "200861051c408504012200618005094700000000000061800896000000000040";
    private static final String TYPEID_3 = "200861051c408504012200618005094800000000000061800896000000000040";
    private static final String TYPEID_4 = "200861051c40850401230061800525414c000000000061800896000000000040";
    private static final String[] typeid = new String[]{
            TYPEID_1, TYPEID_2, TYPEID_3, TYPEID_4
    };
    private static final String ACTION_ADD = "01";
    private static final String ACTION_DEL = "02";

    private static final Map<String, String> maqclassMapper = new ConcurrentHashMap<>();

    static {
        maqclassMapper.put("护肤品", "01");
        maqclassMapper.put("美容品", "02");
        maqclassMapper.put("特殊品", "03");
        maqclassMapper.put("保健品", "04");
    }


    @Autowired
    FeedbackMapper feedbackMapper;

    /**
     * 添加化妆品
     */
    public ResponseObj addmaq(RequestObj request) {

        ResponseObj resp = initResporse(request);
        String content;
        String userid = getUserId(request);
        String accessToken = getAccessToken(request);
        String clientid = getClientId(request);
        ControlDevices selectdevice = matchDevice(request, typeid);
        String maqclass = getSlot(request, "maqclass");
        if (selectdevice != null) {
            String datestr = DateUtils.dateToString(DateUtils.addDay(new Date(), 360));
            boolean setok =

                    setmaq(accessToken, selectdevice.getWifiType(), clientid,
                            selectdevice.getId(), userid, datestr, ACTION_ADD, maqclassMapper.get(maqclass));

            if (setok) {
                if ("护肤品".equals(maqclass)) {
                    content = "好的，已存入" + maqclass + "，您可以试试对我说：小优小优，每天晚上8点提醒我使用护肤品";
                } else {
                    content = "好的，已存入" + maqclass;
                }
            } else {
                content = "小优捣鼓了好久，还是添加失败了，要不你帮帮我";
            }
        } else {
            content = "对不起，您的家里没有支持专属空间的冰箱哦，要不要小优帮你选一台";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 删除化妆品
     */
    public ResponseObj delmaq(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content;
        String userid = getUserId(request);
        String accessToken = getAccessToken(request);
        String clientid = getClientId(request);
        ControlDevices selectdevice = matchDevice(request, typeid);
        String maqclass = getSlot(request, "maqclass");
        if (selectdevice != null) {

            content = setmaq(accessToken, selectdevice.getWifiType(), clientid,
                    selectdevice.getId(), userid, "", ACTION_DEL, maqclassMapper.get(maqclass))
                    ? maqclass + "已取出"
                    : "小优捣鼓了好久，还是删除失败了，要不你帮帮我";
        } else {
            content = "对不起，您的家里没有支持专属空间的冰箱哦，要不要小优帮你选一台";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 查询存储状态
     */
    public ResponseObj querymaq(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content;

        ControlDevices selectdevice = matchDevice(request, typeid);
        if (selectdevice != null) {
            List<Maquillage> msqs = querymaq(selectdevice.getId());
            if (!CollectionUtils.isEmpty(msqs)) {
                StringBuilder maq = new StringBuilder("冰箱专属空间里有：");
                for (Maquillage maquillage : msqs) {
                    maq.append(CommonUtil.getKey(maqclassMapper, maquillage.getGoodsId()));
                    maq.append(",");
                }
                //去结尾的，
                content = CommonUtil.cutlaststr(maq.toString(), ",");
            } else {
                content = "冰箱专属空间里什么也没有哦";
            }
        } else {
            content = "对不起，您的家里没有支持专属空间的冰箱哦，赶紧去海尔商城买一台吧";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    private final static List<String> contentlist = new ArrayList<>(Arrays.asList(
            "好的，小优已帮您打开解冻模式了",
            "明白，冰箱解冻模式已经打开了，即将开始解冻"
    ));


    private Boolean setmaq(String accessToken, String typeid, String clientId
            , String mac, String userId, String expireDate, String action, String goodsid) {
        JSONObject json = new JSONObject();
        json.put("accessToken", accessToken);
        json.put("clientId", clientId);
        json.put("appId", UtilConstants.APPID);
        json.put("appKey", UtilConstants.APPKEY);
        json.put("appVersion", "6.3.0");
        json.put("mac", mac);
        json.put("online", true);
        json.put("userId", userId);
        json.put("goodsId", goodsid);
        json.put("action", action);
        json.put("remindTime", new JSONArray());
        json.put("expireDate", expireDate);

        log.info(json.toJSONString());
        String res = HttpUtil.postJson(URL_MAQ_MANAGER + String.format(ACTION_ADD_MAQ, typeid), json.toJSONString());
        log.info("res===========>" + res);
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            return resJson.containsKey("code") && "000000".equals(resJson.getString("code"));
        }

        return false;
    }

    private List<Maquillage> querymaq(String mac) {
        JSONObject json = new JSONObject();
        json.put("mac", mac);


        log.info(json.toJSONString());
        String res = HttpUtil.postJson(URL_MAQ_MANAGER + String.format(ACTION_QUERY_MAQ, typeid), json.toJSONString());
        log.info("res===========>" + res);
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("code") && "000000".equals(resJson.getString("code"))) {
                JSONArray data = resJson.getJSONArray("data");
                return JSONArray.parseArray(data.toJSONString(), Maquillage.class);
            }
        }

        return null;
    }

}
