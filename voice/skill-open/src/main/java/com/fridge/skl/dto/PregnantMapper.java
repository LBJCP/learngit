package com.fridge.skl.dto;

import com.fridge.skl.entity.Pregnant;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
public interface PregnantMapper {
    //TODO 修改数据库字段  增加插入时间等字段
    @Insert("insert into pregnant (userid,starttime,pretime)values (#{userid},#{starttime},#{pretime})")
     void insert(Pregnant pregnant);

    @Select("select * from pregnant where userid = ${userid} and cleanflg = 0")
    @Results({
            @Result(property = "userid", column = "userid"),
            @Result(property = "starttime", column = "starttime"),
            @Result(property = "pretime", column = "pretime")
    })
    Pregnant get(@Param("userid") String userid);

    @Delete("Delete from pregnant where userid =${userid}")
    @Update("Update pregnant set cleanflg = 0 where userid =${userid}")
    void delete(Pregnant pregnant);
}
