package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Posts;
import com.example.mapper.PostsMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员业务处理
 **/
@Service
public class PostsService {

    @Resource
    private PostsMapper postsMapper;

    /**
     * 新增
     */
    public void add(Posts posts) {
        posts.setTime(DateUtil.now());
        Account currentUser = TokenUtils.getCurrentUser();
        posts.setUserId(currentUser.getId());
        posts.setStatus("待审核");
        postsMapper.insert(posts);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        postsMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            postsMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Posts posts) {
        postsMapper.updateById(posts);
    }

    /**
     * 根据ID查询
     */
    public Posts selectByTitle(String title) {
        return postsMapper.selectByTitle(title);
    }

    /**
     * 查询所有
     */
    public List<Posts> selectAll(Posts posts) {
        return postsMapper.selectAll(posts);
    }

    /**
     * 分页查询
     */
    public PageInfo<Posts> selectPage(Posts posts, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Posts> list = postsMapper.selectAll(posts);
        return PageInfo.of(list);
    }




}