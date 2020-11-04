package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.NoteService;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.NOTE_SKILL_ID;

@Component(NOTE_SKILL_ID)
public class Notehandler implements ResultHandler {
    @Autowired
    NoteService noteService;

    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        String intentName = requestObj.getRequest().getIntents()[0].getIntentName();
        switch (intentName) {
            case UtilConstants.Intent.NOTESOMETHING_INTENT_NAME:
            default:
                return noteService.noteSomething(requestObj);
            case UtilConstants.Intent.NOTEME_INTENT_NAME:
                return noteService.noteMe(requestObj);
            case UtilConstants.Intent.CLEAR_NOTE_INTENT_NAME:
                return noteService.clearNote(requestObj);
            case UtilConstants.Intent.DEL_LASTNOTE_INTENT_NAME:
                return noteService.dellastNote(requestObj);
            case UtilConstants.Intent.LAST_NOTE_INTENT_NAME:
                return noteService.lastNote(requestObj);
            case UtilConstants.Intent.QUER_SHOPLIST_INTENT_NAME:
                return noteService.queryShopList(requestObj);
            case UtilConstants.Intent.ADD_SHOPLIST_INTENT_NAME:
                return noteService.addShopList(requestObj);
            case UtilConstants.Intent.DEL_SHOPLIST_INTENT_NAME:
                return noteService.delShopList(requestObj);

        }


    }
}
