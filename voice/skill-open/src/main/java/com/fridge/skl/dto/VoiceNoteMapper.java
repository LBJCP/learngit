package com.fridge.skl.dto;

import com.fridge.skl.entity.VoiceNote;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface VoiceNoteMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByUserid(String userid);
    int deleteLastByUserid(String userid);

    int insert(VoiceNote record);

    VoiceNote selectByPrimaryKey(Integer id);

    VoiceNote selectByUserid(@Param("userid") String Userid,@Param("page") int page);

}