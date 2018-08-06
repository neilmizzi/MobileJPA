package mobile;

import java.lang.RuntimeException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MobileConflictException extends RuntimeException
{
  public MobileConflictException(String exception)
  {
    super(exception);
  }
}
