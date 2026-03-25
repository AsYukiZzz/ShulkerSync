package xyz.saturnhalo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.saturnhalo.domain.vo.CheckResult;
import xyz.saturnhalo.domain.vo.MergeResult;
import xyz.saturnhalo.result.Result;
import xyz.saturnhalo.service.impl.LocalUploadServiceImpl;

/**
 * 版本相关 API
 */
@Validated
@RestController
@RequestMapping("/modpacks/{slug}/versions")
public class VersionController {

    @Autowired
    private LocalUploadServiceImpl uploadService;

    /**
     * 上传文件预检
     */
    @GetMapping("/upload/check")
    public Result<CheckResult> uploadCheck(
            @PathVariable String slug,
            @RequestParam String hash,
            @RequestParam Integer totalChunk
    ) {
        return Result.ok(uploadService.uploadCheck(slug, hash, totalChunk));
    }

    /**
     * 上传分片文件
     */
    @PostMapping("/upload/chunk")
    public Result<Void> uploadChunk(
            @PathVariable String slug,
            @RequestParam String modpackHash,
            @RequestParam MultipartFile file,
            @RequestParam String chunkHash,
            @RequestParam Integer chunkIndex
    ) {
        uploadService.uploadChunk(slug, modpackHash, file, chunkHash, chunkIndex);
        return Result.ok();
    }

    /**
     * 合并分片文件
     */
    @PostMapping("/upload/merge")
    public Result<MergeResult> uploadMerge(
            @PathVariable String slug,
            @RequestParam String hash,
            @RequestParam String fileName,
            @RequestParam Integer totalChunk
    ) {
        return Result.ok(uploadService.uploadMerge(slug, hash, fileName, totalChunk));
    }
}