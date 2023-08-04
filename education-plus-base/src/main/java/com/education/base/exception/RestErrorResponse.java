package com.education.base.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * @description: 用于传输异常信息
 * @author：wufengning
 * @date: 2023/8/2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestErrorResponse implements Serializable {
    private String errMessage;

}