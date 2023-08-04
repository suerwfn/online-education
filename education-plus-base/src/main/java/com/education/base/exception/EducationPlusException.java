package com.education.base.exception;

import lombok.Getter;

/**
 * @description: 自定义异常处理
 * @author：wufengning
 * @date: 2023/8/2
 */
@Getter
public class EducationPlusException extends RuntimeException{
    private String message;

    public EducationPlusException(String message) {
        super(message);
        this.message = message;
    }

    public static void cast(String message){
        throw new EducationPlusException(message);
    }

    public static void cast(CommonError error){
        throw new EducationPlusException(error.getErrMessage());
    }
}
