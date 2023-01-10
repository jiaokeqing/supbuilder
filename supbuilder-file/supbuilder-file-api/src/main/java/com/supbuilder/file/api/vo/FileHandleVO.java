package com.supbuilder.file.api.vo;

import com.supbuilder.file.api.constant.FileStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文件处理情况
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileHandleVO implements Serializable {
    private String fileId;
    private String downloadUrl;
    private FileStatusEnum status;
    private String msg;
}
