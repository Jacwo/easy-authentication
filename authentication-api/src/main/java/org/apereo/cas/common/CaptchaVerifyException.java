package org.apereo.cas.common;

import org.apereo.cas.authentication.RootCasException;

/**
 * 
 * @author zhouliang
 *
 */
public class CaptchaVerifyException extends RootCasException {

	private static final long serialVersionUID = 6187128957519204825L;

	/** Code description. */
	public static final String CODE = "BAD_CAPTCHA";

	public CaptchaVerifyException(String msg) {
		super(CODE, msg);
	}

	public CaptchaVerifyException(final String msg, final Throwable throwable) {
		super(msg, throwable);
	}
}
