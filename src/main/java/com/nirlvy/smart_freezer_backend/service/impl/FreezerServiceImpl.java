package com.nirlvy.smart_freezer_backend.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nirlvy.smart_freezer_backend.entity.Freezer;
import com.nirlvy.smart_freezer_backend.mapper.FreezerMapper;
import com.nirlvy.smart_freezer_backend.service.IFreezerService;

@Service
public class FreezerServiceImpl extends ServiceImpl<FreezerMapper, Freezer> implements IFreezerService {

}
