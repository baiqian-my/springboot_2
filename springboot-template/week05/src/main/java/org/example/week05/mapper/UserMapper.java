package org.example.week05.mapper;
import org.apache.ibatis.annotations.*;
import org.example.week05.entity.User;

import java.util.List;

/**
 * @author mqxu
 * @description 针对表 [t_user(用户表)] 的数据库操作 Mapper
 * @createDate 2026-04-02 15:10:24
 */
@Mapper
public interface UserMapper {

    /**
     * 插入数据
     *
     * @param record 插入的数据记录
     * @return 受影响的记录行数
     */
    @Insert("INSERT INTO t_user(username,password,age,email) VALUES (#{username}, #{password}, #{age}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User record);

    /**
     * 根据id查询数据
     *
     * @param id 主键
     * @return 数据记录
     */
    @Select("SELECT * FROM t_user WHERE id = #{id}")
    User selectByPrimaryKey(Long id);

    /**
     * 查询所有数据
     *
     * @return 数据列表
     */
    @Select("SELECT * FROM t_user")
    List<User> selectList();

    /**
     * 根据id更新数据
     *
     * @param record 更新的数据记录
     * @return 受影响的记录行数
     */
    @Update("UPDATE t_user SET username=#{username}, age=#{age}, email=#{email} WHERE id=#{id}")
    int updateByPrimaryKey(User record);

    /**
     * 根据id删除数据
     *
     * @param id 主键
     * @return 受影响的记录行数
     */
    @Delete("DELETE FROM t_user WHERE id = #{id}")
    int deleteByPrimaryKey(Long id);

    List<User> selectByCondition(String username, Integer minAge);
}