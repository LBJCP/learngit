package com.fridge.skl.dto;

import com.fridge.skl.entity.UnlifeTogetherfood;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UnlifeTogetherfoodMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UnlifeTogetherfood record);

    int insertSelective(UnlifeTogetherfood record);

    int insertOrUpadte(UnlifeTogetherfood record);

    UnlifeTogetherfood selectByPrimaryKey(Integer id);

    List<UnlifeTogetherfood> selectByFoodid(Integer id);

    int updateByPrimaryKeySelective(UnlifeTogetherfood record);

    int updateByPrimaryKey(UnlifeTogetherfood record);
}