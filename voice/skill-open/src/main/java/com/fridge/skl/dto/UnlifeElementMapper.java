package com.fridge.skl.dto;

import com.fridge.skl.entity.UnlifeElement;
import org.springframework.stereotype.Component;

@Component
public interface UnlifeElementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UnlifeElement record);

    int insertSelective(UnlifeElement record);

    UnlifeElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UnlifeElement record);

    int updateOrUpdate(UnlifeElement record);

    int updateByPrimaryKey(UnlifeElement record);
}