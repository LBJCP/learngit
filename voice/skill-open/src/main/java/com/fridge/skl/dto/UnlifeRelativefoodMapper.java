package com.fridge.skl.dto;

import com.fridge.skl.entity.UnlifeRelativefood;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UnlifeRelativefoodMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UnlifeRelativefood record);

    int insertSelective(UnlifeRelativefood record);

    int insertOrUpadte(UnlifeRelativefood record);

    UnlifeRelativefood selectByPrimaryKey(Integer id);

    List<UnlifeRelativefood> selectByFoodid(Integer id);

    List<String> selectallfoodname();
    int updateByPrimaryKeySelective(UnlifeRelativefood record);

    int updateByPrimaryKey(UnlifeRelativefood record);
}