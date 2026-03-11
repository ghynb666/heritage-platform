package com.heritage.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.common.Result;
import com.heritage.dto.AnnouncementDTO;
import com.heritage.service.AnnouncementAdminService;
import com.heritage.vo.AnnouncementVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "管理员公告接口")
@RestController
@RequestMapping("/api/admin/announcement")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAnnouncementController {
    @Autowired
    private AnnouncementAdminService announcementAdminService;

    @ApiOperation("公告分页列表")
    @GetMapping("/page")
    public Result<Page<AnnouncementVO>> getPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(announcementAdminService.getPage(page, size));
    }

    @ApiOperation("新增公告")
    @PostMapping
    public Result<?> add(@Validated @RequestBody AnnouncementDTO dto) {
        announcementAdminService.add(dto);
        return Result.success();
    }

    @ApiOperation("更新公告")
    @PutMapping
    public Result<?> update(@Validated @RequestBody AnnouncementDTO dto) {
        announcementAdminService.update(dto);
        return Result.success();
    }

    @ApiOperation("删除公告")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        announcementAdminService.delete(id);
        return Result.success();
    }

    @ApiOperation("公告置顶")
    @PutMapping("/{id}/top")
    public Result<?> updateTop(@PathVariable Long id, @RequestParam Integer isTop) {
        announcementAdminService.updateTop(id, isTop);
        return Result.success();
    }
}
