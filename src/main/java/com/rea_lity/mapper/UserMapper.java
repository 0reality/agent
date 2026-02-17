package com.rea_lity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rea_lity.modle.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author DELL
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2026-02-09 15:41:47
* @Entity com.rea_lity.modle.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




