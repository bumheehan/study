package xyz.bumbing.webclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

@EnableWebFlux
@Configuration
public class WebClientExRouter {

  @Bean
  RouterFunction<?> routes(WebClientExHandler handler) {
    return RouterFunctions.route(RequestPredicates.POST("/user"), handler::addUser)
        .andRoute(RequestPredicates.GET("/user"), handler::listUser)
        .andRoute(RequestPredicates.GET("/user/{name}"), handler::getUser)
        .andRoute(RequestPredicates.DELETE("/user"), handler::remove)
        .andRoute(RequestPredicates.GET("/board"), handler::listBoard)
        .andRoute(RequestPredicates.GET("/upload"), handler::uploadView)
        .andRoute(RequestPredicates.POST("/upload")
            .and(RequestPredicates.accept(MediaType.MULTIPART_FORM_DATA)), handler::upload)
        .andRoute(RequestPredicates.GET("/download"), handler::download);
  }


}
