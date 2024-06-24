package org.apereo.cas.common;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Base Exception
 * 
 * @author wyx
 *
 */
@Getter
@Setter
public class BaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/** The message key */
	private int code;

	/** The message parameters */
	private Object[] params;

	/** The detail information, expected to appear in the response payload */
	private Collection<?> details = new ArrayList<>();

	public BaseException(Throwable cause) {
		super(cause);
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String message, int code) {
		super(message);
		setCode(code);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(String message, Throwable cause, int code) {
		super(message, cause);
		setCode(code);
	}

	public BaseException(String message, int code, Object[] params) {
		super(message);
		setCode(code);
		setParams(params);
	}

	public BaseException(String message, int code, Collection<?> details) {
		super(message);
		setCode(code);
		setDetails(details);
	}

	public BaseException(String message, Throwable cause, int code, Object[] params) {
		super(message, cause);
		setCode(code);
		setParams(params);
	}

	public BaseException(String message, int code, Object[] params, Collection<?> details) {
		super(message);
		setCode(code);
		setParams(params);
		setDetails(details);
	}

	public BaseException(String message, Throwable cause, int code, Collection<?> details) {
		super(message, cause);
		setCode(code);
		setDetails(details);
	}

	public BaseException(String message, Throwable cause, int code, Object[] params, Collection<?> details) {
		super(message, cause);
		setCode(code);
		setParams(params);
		setDetails(details);
	}
}
