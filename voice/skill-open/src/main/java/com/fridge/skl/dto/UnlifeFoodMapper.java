package com.fridge.skl.dto;

import com.fridge.skl.entity.UnlifeFood;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UnlifeFoodMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UnlifeFood record);

    int insertSelective(UnlifeFood record);

    UnlifeFood selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UnlifeFood record);

    int inserorupdate(UnlifeFood record);

    int updateByPrimaryKey(UnlifeFood record);

    UnlifeFood selectByName(UnlifeFood record);

    List<String> selectAllfoodname();
}