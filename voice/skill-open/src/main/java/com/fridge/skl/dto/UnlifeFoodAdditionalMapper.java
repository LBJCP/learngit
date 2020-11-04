package com.fridge.skl.dto;

import com.fridge.skl.entity.UnlifeFoodAdditional;
import org.springframework.stereotype.Component;

@Component
public interface UnlifeFoodAdditionalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UnlifeFoodAdditional record);

    int insertSelective(UnlifeFoodAdditional record);

    UnlifeFoodAdditional selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UnlifeFoodAdditional record);

    int updateByPrimaryKey(UnlifeFoodAdditional record);

    int insertOrUpdate(UnlifeFoodAdditional record);
}