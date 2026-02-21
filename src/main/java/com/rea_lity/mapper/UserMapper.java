package com.rea_lity.mapper;

import com.rea_lity.modle.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author DELL
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2026-02-21 14:17:07
* @Entity com.rea_lity.modle.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




