package org.example.week6.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.example.week6.entity.Special;
import org.example.week6.mapper.SpecialMapper;
import org.example.week6.service.SpecialService;


@Service
@RequiredArgsConstructor
public class SpecialServiceImpl implements SpecialService {
    private final SpecialMapper specialMapper;


    @Override
    public Page<Special> selectByTitle(String title, int pageNum, int pageSize) {
        Page<Special> page = Page.of(pageNum, pageSize);
        LambdaQueryWrapper<Special> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(title != null && !title.isEmpty(), Special::getTitle, title);
        return specialMapper.selectPage(page, wrapper);
    }

    @Override
    public Special getById(String id) {
        return specialMapper.selectById(id);
    }

}