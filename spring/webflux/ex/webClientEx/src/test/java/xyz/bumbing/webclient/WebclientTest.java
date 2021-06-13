package xyz.bumbing.webclient;

import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

class WebclientTest {


  private static final Logger log = LoggerFactory.getLogger(WebclientTest.class);
  WebClient webClient = WebClient.create("http://localhost:8080");

  @Test
  @Disabled
  void addUser() {
    User user = new User();
    user.setName("한");
    user.setAge(32);
    webClient.post().uri("/user").bodyValue(user).retrieve().bodyToMono(String.class)
        .doOnNext(log::info).block();
  }

  @Test
  @Disabled
  void listUser() {
    webClient.get().uri("/user").retrieve().bodyToMono(String.class).doOnNext(log::info).block();
  }

  @Test
  @Disabled
  void upload() {
    MultipartBodyBuilder builder = new MultipartBodyBuilder();
    builder.part("files", new ClassPathResource("static/test1.txt"));
    builder.part("files", new ClassPathResource("static/test2.txt"));

    webClient.post().uri("/upload").headers(s -> s.setContentType(MediaType.MULTIPART_FORM_DATA))
        .body(BodyInserters.fromMultipartData(builder.build())).retrieve().bodyToMono(String.class)
        .doOnNext(log::info).block();
  }

  @Test
  void download() {

    webClient.get().uri("/download").exchange().flatMap(response -> {
      String filename = response.headers().asHttpHeaders().getContentDisposition().getFilename();
      Flux<DataBuffer> dataBufferFlux = response.bodyToFlux(DataBuffer.class);
      if (filename == null) {
        // 서버에서 ContentDisposition 셋팅 안할 시 파일 이름 못가져옴
        filename = "bb";
      }
      return DataBufferUtils.write(dataBufferFlux, Path.of("download/" + filename),
          StandardOpenOption.CREATE);
    }).block();

  }



}
