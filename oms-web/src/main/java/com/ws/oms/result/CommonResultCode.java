/**
 * Created on 2017年2月14日
 * filename: PaymentResultCode.java
 * Description: 
 *
 */
package com.ws.oms.result;

/**
 * Title: Class PaymentResultCode
 * Description:
 *
 *
 * @author guoqiang.zhao
 * @email  guoqiang.zhao@chinaredstar.com
 * @version 1.0.0
 */
public enum CommonResultCode implements ResultCode {
	
	/***********************************
	*  支付api，除了共用的C开头的错误		   *
	*  统一前缀p开头，0~100位。  	   		   *
	************************************/
	
    C200("200", "Success"),
    C403("403", "Forbidden"),
    C500("500", "Internal Server Error"),
    C400("400", "请求参数错误"),
    C402("-402", "用户未登录!"),
    COMMON_PARAM_ERROR("001","必要的参数不能为空"),

    MARKET_PARAM_ERROR("C002","获取商场编号为空"),
    EMPLOYEE_PARAM_ERROR("C003","获取用户信息不全"),
    DEDUCT_AVAILABLE_ERROR("C004","帐扣金额不足"),
    ORDER_TRADE_EMPTY_ERROR("C005","该订单不存在交易记录"),
    ORDER_TRADE_NOFINISHES("C006","返款Finishes查询交易明细失败"),
    ORDER_SHOP_EMPTY("C007","商户账户信息查询失败"),
	ORDER_EMPLYEETYPE_EMPTY("C008","订单类型无法区分"),

    ;
    
    
    public final String code;
	public final String message;

	private CommonResultCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
