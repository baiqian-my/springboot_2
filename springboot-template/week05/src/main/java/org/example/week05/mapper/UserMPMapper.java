package org.example.week05.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.week05.entity.User;

/**
 * @author mqxu
 * @date 2026/4/2
 * @description UserMPMapper：空接口：BaseMapper 已封装所有单表 CRUD 方法，无需手动编写
 **/
@Mapper
public interface UserMPMapper extends BaseMapper<User> {

}