package org.apereo.cas.adaptors.rest;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;

import java.util.Collection;

/**
 * Response data
 * 
 * @author wyx
 *
 */
@Getter
@Setter
public class ResponseWrapper<T> {
	private int code;
	private String message;
	private T data;

	public ResponseWrapper() {
		this.code = HttpStatus.OK.value();
		this.message = HttpStatus.OK.getReasonPhrase();
	}

	public ResponseWrapper(HttpStatus status) {
		this.code = status.value();
		this.message = status.getReasonPhrase();
	}

	public ResponseWrapper(T data) {

		if (isEmpty(data)) {
			this.code = HttpStatus.NO_CONTENT.value();
			this.message = HttpStatus.NO_CONTENT.getReasonPhrase();
		} else {
			this.code = HttpStatus.OK.value();
			this.message = HttpStatus.OK.getReasonPhrase();
			this.data = data;
		}
	}

	@SuppressWarnings("rawtypes")
	public boolean isEmpty(T data) {
		return data == null || data instanceof Collection && ((Collection) data).isEmpty();
	}

	public ResponseWrapper(HttpStatus status, T data) {
		this.code = status.value();
		this.message = status.getReasonPhrase();
		this.data = data;
	}

	@Override
	public String toString() {
		try {
			return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
		} catch (Exception e) {
			return super.toString();
		}
	}
}
