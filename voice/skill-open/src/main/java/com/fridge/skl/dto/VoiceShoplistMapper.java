package com.fridge.skl.dto;

import com.fridge.skl.entity.VoiceShoplist;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VoiceShoplistMapper {
    int deleteByUserid(String userid);

    int insert(VoiceShoplist record);

    List<VoiceShoplist> selectByUserid(String userid);

}