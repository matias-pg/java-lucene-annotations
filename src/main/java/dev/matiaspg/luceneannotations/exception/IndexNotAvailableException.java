package dev.matiaspg.luceneannotations.exception;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@StandardException
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class IndexNotAvailableException extends RuntimeException {
}
