package xyz.saturnhalo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.saturnhalo.anno.MethodDescription;
import xyz.saturnhalo.domain.dto.ModpackCreateReq;
import xyz.saturnhalo.result.Result;
import xyz.saturnhalo.service.ModpackService;

/**
 * 整合包相关 API
 */
@Validated
@RestController
@RequestMapping("/modpacks")
public class ModpackController {

    @Autowired
    private ModpackService modpackService;

    /**
     * 添加整合包
     */
    @PostMapping
    @MethodDescription("添加整合包")
    public Result<Void> addModpack(@RequestBody @Validated ModpackCreateReq req) {
        modpackService.addModpack(req);
        return Result.ok();
    }
}