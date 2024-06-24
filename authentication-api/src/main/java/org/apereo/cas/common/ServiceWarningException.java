package org.apereo.cas.common;

import java.util.Collection;

/**
 * Service warning exception
 * 
 * @author wyx
 *
 */
public class ServiceWarningException extends BaseException {
	private static final long serialVersionUID = 1L;

	public ServiceWarningException(String message) {
		super(message);
	}

	public ServiceWarningException(int code) {
		super(null, code);
	}

	public ServiceWarningException(int code, Object... params) {
		super(null, code, params);
	}

	public ServiceWarningException(int code, Collection<?> details) {
		super(null, code, details);
	}

	public ServiceWarningException(int code, Object[] params, Collection<?> details) {
		super(null, code, params, details);
	}

	public ServiceWarningException(String message, int code) {
		super(message, code);
	}

	public ServiceWarningException(String message, int code, Object[] params) {
		super(message, code, params);
	}

	public ServiceWarningException(String message, int code, Object[] params, Collection<?> details) {
		super(message, code, params, details);
	}

}
