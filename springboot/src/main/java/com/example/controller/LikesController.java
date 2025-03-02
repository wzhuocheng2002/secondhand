package com.example.controller;

import com.example.common.Result;
import com.example.entity.Likes;
import com.example.service.LikesService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("likes")
public class LikesController {
    @Resource
    LikesService likesService;

    @PostMapping("/add")
    public Result add(@RequestBody Likes likes){
        likesService.add(likes);
        return Result.success();
    }
}
