package br.com.virilcorp.io.system.exceptions;

import java.io.IOException;

public final class UnknowSystemException extends IOException {

	private static final long serialVersionUID = -5661186531977188850L;

    public UnknowSystemException() {
        super();
    }

    public UnknowSystemException(String message) {
        super(message);
    }

    public UnknowSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknowSystemException(Throwable cause) {
        super(cause);
    }
}
