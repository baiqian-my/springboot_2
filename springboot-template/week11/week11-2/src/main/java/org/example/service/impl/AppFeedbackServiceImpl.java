package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.AppFeedback;
import org.example.mapper.AppFeedbackMapper;
import org.example.service.AppFeedbackService;
import org.springframework.stereotype.Service;


@Service
public class AppFeedbackServiceImpl extends ServiceImpl<AppFeedbackMapper, AppFeedback> implements AppFeedbackService {
}