package com.ws.oms.result;

import java.io.Serializable;

/**
 * 
 * Title: Class Result
 * Description:
 *
 *	结算返回对象
 * @author guoqiang.zhao
 * @email  guoqiang.zhao@chinaredstar.com
 * @version 1.0.0
 * @param <T>
 */
public class Result<T> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7736562272493014127L;
	private String code;// return code
	private String message;// return message
	private boolean success;// 操作标识
	private T value;// 返回对象

    public Result() {
    }

    /**
	 * construction method
	 * @param code
	 * 	return code
	 * @param message
	 * 	return message
	 * @param success
	 * 	operation flag
	 * @param value
	 * 	return object
	 */
	public Result(String code, String message, boolean success, T value) {
		this.code = code;
		this.message = message;
		this.success = success;
		this.value = value;
	}
	
	/**
	 * 构建返回对象实例
	 * Created on 2016年11月29日
	 * @author guoqiang.zhao
	 * @version 
	 * @param successCode
	 * @return
	 */
	public static <T> Result<T> newSuccess(ResultCode successCode) {
		return new Result<T>(successCode.getCode(), successCode.getMessage(),
				true, null);
	}
	
	
	/**
	 * 构建返回对象实例
	 * Created on 2016年11月29日
	 * @author guoqiang.zhao
	 * @version 
	 * @param successCode
	 * @return
	 */
	public static <T> Result<T> newSuccess(ResultCode successCode,T data) {
		return new Result<T>(successCode.getCode(), successCode.getMessage(),
				true, data);
	}
	
	/**
	 * 
	 * 构建返回对象实例
	 * Created on 2017年2月22日
	 * @author guoqiang.zhao
	 * @version 
	 * @return
	 */
    public static <T> Result<T> newSuccess() {
        return new Result<T>(CommonResultCode.C200.code,CommonResultCode.C200.getMessage(),true, null);
    }

    public static <T> Result<T> newSuccess(T t) {
    	return newSuccess(CommonResultCode.C200, t);
	}

    /**
     * 
     * 构建返回对象实例
     * Created on 2017年2月22日
     * @author guoqiang.zhao
     * @version 
     * @param code
     * @param message
     * @param t
     * @return
     */
    public static <T> Result<T> newSuccess(String code ,String message , T t) {
        return new Result<T>(code, message, true, t);
    }
	
	/**
	 * 
	 *  构建返回对象实例
	 * Created on 2016年11月29日
	 * @author guoqiang.zhao
	 * @version 
	 * @param errorCode
	 * @return
	 */
	public static <T> Result<T> newError(ResultCode errorCode) {
		return new Result<T>(errorCode.getCode(), errorCode.getMessage(),
				false, null);
	}
	
	/**
	 * 
	 * 构建返回对象实例
	 * Created on 2017年2月22日
	 * @author guoqiang.zhao
	 * @version 
	 * @param code
	 * @param message
	 * @param t
	 * @return
	 */
	public static <T> Result<T> newError(String code ,String message , T t) {
		return new Result<T>(code, message, false, t);
	}
	
	/**
	 * 
	 * 构建返回对象实例
	 * Created on 2017年2月22日
	 * @author guoqiang.zhao
	 * @version 
	 * @param code
	 * @param message
	 * @return
	 */
	public static <T> Result<T> newError(String code ,String message) {
		return new Result<T>(code, message, false, null);
	}

	

	/**
	 * 
	 * 构建返回对象实例
	 * Created on 2016年10月12日
	 * @author guoqiang.zhao
	 * @version 
	 * @param resultCode
	 * @param success
	 * @param value
	 * @return
	 */
	public static <T> Result<T> newInstance(
			ResultCode resultCode, boolean success, T value) {
		return new Result<T>(resultCode.getCode(), resultCode.getMessage(),
				success, value);
	}

	/**
	 * 
	 * 设置错误编码
	 * Created on 2016年10月12日
	 * @author guoqiang.zhao
	 * @version 
	 */
	public  Result<T> setErrorCode(String code,String message){
		this.code = code;
		this.message = message;
		this.success = false;
		return this;
	}

	/**
	 * 
	 * 设置错误编码
	 * Created on 2016年10月12日
	 * @author guoqiang.zhao
	 * @version 
	 * @param errorCode
	 */
	public  Result<T> setErrorCode(ResultCode errorCode){
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
		this.success = false;
		return this;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Result [code=" + code + ", message=" + message
				+ ", success=" + success + ", value=" + value + "]";
	}

	
	
	

}
