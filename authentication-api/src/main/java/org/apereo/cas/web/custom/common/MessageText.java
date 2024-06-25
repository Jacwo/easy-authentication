package org.apereo.cas.web.custom.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Locale;

/**
 * 
 * International utils
 * 
 * @author wyx
 *
 */
@AllArgsConstructor
public class MessageText {


	private MessageSource messageSource;

	public String getMessage(int key, Locale local, Object... params) {
		Locale defaultLocal = new Locale("zh", "CN");
		if (local != null) {
			defaultLocal = local;
		}
		return messageSource.getMessage(String.valueOf(key), params, defaultLocal);
	}

	public String getMessage(int key, Locale local) {
		Locale defaultLocal = new Locale("zh", "CN");
		if (local != null) {
			defaultLocal = local;
		}
		return messageSource.getMessage(String.valueOf(key), null, defaultLocal);
	}
	public String getMessage(int key){
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		Locale local=request.getLocale();
		return messageSource.getMessage(String.valueOf(key), null, local);
	}
}
