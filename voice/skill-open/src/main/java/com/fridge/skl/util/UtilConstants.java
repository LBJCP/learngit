package com.fridge.skl.util;


public interface UtilConstants {


    public static UnilifeAccessToken token = new UnilifeAccessToken();
    public static final String APPID = "MB-UZHSH-0000";
    public static final String APPKEY = "f50c76fbc8271d361e1f6b5973f54585";

    /**
     * 公共常量
     */
    class Public {

        //欢迎语
        public static final String WELCOME_INFO = "欢迎使用海尔技能";
        //结束语
        public static final String END_INFO = "欢迎下次再次使用";

        //用户的话无法解析
        public static final String ERROR_TYPE = "error";
        public static final String ERROR_INFO = "抱歉，不知道您说什么";

        //请求类型 start(首次唤醒)
        public static final String REQUEST_TYPE_START = "start";
        //请求类型continue(中间交互)
        public static final String REQUEST_TYPE_CONTINUE = "continue";
        //请求类型end(结束对话通知)
        public static final String REQUEST_TYPE_END = "end";
    }


    /**
     * 技能
     */
    class Skil {

        //技能名为食材管理对应的技能ID，会有多个技能ID
        public static final String FOOD_MANAGER_SKILL_ID = "2357077ae74a41308c106b9077a2a23e";
        //我要吐槽
        public static final String FEEDBERAK_SKILL_ID = "3d072604ec2441da8abb086348382ec8";
        //健康管理
        public static final String HEALTH_MANAGER_SKILL_ID = "2af997ac35f4466e9ca9baa915c1ea2a";
        //介绍下设备
        public static final String RECOMMAND_DEVICE_SKILL_ID = "86761f944e594a3c949ca9c9f751f331";
        //叫爸爸
        public static final String ASK_FATHER_SKILL_ID = "662ad6a0452643e8938d5a17d1a27af8";
        //母婴场景
        public static final String PREGNANT_MANAGER_SKILL_ID = "d48bdc5d6efa42adb9baee99eaa6c4f7";
        //食材管理2.0
        public static final String FOOD_MANAGER_2_SKILL_ID = "12f63366e23743db863678573750dfa2";
        //冰箱专用食材管理2。0
        public static final String FOOD_MANAGER_2_REF_SKILL_ID = "ba3b4f3e82514a0caf67a06c8d4cb624";
        //冰箱控制
        public static final String REF_SYSTEMCTRL_SOREF_SKILL_ID = "c9fbea1f7df54ccba7fa19003bd8f13b";
        //专属空间管理
        public static final String MAQ_MANAGER_SKILL_ID = "5b365dc6e47a45efab77a122077941ac";
        //解冻场景
        public static final String DEFREEZE_REF_SKILL_ID = "be1c19af5e314860a303f2199c3778f8";
        //杀菌场景
        public static final String STERILIZE_REF_SKILL_ID = "602557371cf94052aa34e15ea1a5fb3f";
        //杀菌场景
        public static final String STERILIZE_FOOD_REF_SKILL_ID = "1e9a6dc77174427cbb7666ea33284750";
        //记事本场景
        public static final String NOTE_SKILL_ID = "91343135ba8447ce8420d3b67679f903";
        //计算器场景
        public static final String COUNTER_SKILL_ID = "f14dedffd7dd44c689d774351f5bb373";

        //冷萃场景
        public static final String COLDEXTRACTION_SKILL_ID = "8f2cecfe3d62465482108dff55e9f18e";
        //冰箱打印控制
        public static final String PRINT_CONTROL_SKILL_ID = "b32fb2fb74054b5b8159fca80bb93f79";
        //冰箱智慧卡片
        public static final String STERILIZE_SMART_CARD_SKILL_ID = "4f196d44934d4e6bbe3498050da2013a";

        //冷柜食材管理
        public static final String FREEZER_FOOD_MANAGE_SKILL_ID="c22932914fc045c99d1a7fbc0edfd7b7";
    }

    /**
     * 任务(2020.04.23 技能平台升级，任务id会随着技能升级变动，所以废弃),技能分发采用技能id字段
     */
    @Deprecated
    class Task {
        // public static final String QUERY_HEATH_TASK_ID="10245"; 版本废弃
        public static final String QUERY_HEATH_TASK_ID = "10500";


        public static final String FOOD_MANAGER_TASK_ID = "10087";
        public static final String QUERY_HEATH_DOMAIN_TASK_ID = "10149";
        public static final String FOOD_EXPIRE_TASK_ID = "10135";
        public static final String PREGNANT_MANAGER_TASK_ID = "10343";
        public static final String SHOW_MANAGER_TASK_ID = "10369";
        public static final String EXPIRE_FOOD_MANAGER_TASK_ID = "11178";
        public static final String ASK_FATHER_TASK_ID = "10481";
        public static final String FEEDBREAK_TASK_ID = "10389";

        public static final String RECOMMENDREF_TASK_ID = "10499";

        public static final String OPERATE_APP_TASK_AME = "操作应用";
        public static final String QUERY_WEATHER_TASK_AME = "查询天气";
        public static final String FOOD_MANAGER_TASK_AME = "食材管理";
    }

    /**
     * 意图
     */
    class Intent {

        public static final String OPERATE_APP_INTENT_NAME = "操作应用";
        public static final String QUERY_WEATHER_INTENT_NAME = "查询天气";
        public static final String QUERY_EXPIRE_FOOD_INTENT_NAME = "查询过期食材";
        public static final String DEL_EXPIRE_FOOD_INTENT_NAME = "删除过期食材";
        //查询冰箱有哪些快过期食材
        public static final String QUERY_FRIDGE_FOOD_INTENT_NAME = "bxkgq";
        //查询舱室有哪些快过期食材
        public static final String QUERY_LOCATION_FOOD_INTENT_NAME = "cskgq";
        //查询食材是否过期
        public static final String QUERY_GQ_FOOD_INTENT_NAME = "scsfgq";

        //存储日期相关查询
        public static final String QUERY_CC_DATE_INTENT_NAME = "ccrq";

        //查询某食材能存多长时间
        public static final String QUERY_FOOD_TIME_INTENT_NAME = "scsj";

        //语音查询自身身体体重
        public static final String QUERY_HEL_WEIGHT_INTENT_NAME = "yysttz";

        //语音查询自身身体评分
        public static final String QUERY_HEL_SCORE_INTENT_NAME = "yystpf";

        //添加家庭成员
        public static final String QUERY_HEL_ADD_INTENT_NAME = "tjjtcy";


        //语音查询该怎么吃
        public static final String QUERY_HEL_HOWEAT_INTENT_NAME = "cxgzmc";


        //语音查看运动情况
        public static final String QUERY_HEL_SPORT_INTENT_NAME = "ckydqk";

        //语音查询年龄
        public static final String QUERY_HEL_AGE_INTENT_NAME = "cxnl";

        //语音查询健康状况
        public static final String QUERY_HEL_ALL_INTENT_NAME = "查询健康状况";
        //语音查询饮食计划
        public static final String QUERY_HEL_DIETPLAN_INTENT_NAME = "查询饮食计划";
        //语音查询饮食计划
        public static final String QUERY_HEL_INDEX_INTENT_NAME = "查询身体状况";
        //查询步数
        public static final String QUERY_HEL_STEP_INTENT_NAME = "查询步数";

        //开始备孕
        public static final String INSERT_PRE_DATE_INTENT_NAME = "开始备孕";
        //开始备孕
        public static final String QUERY_PRE_DATE_INTENT_NAME = "查询备孕";
        //开始备孕
        public static final String DELETE_PRE_DATE_INTENT_NAME = "取消备孕";


        //开始怀孕
        public static final String INSERT_PRED_DATE_INTENT_NAME = "开始怀孕";
        //开始怀孕
        public static final String QUERY_PRED_DATE_INTENT_NAME = "查询怀孕";
        //开始怀孕
        public static final String DELETE_PRED_DATE_INTENT_NAME = "取消怀孕";

        //惠灵顿牛排搭配什么酒一起吃
        public static final String SEARCH_WELLINGTON_INTENT_NAME = "惠灵顿牛排";
        //惠灵顿牛排搭配什么酒一起吃
        public static final String SEARCH_RECOMMENDEDWINE_INTENT_NAME = "推荐红酒";


        //食材管理2.0
        public static final String QUERY_SAVETIME_INTENT_NAME = "保存时长查询";
        public static final String QUERY_SAVETIME_COLD_INTENT_NAME = "冷藏时间查询";
        public static final String QUERY_SAVETIME_FREEZING_INTENT_NAME = "冷冻时间查询";
        public static final String QUERY_FOOD_FITUSER_INTENT_NAME = "食材适合人群";
        public static final String QUERY_FOOD_AVOIDUSER_INTENT_NAME = "禁止食用人群";
        public static final String QUERY_TOGETHERFOOD_INTENT_NAME = "适合搭配的食材";
        public static final String QUERY_RELATIVEFOOD_INTENT_NAME = "搭配禁忌";
        public static final String QUERY_TOWFOODFIT_INTENT_NAME = "两种食材适合搭配吗";
        public static final String QUERY_FOODSAVEDTIME_INTENT_NAME = "某食材存了几天了";
        public static final String QUERY_ISFOODEXPIRE_INTENT_NAME = "某食材是否过期";
        public static final String QUERY_QUERYFOODSAVEINDATE_INTENT_NAME = "查询某食材存储日期";
        public static final String QUERY_QUERYFOODCATEGORY_INTENT_NAME = "食材分类查询";
        public static final String QUERY_WAYTOSAVEFOOD_INTENT_NAME = "查询食材怎么存储";
        public static final String QUERY_FOOD_LOCATION_INTENT_NAME = "食材存在哪";
        public static final String QUERY_FOOD_EXPIRETIME_INTENT_NAME = "查询什么时候过期";
        public static final String QUERY_COLLOCATION_FOOD_INTENT_NAME = "配菜";

        public static final String ASK_FATHER_INTENT_NAME = "叫爸爸";
        public static final String RECOM_REF_INTENT_NAME = "冰箱介绍";
        public static final String RECOM_SUP_REF_INTENT_NAME = "冰箱特色介绍";

        public static final String DO_DEFREEZE_INTENT_NAME = "开始解冻";
        public static final String DO_RESERVE_DEFREEZE_INTENT_NAME = "预约解冻";
        public static final String DO_RESERVE_CLEAR_DEFREEZE_INTENT_NAME = "取消解冻";

        public static final String DO_OPENREFREENDOOR_INTENT_NAME = "冰箱冷冻室开门";

        //专属空间管理
        public static final String ADD_MAQ_INTENT_NAME = "添加化妆品";
        public static final String DEL_MAQ_INTENT_NAME = "删除化妆品";
        public static final String QUERY_MAQ_INTENT_NAME = "查询化妆品";

        // 杀菌查询
        public static final String QUERY_MTER_INTENT_NAME = "冰箱杀菌进度";
        public static final String COUNT_MTER_INTENT_NAME = "冰箱杀菌次数查询";
        public static final String DEMO_ADDMAST_INTENT_NAME = "添加松茸";
        public static final String DEMO_STARTMAST_INTENT_NAME = "开启立即杀菌";

        // 记事本
        public static final String NOTESOMETHING_INTENT_NAME = "帮我记一下";
        public static final String NOTEME_INTENT_NAME = "我刚才记了什么";
        public static final String CLEAR_NOTE_INTENT_NAME = "清空语音留言板";
        public static final String DEL_LASTNOTE_INTENT_NAME = "删除上一条留言";
        public static final String LAST_NOTE_INTENT_NAME = "上一条记录";
        public static final String QUER_SHOPLIST_INTENT_NAME = "查询购物清单";
        public static final String ADD_SHOPLIST_INTENT_NAME = "添加购物清单";
        public static final String DEL_SHOPLIST_INTENT_NAME = "删除购物清单";


        // 冷萃场景
        public static final String START_COLD_INTENT_NAME = "开始冷萃";
        public static final String STOP_COLD_INTENT_NAME = "结束冷萃";
        public static final String QUERY_COLD_INTENT_NAME = "冷萃结束查询";


        // 加减法
        public static final String ADD_COUNTER_INTENT_NAME = "加法";

        //打印食材标签
        public static final String PRINT_FOOD_INTENT_NAME = "打印食材标签";

        //打开智慧模式卡片
        public static final String OPEN_SMARTCARD_INTENT_NAME = "打开智慧模式卡片";

        //添加食材到冷柜指定层数
        public static final String ADD_FOOD_INTENT_NAME_WITH_TIER="添加食材";
        //从冷柜指定层数取出食材
        public static final String REMOVE_FOOD_INTENT_NAME_WITH_TIER="取出食材";
        //语音查询储存层
        public static final String QUERY_FOOD_INTENT_NAME="语音查询储存层";



    }


    /**
     * 语义槽
     */
    class Slot {

        public static final String SLOT_LOCATION_NAME = "location";
        public static final String SLOT_FOOD_NAME = "food";

        public static final String SLOT_DAYS_NAME = "days";

        public static final String SLOT_MONTHS_NAME = "months";

        public static final String SLOT_CS_NAME = "flocation";

        public static final String SLOT_SC_NAME = "scfood";

        public static final String SLOT_CC_NAME = "ccfood";
        public static final String SLOT_SJ_NAME = "sjfood";

        public static final String SLOT_JT_NAME = "frperson";

        public static final String SLOT_JTCY_NAME = "jtcy";

        public static final String SLOT_FAN_NAME = "efood";

        public static final String SLOT_TIMEBEF_NAME = "time";
        public static final String SLOT_FOODLIST_NAME = "food";
        public static final String SLOT_FOODCATEGORY_NAME = "categoryfood";
        public static final String SLOT_NAME_NAME = "name";
        public static final String SLOT_REFMODE_NAME = "mode";
        public static final String SLOT_USER_NAME = "user";

        public static final String SLOT_INDEX_NAME = "index";

        public static final String SLOT_PERSION_NAME = "persion";
        public static final String SLOT_PERSION_NAME_ME = "我";

        public static final String SLOT_FOOD_NUMBER="num";
    }


}
