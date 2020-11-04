package com.fridge.skl.util;

public interface URL_Constants {
    //host
    public static final String URL_HEALTH = "http://line.xcook.cn:8888/CookbookResourcePlatform-api/healthy/";
    public static final String URL_HEALTH_TEST = "http://enxcook.xcook.cn:8888/CookbookResourcePlatform-api/healthy/";
    public static final String URL_HEALTH_USERCENTER = "https://account-api.haier.net/";
    public static final String URL_DOT_YANZHENG = "http://ai.haier.net:11000/yanzheng/ai-access/";
    public static final String URL_UNFREEZE = "https://space.linkcook.cn/";
    public static final String URL_OPENDOOR = "https://enxcook.linkcook.cn/";
    public static final String URL_MAQ_MANAGER = "https://rtos.linkcook.cn/";
    public static final String URL_STERILIZE_MANAGER = "https://line.linkcook.cn/extend-web/";
    public static final String URL_REFRIGERATOR_SCENE = "https://line.linkcook.cn/refrigerator-scene/";


    //优家音箱开放接口
    // public static final String URL_DOT_ACCESS = "http://ai.haier.net:11000/access/ai-access/"; 切换为https
    public static final String URL_DOT_ACCESS = "https://aiservice.haier.net/access/ai-access/";

    public static final String URL_IOT_ACCESS = "https://uws.haier.net";
    public static final String URL_UNILIFEMEDIA = "http://api.unilifemedia.com/service/v1/";
    public static final String URL_UNILIFE = "http://api.unilifemedia.com/service/v1/";
    public static final String URL_UNILIFE_MOBILE = "http://mobile.unilifemedia.com/service/v1/";
    public static final String URL_RECIPE = "https://sl.linkcook.cn";

    public static final String URL_COFFIC="https://line.linkcook.cn/refrigerator-scene";
    public static final String URL_COFFIC_TEST="https://line.linkcook.cn/refrigerator-scene";
    //public static final String URL_COFFIC_TEST="https://enxcook.xcook.cn/refrigerator-scene";
    //消息推送服务
    public static final String URL_PUSH_MESSAGE="https://www.haigeek.com";
    public static final String ACTION_GETFOODLIST = "food/getList?access_token=" + HttpUtil.getToken();
    public static final String ACTION_ADDFOOD = "food/add?access_token=" + HttpUtil.getToken();
    public static final String ACTION_SEARCHRECIPEBYINGR = "douguo/searchRecipeByIngr" + HttpUtil.getToken();
    public static final String ACTION_GETHEALTHYINFO = "getHealthyInfo";
    public static final String ACTION_GETDIETPLAN = "getDietPlan";
    public static final String ACTION_GETHISTORYINFO = "getHistoryInfoForUHomeApp";
    public static final String ACTION_GETSTEP = "getStep";
    public static final String ACTION_SAVEUSERINFO = "saveUserInfo";
    public static final String ACTION_GETUSERINFO_HEARTH = "getHistoryInfoForUHomeApp";
    public static final String ACTION_GETNEWPUBLICFOODLIST = "food/getNewPublicFoodList";
    public static final String ACTION_GETNEWPUBLICFOODCATALOGLIST = "food/getNewPublicFoodCatalogList";
    public static final String ACTION_BATCHFRIDGEFOODS ="batchFridgeFoods?access_token=" + HttpUtil.getToken();



    public static final String ACTION_GETUSERINFO = "v2/haier/userinfo";
    public static final String ACTION_FICTITIOUS = "/haier/v2/fictitious";

    public static final String ACTION_GETUSERCENTERID = "oauth/channel/getUserId";
    public static final String ACTION_DEVICEINFOS = "/uds/v1/protected/deviceinfos";
    public static final String ACTION_LASTREPORTSTATUS = "/uds/v1/protected/%s/lastReportStatus";
    //用户设备操作-异步接口-标准模型
    public static final String ACTION_OPERATE = "/stdudse/v1/modfier/operate";
    public static final String ACTION_GETBASEINFO = "/stdudse/v1/protected/getBaseInfo";


    public static final String ACTION_RECPIR_GETACCESSTOKEN = "/recipeApi/v1/out/auth/getAccessToken";
    public static final String ACTION_RECPIR_UNIONRECIPEFUZZYSEARCH = "/recipeApi/v1/out/unionRecipe/unionRecipeFuzzySearch";
    public static final String ACTION_RECPIR_FINDRECIPE = "/recipeApi/v1/out/recipe/findRecipeByPage";
    public static final String ACTION_RECPIR_FINDRECIPE_V2 = "/recipeApi/v2/out/unionRecipe/findUnionRecipeByPage";

    public static final String ACTION_RECPIR_UNIONRECIPEFUZZYSEARCH_V2 = "/recipeApi/v2/out/unionRecipe/unionRecipeFuzzySearch";

    public static final String ACTION_RECPIR_FINDRECIPEBYTAG = "/recipeApi/v1/out/recipe/findRecipeByTag";

    //解冻相关接口
    public static final String ACTION_VOICEUNFREEZING = "steak/voiceUnfreezing?typeId=%s";
    public static final String ACTION_SETAPPOINTMENTUNFREEZING = "steak/setAppointmentUnfreezing?typeId=%s";
    public static final String ACTION_STOPUNFREEZING = "steak/stopUnfreezing?typeId=%s";

    //冰箱开门
    public static final String ACTION_OPENDOOR = "extend-web/fridge/openDoor";

    //冰箱查询专属空间
    public static final String ACTION_QUERY_MAQ = "rtos/space/query?typeId=%s";
    //冰箱专属空间存储
    public static final String ACTION_ADD_MAQ = "rtos/space/save?typeId=%s";

    //冰箱杀菌进度查询
    public static final String ACTION_QUERYSTERILIZE_MAQ = "sterilization/getSterilization";
    //冰箱杀菌粗疏
    public static final String ACTION_COUNTSTER_MAQ = "sterilization/daySterilizationReport";

    //冰箱一键杀菌
    public static final String ACTION_ONESHOTSTER_MAQ = "sterilization/manual";


    //一键冷萃咖啡
    public static final String ACTION_ONESHOTCOLD = "/app/v1/leader/coffeeExtract/setCoffeeExtractMode";
    //结束冷萃咖啡
    public static final String ACTION_STOPCOLD = "/app/v1/leader/coffeeExtract/terminateCoffeeExtractMode";
    //查询冷萃咖啡
    public static final String ACTION_QUERYCOLD = "/app/v1/leader/coffeeExtract/getCoffeeExtractDetail";
    //饮用完成
    public static final String ACTION_FINISH_DRINK = "/app/v1/leader/coffeeExtract/completeDrink";

    public static final String ACTION_PUSHMESSAGE = "/developerskill/pushMsg";
    //4.1.1	查询型号下冰箱场景卡片列表接口
    public static final String ACTION_QUERYMODELCARDLIST = "/app/v1/fridge/sceneCard/queryModelCardList";
    //	设置冰箱场景卡片状态接口
    public static final String ACTION_SCENECARD_SET = "/app/v1/fridge/sceneCard/set";




}
