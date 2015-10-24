package com.stargame.ad.util.global;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;


/**
 * 名称： AppException.java 描述：公共异常类.
 * 
 * @author hxy
 * @version v1.0
 * @date：2015-06-11
 */
public class AppException extends Exception {

	private static final long serialVersionUID = 1;

	/** 异常消息. */
	private String msg = null;

	/**
	 * 构造异常类.
	 * 
	 * @param e异常
	 */
	public AppException(Exception e) {
		super();

		try {
			if (e instanceof HttpHostConnectException) {
				msg = AppConfig.UNKNOWN_HOST_EXCEPTION;
			} else if (e instanceof ConnectException) {
				msg = AppConfig.CONNECT_EXCEPTION;
			} else if (e instanceof ConnectTimeoutException) {
				msg = AppConfig.CONNECT_EXCEPTION;
			} else if (e instanceof UnknownHostException) {
				msg = AppConfig.UNKNOWN_HOST_EXCEPTION;
			} else if (e instanceof SocketException) {
				msg = AppConfig.SOCKET_EXCEPTION;
			} else if (e instanceof SocketTimeoutException) {
				msg = AppConfig.SOCKET_TIMEOUT_EXCEPTION;
			} else if (e instanceof NullPointerException) {
				msg = AppConfig.NULL_POINTER_EXCEPTION;
			} else if (e instanceof ClientProtocolException) {
				msg = AppConfig.CLIENT_PROTOCOL_EXCEPTION;
			} else {
				if (e == null ||(e.getMessage()==null&&e.getMessage().equals(""))) {
					msg = AppConfig.NULL_MESSAGE_EXCEPTION;
				} else {
					msg = e.getMessage();
				}
			}
		} catch (Exception e1) {
		}

	}

	/**
	 * 用一个消息构造异常类.
	 * 
	 * @param message
	 *            异常的消息
	 */
	public AppException(String message) {
		super(message);
		msg = message;
	}

	/**
	 * 描述：获取异常信息.
	 * 
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return msg;
	}

}
