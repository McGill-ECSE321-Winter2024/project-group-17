package ca.mcgill.ecse321.SportCenterManager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@OpenAPIDefinition
@Configuration
public class SpringDocConfig {
  @Bean
  public OpenAPI baseOpenAPI(){
    return new OpenAPI().info(new Info().title("Sport Center Manager").version("1.0.0").description("API Documentation"));
  }
}
