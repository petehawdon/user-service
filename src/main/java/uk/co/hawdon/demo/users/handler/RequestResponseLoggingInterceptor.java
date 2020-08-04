package uk.co.hawdon.demo.users.handler;

import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Slf4j
@Component
public class RequestResponseLoggingInterceptor extends HandlerInterceptorAdapter {

  @Override
  public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
      final Object handler) {
    final String correlationId = UUID.randomUUID().toString();
    request.setAttribute("correlationId", correlationId);
    log.info("{type: request, path: {}, correlationId: {}}", request.getRequestURI(), correlationId);
    return true;
  }

  @Override
  public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
      final Object handler, final Exception exception) {
    log.info("{type: response, path: {}, correlationId: {}, status: {}}", 
        request.getRequestURI(), request.getAttribute("correlationId"), response.getStatus());
  }
}
