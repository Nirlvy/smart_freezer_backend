package com.nirlvy.smart_freezer_backend.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nirlvy.smart_freezer_backend.common.Constants;
import com.nirlvy.smart_freezer_backend.entity.Files;
import com.nirlvy.smart_freezer_backend.exception.ServiceException;
import com.nirlvy.smart_freezer_backend.mapper.FileMapper;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FileMapper fileMapper;

    @PostMapping("/upload")
    public void upload(@RequestParam MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        // 磁盘存储
        File uploadParentFile = new File(fileUploadPath);
        // 判断目录存在
        if (!uploadParentFile.exists()) {
            uploadParentFile.mkdirs();
        }
        // 标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;
        try {
            File uploadFile = new File(fileUploadPath + fileUUID);

            // 查询并读取文件md5是否存在
            String md5 = SecureUtil.md5(uploadFile);
            QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("md5", md5);
            Files files = fileMapper.selectOne(queryWrapper);

            String url;
            if (files != null) {
                url = files.getUrl();
                throw new ServiceException(Constants.CODE_400, "文件已存在");
            } else {
                // 转存
                file.transferTo(uploadFile);
                url = "http://localhost:8080/file/" + fileUUID;
                Files saveFile = new Files();
                saveFile.setUrl(url);
                saveFile.setMd5(md5);
                saveFile.setName(originalFilename);
                saveFile.setType(type);
                saveFile.setSize(size / 1024);
                fileMapper.insert(saveFile);
            }
        } catch (IOException e) {
            throw new ServiceException(Constants.CODE_500, "上传失败");
        }
    }

    /**
     * 文件下载
     * 
     * @param fileUUID
     * @param response
     */

    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) {
        // 根据标识符获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        try {
            // 设置输出流格式
            ServletOutputStream outputStream = response.getOutputStream();
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
            response.setContentType("application/octet-stream");
            // 读取文件字节流
            outputStream.write(FileUtil.readBytes(uploadFile));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new ServiceException(Constants.CODE_500, "下载失败");
        }
    }
}
