package com.supbuilder.file.service.impl;

import com.supbuilder.common.core.constant.FileHandleTypeConstants;
import com.supbuilder.common.core.util.R;
import com.supbuilder.common.core.util.RedisUtil;
import com.supbuilder.file.api.vo.FileHandleVO;
import com.supbuilder.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public R getFileStatus(String fileId) {
        FileHandleVO fileHandleVO = (FileHandleVO) redisUtil.hget(FileHandleTypeConstants.FILE_CONVERSE,fileId);
        if (fileHandleVO==null){
            return R.failed("不存在文件格式转换任务");
        }else {
            return R.ok(fileHandleVO);
        }
    }
}
