package com.example.controller;

import com.example.common.Result;
import com.example.entity.Posts;
import com.example.service.PostsService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户前端操作接口
 **/
@RestController
@RequestMapping("/posts")
public class PostsController {

    @Resource
    private PostsService postsService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Posts posts) {
        postsService.add(posts);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        postsService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        postsService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Posts posts) {
        postsService.updateById(posts);
        return Result.success();
    }

    /**
     * 根据Name查询
     */
    @GetMapping("/selectPostsTitle/{title}")
    public Result selectByPostsTitle(@PathVariable String title) {
        Posts posts = postsService.selectByTitle(title);
        return Result.success(posts);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Posts posts ) {
        List<Posts> list = postsService.selectAll(posts);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Posts posts,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Posts> page = postsService.selectPage(posts, pageNum, pageSize);
        return Result.success(page);
    }

}