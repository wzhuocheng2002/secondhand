package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Help;
import com.example.mapper.HelpMapper;
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
public class HelpService {

    @Resource
    private HelpMapper helpMapper;

    /**
     * 新增
     */
    public void add(Help help) {
        help.setTime(DateUtil.now());
        Account currentUser = TokenUtils.getCurrentUser();
        help.setUserId(currentUser.getId());
        help.setStatus("待审核");
        helpMapper.insert(help);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        helpMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            helpMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Help help) {
        helpMapper.updateById(help);
    }

    /**
     * 根据ID查询
     */
    public Help selectById(Integer id) {
        return helpMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Help> selectAll(Help help) {
        return helpMapper.selectAll(help);
    }

    /**
     * 分页查询
     */
    public PageInfo<Help> selectPage(Help help, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Help> list = helpMapper.selectAll(help);
        return PageInfo.of(list);
    }




}