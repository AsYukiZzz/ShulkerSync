package xyz.saturnhalo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.saturnhalo.anno.MethodDescription;
import xyz.saturnhalo.domain.dto.ChildTagCreateReq;
import xyz.saturnhalo.domain.dto.RootTagCreateReq;
import xyz.saturnhalo.result.Result;
import xyz.saturnhalo.service.TagService;

/**
 * 标签相关 API
 */
@Validated
@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 添加根标签
     */
    @PostMapping("/root")
    @MethodDescription("新增根标签")
    public Result<Void> addRootTag(@RequestBody @Validated RootTagCreateReq req) {
        tagService.addRootTag(req);
        return Result.ok();
    }

    /**
     * 添加子标签
     */
    @PostMapping("/child")
    @MethodDescription("新增子标签")
    public Result<Void> addChildTag(@RequestBody @Validated ChildTagCreateReq req){
        tagService.addChildTag(req);
        return Result.ok();
    }
}