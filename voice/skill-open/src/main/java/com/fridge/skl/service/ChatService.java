package com.fridge.skl.service;

import com.fridge.skl.model.GuessIntent;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.model.Slot;
import com.fridge.skl.util.UtilConstants;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService extends BaseService {

    /**
     * 叫爸爸技能
     */
    public ResponseObj askFather(RequestObj request) {
        ResponseObj resp = initResporse(request);
        GuessIntent[] intents = request.getRequest().getIntents();

        String content = "我就不叫，你是不是想占我便宜";


        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            Slot[] slots = intent.getSlots();


            String name = getSlot(slots, UtilConstants.Slot.SLOT_NAME_NAME);
            if (StringUtils.isEmpty(name)) {
                name = findword(request.getRequest().getQuery().getContent(), NAMELIST);
            }
            if (!StringUtils.isEmpty(name)) {

                content = name;

            }

        }


        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    private static List<String> NAMELIST = new ArrayList<>();

    static {

        NAMELIST.add("爸爸");
        NAMELIST.add("妈妈");
        NAMELIST.add("老公");
        NAMELIST.add("爷爷");
        NAMELIST.add("奶奶");
        NAMELIST.add("叔叔");
        NAMELIST.add("姥爷");
        NAMELIST.add("大爷");
        NAMELIST.add("哥哥");
        NAMELIST.add("主人");
        NAMELIST.add("姐姐");
        NAMELIST.add("老爸");
        NAMELIST.add("老妈");

    }

}
