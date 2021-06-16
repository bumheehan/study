package xyz.bumbing.reactor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

@SpringBootTest
class ReactorStudyApplicationTests {

    /** 데이터 생성 */
    //객체로 부터
    @Test
    void createAFlux_just() {
	Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Grape", "Banana", "Strawberry");
	StepVerifier.create(fruitFlux).expectNext("Apple").expectNext("Orange").expectNext("Grape").expectNext("Banana")
		.expectNext("Strawberry").verifyComplete();
    }

    // Array, Stream, Iterable
    @Test
    void createAFlux_fromArray() {
	String[] fruits = { "Apple", "Orange", "Grape", "Banana", "Strawberry" };
	Flux<String> fruitFlux = Flux.fromArray(fruits);
	StepVerifier.create(fruitFlux).expectNext("Apple").expectNext("Orange").expectNext("Grape").expectNext("Banana")
		.expectNext("Strawberry").verifyComplete();
	//fromStream, fromIterable 도있음
    }

    // range Integer
    @Test
    void createAFlux_range() {
	Flux<Integer> range = Flux.range(0, 5); // 0부터 4까지 , [0,4] , [4,5)
	StepVerifier.create(range).expectNext(0).expectNext(1).expectNext(2).expectNext(3).expectNext(4)
		.verifyComplete();
    }

    // integer Long, 매초마다 생성,딜레이 있어서 주석처리
    //    @Test
    void createAFlux_interval() {
	Flux<Long> interval = Flux.interval(Duration.ofSeconds(1)).take(5); //5초
	StepVerifier.create(interval).expectNext(0L).expectNext(1L).expectNext(2L).expectNext(3L).expectNext(4L)
		.verifyComplete();
    }

    /** 결합 */
    //  merge, 딜레이 있어서 주석처리
    //    @Test
    void mergeFluxes() {
	Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(500));
	Flux<String> flux2 = Flux.just("1", "2", "3").delaySubscription(Duration.ofMillis(250))
		.delayElements(Duration.ofMillis(500));
	Flux<String> mergeFlux = Flux.merge(flux1, flux2);
	StepVerifier.create(mergeFlux).expectNext("A").expectNext("1").expectNext("B").expectNext("2").expectNext("C")
		.expectNext("3").verifyComplete();
    }

    // zip , merge는 완벽하게 번갈아간다는것을 보장하지 않음 
    @Test
    void zipFluxes() {
	Flux<String> flux1 = Flux.just("A", "B", "C");
	Flux<String> flux2 = Flux.just("1", "2", "3");
	Flux<Tuple2<String, String>> zip1 = Flux.zip(flux1, flux2);
	StepVerifier.create(zip1).expectNextMatches(p -> p.getT1().equals("A") && p.getT2().equals("1"))
		.expectNextMatches(p -> p.getT1().equals("B") && p.getT2().equals("2"))
		.expectNextMatches(p -> p.getT1().equals("C") && p.getT2().equals("3")).verifyComplete();

	// Tuple2 말고 원하는 객체도 가능
	Flux<String> zip2 = Flux.zip(flux1, flux2, (c, f) -> c + "<->" + f);
	StepVerifier.create(zip2).expectNext("A<->1").expectNext("B<->2").expectNext("C<->3").verifyComplete();
    }

    // first, 먼저들어온 flux만 추출 
    @Test
    void firstFluxes() {
	Flux<String> fast = Flux.just("A", "B", "C");
	Flux<String> slow = Flux.just("1", "2", "3").delaySequence(Duration.ofMillis(100));
	Flux<String> first = Flux.firstWithSignal(slow, fast);
	StepVerifier.create(first).expectNext("A").expectNext("B").expectNext("C").verifyComplete();
    }

    /** 변환과 필터링*/
    //원소 skip
    @Test
    void skipAFew() {
	Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5).skip(3);
	StepVerifier.create(flux).expectNext(4).expectNext(5).verifyComplete();
    }

    //시간 skip, delay element1 delay element2 delay element3 delay element4 순서로 delay 4면 element 4부터 나옴
    //    @Test
    void skipDurationAFew() {
	Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofMillis(100))
		.skip(Duration.ofMillis(400));
	StepVerifier.create(flux).expectNext(4).expectNext(5).verifyComplete();
    }

    //take, skip과 반대, 지정된 원수 만큼 나옴
    @Test
    void takeAFew() {
	Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5).take(3);
	StepVerifier.create(flux).expectNext(1).expectNext(2).expectNext(3).verifyComplete();
    }

    //    @Test
    void takeDurationAFew() {
	Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofMillis(100))
		.take(Duration.ofMillis(400));
	StepVerifier.create(flux).expectNext(1).expectNext(2).expectNext(3).verifyComplete();
    }

    //Filter 중요
    @Test
    void filter() {
	Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5).filter(s -> s > 3);
	StepVerifier.create(flux).expectNext(4).expectNext(5).verifyComplete();
    }

    //중복제거, 중복되면 앞에 원소 추출
    @Test
    void distict() {
	Flux<Integer> flux = Flux.just(1, 2, 3, 1, 3).distinct();
	StepVerifier.create(flux).expectNext(1, 2, 3).verifyComplete();
    }

    //map , 동기 프로세스라 비동기사용하려면 flatmap 사용
    @Test
    void map() {
	Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5).map(s -> s + 1);
	StepVerifier.create(flux).expectNext(2, 3, 4, 5, 6).verifyComplete();
    }

    //    @Test
    void flatMap() {

	//flux(1,5) => (비동기 flux1) (비동기 flux2) (비동기 flux3) (비동기 flux4) (비동기 flux5)
	Flux.range(1, 5).flatMap(a -> Flux.just(a, a + 100).subscribeOn(Schedulers.parallel())).subscribe(
		s -> System.out.println(String.format("%d , Thread : %s", s, Thread.currentThread().getName())));

	Flux.range(6, 10).flatMap(a -> Flux.just(a, a + 100).subscribeOn(Schedulers.single())).subscribe(
		s -> System.out.println(String.format("%d , Thread : %s", s, Thread.currentThread().getName())));
    }

    //    @Test
    void streamPerf() {
	//생성 테스트
	long start = System.nanoTime();
	System.out.println("Reactor 10000000 생성 테스트");
	Mono<List<Integer>> range = Flux.range(0, 10000000).collectList();
	List<Integer> reactorList = range.block();
	start = stopPrint(start);
	System.out.println("intStream 10000000 생성 테스트");
	List<Integer> streamList = IntStream.range(0, 10000000).boxed().collect(Collectors.toList());
	start = stopPrint(start);

	System.out.println("for 10000000 생성 테스트");
	List<Integer> forList = new ArrayList<>();
	for (int i = 0; i < 10000000; i++) {
	    forList.add(i);
	}
	start = stopPrint(start);
	//더하기 테스트
	//아래꺼는 매우느림
	//	System.out.println("더하기 Flux flatMap subscribeOn Schedulers.parallel 테스트");
	//	reactorList = Flux.range(0, 10000000).flatMap(s -> Mono.just(s + 1).subscribeOn(Schedulers.parallel()))
	//		.collectList().block();
	//	start = stopPrint(start);

	System.out.println("더하기 Flux map subscribeOn Schedulers.parallel 테스트");
	reactorList = Flux.range(0, 100000000).subscribeOn(Schedulers.parallel()).map(s -> s + 1).collectList().block();
	start = stopPrint(start);
	System.out.println("더하기 Flux map  테스트");
	reactorList = Flux.range(0, 100000000).map(s -> s + 1).collectList().block();
	start = stopPrint(start);
	System.out.println("더하기 stream map  테스트");
	streamList = IntStream.range(0, 100000000).map(s -> s + 1).boxed().collect(Collectors.toList());
	start = stopPrint(start);
	System.out.println("더하기 for  테스트");
	forList = new ArrayList<>();
	for (int i = 0; i < 100000000; i++) {
	    forList.add(i + 1);
	}
	start = stopPrint(start);
    }

    long stopPrint(long start) {
	long stop = System.nanoTime();
	System.out.println((stop - start) / 1000000 + "ms");
	return stop;
    }

    @Test
    public void buffer() {
	Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");
	Flux<List<String>> buffer = fruitFlux.buffer(3);

	StepVerifier.create(buffer).expectNext(List.of("apple", "orange", "banana"))
		.expectNext(List.of("kiwi", "strawberry")).verifyComplete();
    }

    //하나의 리스트
    @Test
    public void collectList() {
	Flux<String> fruitFlux = Flux.just("apple", "orange", "banana", "kiwi", "strawberry");
	Mono<List<String>> collectList = fruitFlux.collectList();
	StepVerifier.create(collectList).expectNext(List.of("apple", "orange", "banana", "kiwi", "strawberry"))
		.verifyComplete();
    }

    //map
    @Test
    public void collectMap() {
	Flux<String> flux = Flux.just("aaa", "bbb", "ccc", "aaaa", "cv");
	//키셋팅
	Mono<Map<Character, String>> collectMap = flux.collectMap(a -> a.charAt(0));

	//뒤에꺼로 덮어씌움
	StepVerifier.create(collectMap).expectNextMatches(
		map -> map.size() == 3 && map.get('a') == "aaaa" && map.get('b') == "bbb" && map.get('c') == "cv")
		.verifyComplete();
    }

    //all
    @Test
    public void all() {
	Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");
	Mono<Boolean> hasAMono = animalFlux.all(s -> s.contains("a"));
	StepVerifier.create(hasAMono).expectNext(true).verifyComplete();
    }

    //any
    @Test
    public void any() {
	Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");
	Mono<Boolean> hasAMono = animalFlux.any(s -> s.contains("t"));
	StepVerifier.create(hasAMono).expectNext(true).verifyComplete();
    }

}
