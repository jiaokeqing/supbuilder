package com.supbuilder.file.service;

import com.supbuilder.common.core.util.R;

public interface FileService {
    /**
     * 获取文件转换处理状态
     * @param fileId
     * @return
     */
    R getFileStatus(String fileId);
}
