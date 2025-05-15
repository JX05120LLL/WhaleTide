package com.whaletide.common.exception.file;

import com.whaletide.common.exception.base.BusinessException;

/**
 * 文件操作异常
 * 用于处理文件上传、下载、读写等操作的异常
 */
public class FileOperationException extends BusinessException {
    
    public FileOperationException(String message) {
        super(message);
    }
    
    public FileOperationException(String operation, String fileName) {
        super("文件" + fileName + operation + "失败");
    }
    
    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
    }
} 