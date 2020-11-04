package com.fridge.skl.util;

import com.alibaba.fastjson.JSONArray;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * @Description TODO
 * @Date 2019/7/5 14:44
 * @Created by liupeng
 */
public class CommonUtil {
    //readis 存储读取参数

    public static final String IOTREADISFLG = "IOTSN";
    public static final String REQUEST = "request";
    public static final String PARAM = "param";
    public static final String PARAM1 = "param1";
    public static final String PARAM2 = "param2";


    private static Map<String, JSONArray> FOOD_JSON_SOURCE = new ConcurrentHashMap<String, JSONArray>();

    public static Map<String, JSONArray> getFoodJsonSource() {
        return FOOD_JSON_SOURCE;
    }

    public static void setFoodJsonSource(Map<String, JSONArray> foodJsonSource) {
        FOOD_JSON_SOURCE = foodJsonSource;
    }

    private final static Map<String, Integer> fridgeLocation;

    public static Map<String, Integer> getFridgeLocation() {
        return fridgeLocation;
    }

    static {
        fridgeLocation = new ConcurrentHashMap<>();
        fridgeLocation.put("冰箱", -1);
        fridgeLocation.put("冷藏室", 1);
        fridgeLocation.put("冷冻室", 2);
        fridgeLocation.put("冷冻室二", 3);
        fridgeLocation.put("变温室", 4);
        fridgeLocation.put("左变温室", 5);
        fridgeLocation.put("右变温室", 6);
        fridgeLocation.put("上变温室", 7);
        fridgeLocation.put("下变温室", 8);
        fridgeLocation.put("007多功能变温室", 9);
        fridgeLocation.put("小变温室", 10);
        fridgeLocation.put("大变温室", 11);
        fridgeLocation.put("上冷冻室", 13);
        fridgeLocation.put("下冷冻室", 14);
        fridgeLocation.put("左冷冻室", 15);
        fridgeLocation.put("右冷冻室", 16);
        fridgeLocation.put("保湿室", 17);
        fridgeLocation.put("冷冻变温室", 18);
        fridgeLocation.put("冷藏变温室", 19);
        fridgeLocation.put("第一层", 1001);
        fridgeLocation.put("第二层", 1002);
        fridgeLocation.put("第三层", 1003);
        fridgeLocation.put("第四层", 1004);
        fridgeLocation.put("第五层", 1005);
        fridgeLocation.put("第六层", 1006);
        fridgeLocation.put("第七层", 1007);


    }

    public static Integer getNum(String name) {
        Integer num = fridgeLocation.get(name);
        if (num == null) {
            num = -1;
        }

        return num;
    }

    public static String list2String(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    private final static ConcurrentHashMap<String, String> bodyindex = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<String, String> bodyunit = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<String, Integer> mealstime = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> getBodyindex() {
        return bodyindex;
    }

    public static ConcurrentHashMap<String, String> getBodyunit() {
        return bodyunit;
    }

    public static ConcurrentHashMap<String, Integer> getMealstime() {
        return mealstime;
    }

    static {
        bodyindex.put("身高", "height");
        bodyindex.put("体重", "weight");
        bodyindex.put("体脂率", "fat");
        bodyindex.put("身体质量指数", "BMI");
        bodyindex.put("燃脂心率", "heartrhythm");
        bodyindex.put("标准体重范围", "weight_range");
        bodyindex.put("预算热量", "heat");
        bodyindex.put("健康评级", "BMI_Grade");

        bodyunit.put("身高", "米");
        bodyunit.put("体重", "千克");
        bodyunit.put("体脂率", "%");
        bodyunit.put("身体质量指数", "");
        bodyunit.put("燃脂心率", "次每分钟");
        bodyunit.put("标准体重范围", "千克");
        bodyunit.put("预算热量", "千卡");
        bodyunit.put("健康评级", "级");


        mealstime.put("今天", 1);

        mealstime.put("早上", 2);
        mealstime.put("早饭", 2);
        mealstime.put("早餐", 2);
        mealstime.put("今早上", 2);

        mealstime.put("中午", 3);
        mealstime.put("今中午", 3);
        mealstime.put("午饭", 3);
        mealstime.put("午餐", 3);
        mealstime.put("中午饭", 3);

        mealstime.put("晚上", 4);
        mealstime.put("今晚上", 4);
        mealstime.put("晚饭", 4);
        mealstime.put("晚上饭", 4);
        mealstime.put("晚餐", 4);

    }


    /*public static void main(String[] args) {
        ClassPathResource classPathResource = new ClassPathResource("food.json");
        StringBuilder sb = new StringBuilder();

        try {
            InputStream inputStream = classPathResource.getInputStream();


            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bf = new BufferedReader(inputReader);

            String str;
            while ((str = bf.readLine()) != null) {
                sb.append(str);
            }
            bf.close();
            inputReader.close();


        } catch (Exception e) {

            e.printStackTrace();
        }


        JSONObject json = JSONObject.parseObject(sb.toString());
        JSONArray arr = json.getJSONArray("data");
        StringBuilder sb1 = new StringBuilder();
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < arr.size(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            String s = obj.getString("alias");
            sb1.append(s.replace(",", "\n").replace("、", "\n"));
            sb1.append("\n");
            sb1.append(obj.getString("name"));
            sb1.append("\n");
            list.add(obj.getString("name"));
            if (s.contains(",")) {
                String[] ll = s.split(",");
                list.addAll(Arrays.asList(ll));
            } else if (s.contains("、")) {
                String[] ll = s.split("、");
                list.addAll(Arrays.asList(ll));
            } else if (s.contains("，")) {
                String[] ll = s.split("，");
                list.addAll(Arrays.asList(ll));
            } else if (!StringUtils.isEmpty(s)) {
                if (isChines(s)) {
                    list.add(s);
                }
            }

        }
        ArrayList<String> list1 = getList(list);
        for (String str : list1) {
            if (isChines(str)) {
                System.out.println(str);
            }
        }
//        System.out.println(sb1.toString());


    }
*/
    public static ArrayList getList(List arr) {
        List list = new ArrayList();
        Iterator it = arr.iterator();
        while (it.hasNext()) {
            Object obj = (Object) it.next();
            if (!list.contains(obj)) {                //不包含就添加
                list.add(obj);
            }
        }
        return (ArrayList) list;
    }

    public static boolean isChines(String str) {
        return str.matches("[\u4E00-\u9FA5]+");
    }


    public static int getALBNum(String replaceNumber) {
        switch (replaceNumber) {
            case "一":
                return 1;
            case "二":
                return 2;
            case "三":
                return 3;
            case "四":
                return 4;
            case "五":
                return 5;
            case "六":
                return 6;
            case "七":
                return 7;
            case "八":
                return 8;
            case "九":
                return 9;
            case "十":
                return 10;
            case "零":
                return 0;
            default:
                return 0;
        }
    }


    public static int getALBNumFast(String num) {

        if (num.length() == 1) {
            return getALBNum(num);
        } else if (num.length() == 2) {
            if (num.startsWith("十")) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < num.length(); i++) {
                    sb.append(getALBNum(String.valueOf(num.charAt(i))));
                }
                return Integer.parseInt("1" + sb.toString());
            } else {
                if (num.charAt(num.length() - 1) == '十') {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < num.length() - 1; i++) {
                        sb.append(getALBNum(String.valueOf(num.charAt(i))));

                    }
                    return Integer.parseInt(sb.toString() + "0");
                }
            }
        } else if (num.length() == 3) {
            return Integer.parseInt(getALBNum(String.valueOf(num.charAt(0))) + "" + getALBNum(String.valueOf(num.charAt(2))));
        }


        return 0;

    }

    public static String getRondomStringFromList(List<String> list) {
        Random random = new Random();
        int n = random.nextInt(list.size());
        return list.get(n);
    }

    public static String cutlaststr(String str, String cutstr) {
        if (!StringUtils.isEmpty(str) && str.endsWith(cutstr)) {
            return str.substring(0, str.length() - 1);
        }

        return str;
    }

    public static String cutlaststr(StringBuilder sb, String cutstr) {
        String str = sb.toString();
        if (!StringUtils.isEmpty(str) && str.endsWith(cutstr)) {
            return str.substring(0, str.length() - 1);
        }

        return str;
    }

    public static String gernateRanNum() {
        return String.valueOf(new Date().getTime());
    }

    public static <K, V> Set<K> getKeysByStream(Map<K, V> map, V value) {

        return map.entrySet()
                .stream()
                .filter(kvEntry -> Objects.equals(kvEntry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

    }

    public static String getMapKey(Map<String, Object> map, Object value) {
        String key = "";
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                key = entry.getKey();
            }
        }
        return key;
    }

    public static String getLocationByid(Integer id) {
        String key = "";
        for (Map.Entry<String, Integer> entry : fridgeLocation.entrySet()) {
            if (id.equals(entry.getValue())) {
                key = entry.getKey();
            }
        }
        return key;
    }

    public static String getLocationByid(String id) {
        String key = "";
        for (Map.Entry<String, Integer> entry : fridgeLocation.entrySet()) {
            if (id.equals(String.valueOf(entry.getValue()))) {
                key = entry.getKey();
            }
        }
        return key;
    }

    /**
     * 不重复valel取key，适合一对一关系
     */
    public static Object getKey(Map<String, String> map, String v) {

        String key = "";

        for (Map.Entry<String, String> m : map.entrySet()) {

            if (m.getValue().equals(v)) {

                key = m.getKey();

            }
        }

        return key;

    }

    /**
     * 不重复valel取key，适合一对一关系
     */
    public static Object getKey(Map<String, Integer> map, Integer v) {

        String key = "";

        for (Map.Entry<String, Integer> m : map.entrySet()) {

            if (m.getValue().equals(v)) {

                key = m.getKey();

            }
        }

        return key;

    }

    /**
     * 数组中是否存在某元素
     */
    public static boolean containsvalue(String[] objs, String value) {

        return Arrays.asList(objs).contains(value);

    }

}
