package org.example.week6.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.week6.entity.Special;


public interface SpecialService {

    Page<Special> selectByTitle(String title, int pageNum, int pageSize);

    Special getById(String id);
}