package com.nirlvy.smart_freezer_backend.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
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
    public String upload(@RequestParam MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        Long size = file.getSize();
        // 标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 查询并读取文件md5是否存在
        String md5 = getMd5ByInputStream(file);
        Files dbfiles = getFileByMd5(md5);
        String url;
        if (dbfiles != null) {
            url = dbfiles.getUrl();
            uploadFile.delete();
        } else {
            File parentFile = uploadFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            try {
                file.transferTo(uploadFile);
            } catch (IOException e) {
                throw new ServiceException(Constants.CODE_500, "保存失败");
            }
            // TODO:需要修改
            url = "http://localhost:8080/file/" + fileUUID;
            Files saveFile = new Files();
            saveFile.setUrl(url);
            saveFile.setMd5(md5);
            saveFile.setName(originalFilename);
            saveFile.setType(type);
            saveFile.setSize(size / 1024);
            fileMapper.insert(saveFile);
        }
        return url;
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

    private Files getFileByMd5(String md5) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    public static String getMd5ByInputStream(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 分多次将一个文件读入，对于大型文件而言，比较推荐这种方式；
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = inputStream.read(buffer, 0, 1024)) != -1) {
                messageDigest.update(buffer, 0, length);
            }
            // 转换并返回包含16个元素字节数组,返回数值范围为-128到127
            byte[] md5Bytes = messageDigest.digest();
            // 使用Hex编码
            return Hex.encodeHexString(md5Bytes);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
