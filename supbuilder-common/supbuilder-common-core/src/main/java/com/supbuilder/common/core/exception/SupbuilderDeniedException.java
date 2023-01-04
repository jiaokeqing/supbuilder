
package com.supbuilder.common.core.exception;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class SupbuilderDeniedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SupbuilderDeniedException(String message) {
		super(message);
	}

	public SupbuilderDeniedException(Throwable cause) {
		super(cause);
	}

	public SupbuilderDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	public SupbuilderDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
