
package org.apereo.cas.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Base controller
 * 
 * @author wyx
 *
 */

@Slf4j
public class BaseController {

	@Autowired
	MessageText messageText;


/***
	 * { "code": 204, "message": "No Content", "data": null }
	 * 
	 * @param exception
	 * @param request
	 * @return 204
 */

	@ExceptionHandler({ NullContentException.class })
	@ResponseBody
	public ResponseWrapper<ExceptionData> nullContentExceptionHandler(BaseException exception,
			HttpServletRequest request) {
		ResponseWrapper<ExceptionData> dataWrapper = new ResponseWrapper<>(HttpStatus.NO_CONTENT);
		String message = exception.getLocalizedMessage();
		dataWrapper.setData(null);
		log.warn("Query content is null. Url is {}; message is {};exception is {}", request.getRequestURI(), message,
				exception);
		return dataWrapper;
	}


/***
	 * { "code": 412, "message": "Precondition Failed", "data": { "errorCode":
	 * 100001, "errorMessage": "No user found for conditions.", "details": [
	 * "Required field userid not found." ] } }
	 * 
	 * @param exception
	 * @param request
	 * @return 412
 */

	@ExceptionHandler({ ServiceWarningException.class })
	@ResponseBody
	public ResponseWrapper<ExceptionData> serviceWarningExceptionHandler(BaseException exception,
			HttpServletRequest request) {
		ResponseWrapper<ExceptionData> dataWrapper = new ResponseWrapper<>(HttpStatus.PRECONDITION_FAILED);
		String message = exception.getLocalizedMessage();

		if (exception.getCode() != 0) {
			message = messageText.getMessage(exception.getCode(), request.getLocale(), exception.getParams());
		}

		List<String> details = new ArrayList<>();
		Collection<?> exceptionDetails = exception.getDetails();
		if (!exceptionDetails.isEmpty()) {
			for (Object object : exceptionDetails) {
				if (object instanceof Integer) {
					int errorCode = (Integer) object;
					details.add(messageText.getMessage(errorCode, request.getLocale()));
				}
			}
		}

		dataWrapper.setData(new ExceptionData(exception.getCode(), message, details));
		return dataWrapper;
	}


/**
	 * 
	 * { "code": 500, "message": "Internal Server Error", "data": { "cause":
	 * "Exception in thread main java.lang.NullPointerException at
	 * se.citerus.dddsample.Main.main(Main.java: 6)" } }
	 * 
	 * @param exception
	 * @param request
	 * @return
	 * @throws IOException
	 */

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseWrapper<ExceptionData> exceptionHandler(Exception exception, HttpServletRequest request) {
		ResponseWrapper<ExceptionData> dataWrapper = new ResponseWrapper<>(
				HttpStatus.INTERNAL_SERVER_ERROR);
		String message = exception.getLocalizedMessage();
		String cause = ExceptionUtils.getStackTrace(exception);
		dataWrapper.setData(new ExceptionData(null, message, cause));
		log.warn("Query content is null. Url is {}; message is {};exception is {}", request.getRequestURI(), message,
				exception);
		return dataWrapper;
	}


/**
	 *
	 * { "code": 500, "message": "Internal Server Error", "data": { "cause":
	 * "Exception in thread main java.lang.NullPointerException at
	 * se.citerus.dddsample.Main.main(Main.java: 6)" } }
	 *
	 * @param exception
	 * @param request
	 * @return
	 * @throws IOException
	 */

	@ExceptionHandler(ClientForbiddenException.class)
	@ResponseBody
	public ResponseWrapper<ExceptionData> requestRejectionHandler(Exception exception, HttpServletRequest request) {
		ResponseWrapper<ExceptionData> dataWrapper = new ResponseWrapper<>(
                HttpStatus.UNAUTHORIZED);
		String message = exception.getLocalizedMessage();
		dataWrapper.setData(new ExceptionData(null, message));
		return dataWrapper;
	}

}

