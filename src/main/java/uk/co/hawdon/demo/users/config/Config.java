package uk.co.hawdon.demo.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.co.hawdon.demo.users.handler.RequestResponseLoggingInterceptor;

/**
 * Spring Configuration beans.
 */
@Configuration
public class Config implements WebMvcConfigurer {

  /**
   * Override for addInterceptors.
   */
  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    registry.addInterceptor(new RequestResponseLoggingInterceptor()).addPathPatterns("/**");
  }
  

  /**
   * Creates the RestTemplate bean.
   *
   * @return Configured RestTemplate object
   */
  @Bean
  public RestTemplate restTemplate() {
    final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    ClientHttpRequestFactory clientHttpRequestFactory = new BufferingClientHttpRequestFactory(factory);
    return new RestTemplate(clientHttpRequestFactory);
  }
}
