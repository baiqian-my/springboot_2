package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.SysUser;


@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    // 使用 MyBatis Plus 的 selectOne 方法，自动处理逻辑删除
}
