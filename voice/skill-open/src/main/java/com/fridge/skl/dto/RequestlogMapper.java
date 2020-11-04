package com.fridge.skl.dto;

import com.fridge.skl.entity.Requestlog;
import org.springframework.stereotype.Component;

@Component
public interface RequestlogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Requestlog record);

    int insertSelective(Requestlog record);

    Requestlog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Requestlog record);

    int updateByPrimaryKey(Requestlog record);
}