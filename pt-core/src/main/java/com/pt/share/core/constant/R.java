package com.pt.share.core.constant;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName R
 * @Description TODO
 * @Author potato
 * @Date 2020/12/15 下午10:49
 **/
@Data
@NoArgsConstructor
public class R<T> {

    private Integer code;
    private String message;
    private T data;

    public R(Response response) {
        this.code = response.getCode();
        this.message = response.getMessage();
        this.data = null;
    }
    public R(Response response,Object o) {
        this.code = response.getCode();
        this.message = response.getMessage();
        this.data = (T) o;
    }

}
