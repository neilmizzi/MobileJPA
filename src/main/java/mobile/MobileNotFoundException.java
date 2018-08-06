package mobile;

import java.lang.RuntimeException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MobileNotFoundException extends RuntimeException
{
  public MobileNotFoundException(String exception)
  {
    super(exception);
  }
}
