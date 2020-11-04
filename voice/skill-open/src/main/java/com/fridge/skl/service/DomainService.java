package com.fridge.skl.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.*;
import com.fridge.skl.model.response.Command;
import com.fridge.skl.model.response.Speech;
import com.fridge.skl.model.response.SpeechContent;
import com.fridge.skl.util.UtilConstants;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component()
public class DomainService implements ResultHandler {

    @Override
    public ResponseObj handleResult(RequestObj request) {
        String intentName = request.getRequest().getIntents()[0].getIntentName();
        if (UtilConstants.Intent.OPERATE_APP_INTENT_NAME.equals(intentName)) {
            return doOperateApp(request);

        } else if (UtilConstants.Intent.QUERY_HEL_WEIGHT_INTENT_NAME.equals(intentName)) {

            return doQueryWeight(request);
        } else if (UtilConstants.Intent.QUERY_HEL_SCORE_INTENT_NAME.equals(intentName)) {

            return doQueryScore(request);
        } else if (UtilConstants.Intent.QUERY_HEL_ADD_INTENT_NAME.equals(intentName)) {

            return doAdd(request);
        } else if (UtilConstants.Intent.QUERY_HEL_HOWEAT_INTENT_NAME.equals(intentName)) {

            return doQueryEat(request);
        } else if (UtilConstants.Intent.QUERY_HEL_SPORT_INTENT_NAME.equals(intentName)) {

            return doQuerySport(request);
        } else if (UtilConstants.Intent.QUERY_HEL_AGE_INTENT_NAME.equals(intentName)) {

            return doQueryAge(request);
        }
        return new ResponseObj();
    }

    public  ResponseObj doQueryEat(RequestObj request) {
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
        GuessIntent[] intents = request.getRequest().getIntents();

        String content = "";


        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            Slot[] slots = intent.getSlots();


            if (slots.length > 0) {

                Command command = new Command();

                JSONArray arr = new JSONArray();
                JSONObject json = new JSONObject();
                JSONObject params = new JSONObject();
                for (int i = 0; i < slots.length; i++) {
                    params.put(slots[i].getName(), slots[i].getValues()[0]);
                }
                params.put("action", "queryeat");

                params.put("domain", "familyhealth");

                json.put("params", params);

                arr.add(json);
                command.setResults(arr);

                response.setCommand(command);


            }


        }


        speech.setContent(content);
        response.setSpeech(speech);
        response.setShouldEndSession(false);
        resp.setResponse(response);

        return resp;
    }

    /**
     * 查询年龄Domain
     *
     * @param request
     * @return
     */
    public  ResponseObj doQueryAge(RequestObj request) {
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
        GuessIntent[] intents = request.getRequest().getIntents();

        String content = "";


        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            Slot[] slots = intent.getSlots();


            if (slots.length > 0) {

                if (slots.length == 1) {

                    if (UtilConstants.Slot.SLOT_JT_NAME.equals(slots[0].getName())) {


                        Command command = new Command();

                        JSONArray arr = new JSONArray();
                        JSONObject json = new JSONObject();
                        JSONObject params = new JSONObject();


                        params.put("action", "queryage");
                        params.put("frperson", slots[0].getValues()[0]);
                        params.put("domain", "familyhealth");

                        json.put("params", params);

                        arr.add(json);
                        command.setResults(arr);

                        response.setCommand(command);


                    }


                }

            }


        }


        speech.setContent(content);
        response.setSpeech(speech);
        response.setShouldEndSession(false);
        resp.setResponse(response);

        return resp;
    }

    public  ResponseObj doQuerySport(RequestObj request) {
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
        GuessIntent[] intents = request.getRequest().getIntents();

        String content = "";


        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            Slot[] slots = intent.getSlots();


            if (slots.length > 0) {

                if (slots.length == 1) {

                    if (UtilConstants.Slot.SLOT_JT_NAME.equals(slots[0].getName())) {


                        Command command = new Command();

                        JSONArray arr = new JSONArray();
                        JSONObject json = new JSONObject();
                        JSONObject params = new JSONObject();


                        params.put("action", "querysport");
                        params.put("frperson", slots[0].getValues()[0]);
                        params.put("domain", "familyhealth");

                        json.put("params", params);

                        arr.add(json);
                        command.setResults(arr);

                        response.setCommand(command);


                    }


                }

            }


        }


        speech.setContent(content);
        response.setSpeech(speech);
        response.setShouldEndSession(false);
        resp.setResponse(response);

        return resp;
    }

    public  ResponseObj doQueryScore(RequestObj request) {
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
        GuessIntent[] intents = request.getRequest().getIntents();

        String content = "";


        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            Slot[] slots = intent.getSlots();


            if (slots.length > 0) {

                if (slots.length == 1) {

                    if (UtilConstants.Slot.SLOT_JT_NAME.equals(slots[0].getName())) {


                        Command command = new Command();

                        JSONArray arr = new JSONArray();
                        JSONObject json = new JSONObject();
                        JSONObject params = new JSONObject();


                        params.put("action", "queryscore");
                        params.put("frperson", slots[0].getValues()[0]);
                        params.put("domain", "familyhealth");

                        json.put("params", params);

                        arr.add(json);
                        command.setResults(arr);

                        response.setCommand(command);


                    }


                }

            }


        }


        speech.setContent(content);
        response.setSpeech(speech);
        response.setShouldEndSession(false);
        resp.setResponse(response);

        return resp;
    }

    public  ResponseObj doQueryWeight(RequestObj request) {
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
        GuessIntent[] intents = request.getRequest().getIntents();

        String content = "";


        if (intents.length > 0) {
            GuessIntent intent = intents[0];
            Slot[] slots = intent.getSlots();


            if (slots.length > 0) {

                if (slots.length == 1) {

                    if (UtilConstants.Slot.SLOT_JT_NAME.equals(slots[0].getName())) {


                        Command command = new Command();

                        JSONArray arr = new JSONArray();
                        JSONObject json = new JSONObject();
                        JSONObject params = new JSONObject();


                        params.put("action", "queryweight");
                        params.put("frperson", slots[0].getValues()[0]);
                        params.put("domain", "familyhealth");

                        json.put("params", params);

                        arr.add(json);
                        command.setResults(arr);

                        response.setCommand(command);


                    }


                }

            }


        }


        speech.setContent(content);
        response.setSpeech(speech);
        response.setShouldEndSession(false);
        resp.setResponse(response);

        return resp;
    }


    public  ResponseObj doAdd(RequestObj request) {
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


        Command command = new Command();

        JSONArray arr = new JSONArray();
        JSONObject json = new JSONObject();
        JSONObject params = new JSONObject();


        params.put("action", "addmember");
        params.put("domain", "familyhealth");

        json.put("params", params);

        arr.add(json);
        command.setResults(arr);

        response.setCommand(command);

        speech.setContent("");
        response.setSpeech(speech);
        response.setShouldEndSession(false);
        resp.setResponse(response);

        return resp;
    }

    public  ResponseObj doOperateApp(RequestObj request) {

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
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("intentName", UtilConstants.Intent.OPERATE_APP_INTENT_NAME);

        GuessIntent[] intents = request.getRequest().getIntents();

        GuessIntent intent = intents[0];
        Slot[] slots = intent.getSlots();
        for (Slot o : slots) {

            data.put(o.getName(), o.getValues()[0]);
        }

        sc.setData(data);
        speech.setContent(JSONObject.toJSONString(sc));
        response.setSpeech(speech);
        response.setShouldEndSession(false);
        resp.setResponse(response);

        return resp;

    }

}
