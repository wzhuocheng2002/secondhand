package com.example.controller;

import com.example.common.Result;
import com.example.entity.Circles;
import com.example.service.CirclesService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户前端操作接口
 **/
@RestController
@RequestMapping("/circles")
public class CirclesController {

    @Resource
    private CirclesService circlesService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Circles circles) {
        circlesService.add(circles);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        circlesService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        circlesService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Circles circles) {
        circlesService.updateById(circles);
        return Result.success();
    }

    /**
     * 根据Name查询
     */
    @GetMapping("/selectCirclesName/{name}")
    public Result selectByCirclesName(@PathVariable String name) {
        Circles circles = circlesService.selectByCirclesName(name);
        return Result.success(circles);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Circles circles ) {
        List<Circles> list = circlesService.selectAll(circles);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Circles circles,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Circles> page = circlesService.selectPage(circles, pageNum, pageSize);
        return Result.success(page);
    }

}