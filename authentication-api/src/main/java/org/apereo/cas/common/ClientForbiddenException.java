package org.apereo.cas.common;

/**
 * Clients forbidden exception
 * 
 * @author james
 *
 */
public class ClientForbiddenException extends BaseException {
	private static final long serialVersionUID = 1L;

	public ClientForbiddenException(String message, Integer code) {
		super(message, code);
	}
}