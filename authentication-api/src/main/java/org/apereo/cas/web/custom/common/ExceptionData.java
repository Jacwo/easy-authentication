package org.apereo.cas.web.custom.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;

/**
 * Exception data
 * 
 * @author wyx
 *
 */
@NoArgsConstructor
@Getter
@Setter
public class ExceptionData implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Error code
	 */
	@JsonInclude(Include.NON_EMPTY)
	private Integer errorCode;
	/**
	 * Error message
	 */
	@JsonInclude(Include.NON_NULL)
	private String errorMessage;
	/**
	 * Error message details as list
	 */
	@JsonInclude(Include.NON_EMPTY)
	private Collection<?> details;
	/**
	 * Exception stack trace
	 */
	@JsonIgnore
	private String cause;

	@JsonInclude(Include.NON_NULL)
	private Object[] params;

	public ExceptionData(Integer errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public ExceptionData(Integer errorCode, String errorMessage, Collection<?> details) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.details = details;
	}

	public ExceptionData(Integer errorCode, String errorMessage, Object[] params, Collection<?> details) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.params = params;
		this.details = details;
	}

	public ExceptionData(Integer errorCode, String errorMessage, Collection<?> details, String cause) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.details = details;
		this.cause = cause;
	}

	public ExceptionData(Integer errorCode, String errorMessage, String cause) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.cause = cause;
	}
}
