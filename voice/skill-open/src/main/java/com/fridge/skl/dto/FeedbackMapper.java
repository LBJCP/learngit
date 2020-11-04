package com.fridge.skl.dto;

import com.fridge.skl.entity.Feedback;
import com.fridge.skl.entity.Pregnant;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Component
public interface FeedbackMapper {
    @Insert("insert into feedback (userid,deviceid,content,teiminal,time)values (#{userid}, #{deviceid},#{content},#{teiminal},now())")
    void insert(Feedback feedback);

}
