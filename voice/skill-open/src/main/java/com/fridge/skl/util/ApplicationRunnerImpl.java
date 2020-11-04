package com.fridge.skl.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
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


        JSONObject json =  JSONObject.parseObject(sb.toString());
        JSONArray arr = json.getJSONArray("data");
        CommonUtil.getFoodJsonSource().put("food",arr);

    }
}
