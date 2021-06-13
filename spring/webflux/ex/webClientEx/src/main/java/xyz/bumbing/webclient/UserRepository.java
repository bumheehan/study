package xyz.bumbing.webclient;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

  Flux<User> findByName(String name);

  Mono<User> findByNameAndAge(String name, int age);
}
