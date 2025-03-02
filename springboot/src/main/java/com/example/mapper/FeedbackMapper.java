package com.example.mapper;

import com.example.entity.Feedback;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作feedback相关数据接口
*/
public interface FeedbackMapper {

    /**
      * 新增
    */
    int insert(Feedback feedback);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Feedback feedback);

    /**
      * 查询所有
    */
    List<Feedback> selectAll(Feedback feedback);


    Feedback selectById(Integer id);

    Feedback selectByTitle(@Param("title") String title);
}