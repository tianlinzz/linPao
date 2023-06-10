package com.tianlin.linpaobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianlin.linpaobackend.mapper.TagMapper;
import com.tianlin.linpaobackend.model.domain.Tag;
import com.tianlin.linpaobackend.service.TagService;
import org.springframework.stereotype.Service;

/**
* @author 张添琳
* @description 针对表【tag(标签表)】的数据库操作Service实现
* @createDate 2023-06-09 17:25:01
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




