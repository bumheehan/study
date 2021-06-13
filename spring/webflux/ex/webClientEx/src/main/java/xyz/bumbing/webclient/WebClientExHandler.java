package xyz.bumbing.webclient;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class WebClientExHandler {

  private BoardRepository boardRepository;
  private UserRepository userRepository;


  public WebClientExHandler(BoardRepository boardRepository, UserRepository userRepository) {
    this.boardRepository = boardRepository;
    this.userRepository = userRepository;
  }

  public Mono<ServerResponse> addUser(ServerRequest req) {
    Mono<User> body = req.bodyToMono(User.class);
    return ServerResponse.ok().body(userRepository.saveAll(body), User.class);
  }

  public Mono<ServerResponse> listUser(ServerRequest req) {
    return ServerResponse.ok().body(userRepository.findAll(), User.class);
  }

  public Mono<ServerResponse> getUser(ServerRequest req) {
    String name = req.pathVariable("name");
    return ServerResponse.ok().body(userRepository.findByName(name), User.class);
  }

  public Mono<ServerResponse> remove(ServerRequest req) {
    String name = req.pathVariable("name");
    return ServerResponse.ok().body(userRepository.deleteAll(userRepository.findByName(name)),
        User.class);
  }

  public Mono<ServerResponse> addBoard(ServerRequest req) {
    Mono<Board> body = req.bodyToMono(Board.class).doOnNext(board -> {
      userRepository.findByNameAndAge(board.getUser().getName(), board.getUser().getAge())
          .doOnNext(user -> board.setUser(user)).subscribe();
    });
    return ServerResponse.ok().body(boardRepository.saveAll(body), Board.class);
  }

  public Mono<ServerResponse> listBoard(ServerRequest req) {
    return ServerResponse.ok().body(boardRepository.findAll(), Board.class);
  }

  public Mono<ServerResponse> uploadView(ServerRequest req) {
    return ServerResponse.ok().contentType(MediaType.TEXT_HTML).render("upload");
  }

  public Mono<ServerResponse> upload(ServerRequest req) {
    return ServerResponse.ok()
        .body(req.multipartData().map(mu -> mu.get("files")).flatMapMany(Flux::fromIterable)
            .cast(FilePart.class).doOnNext(fp -> System.out.println(fp.filename()))
            .then(Mono.just("OK")), String.class);
  }

  public Mono<ServerResponse> download(ServerRequest req) {
    Resource resource = new ClassPathResource("application.properties");
    return ServerResponse.ok()
        .header("Content-Disposition", "attachment; filename=\"application.properties\"")
        .contentType(MediaType.APPLICATION_OCTET_STREAM).body(BodyInserters.fromResource(resource))
        .switchIfEmpty(Mono.empty());
  }
}
