package com.github.cdtft.microservice.reactor;

import com.github.cdtft.microservice.reactor.subscriber.SampleSubscriber;
import reactor.core.publisher.Flux;

import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : wangcheng
 * @date : 2020年07月07日 17:15
 */
public class FluxTest {

    /**
     * 创建
     */
    public static void subscribeTest() {
        Flux<Integer> ints = Flux.range(1, 3);
        ints.subscribe(System.out::println);
    }

    /**
     * 异常处理
     */
    public static void errorConsumerTest() {
        Flux<Integer> ints = Flux.range(1, 4)
                .map(i -> {
                    if (i <= 3) {
                        return i;
                    }
                    throw new RuntimeException();
                });
        ints.subscribe(System.out::println, error -> System.err.println("error: " + error));
    }

    /**
     * 完成标志
     */
    public static void completeConsumerTest() {
        Flux<Integer> ints = Flux.range(1, 4);
        ints.subscribe(System.out::println, error -> System.err.println("error: " + error), () -> System.out.println(
                "Done"));
    }

    /**
     * request限制
     */
    public static void requestTest() {
        Flux<Integer> ints = Flux.range(1, 4);
        ints.subscribe(System.out::println, System.err::println, () -> System.out.println("Done"),
                sub -> sub.request(3));
    }

    /**
     * 自定义Subscribe
     */
    private static void sampleSubscriberTest() {
        SampleSubscriber<Integer> ss = new SampleSubscriber<>();
        Flux<Integer> ints = Flux.range(1, 4);

        ints.subscribe(System.out::println, System.err::println, () -> System.out.println("Done"
        ), s -> s.request(10));

        ints.subscribe(ss);
    }

    /**
     * 生成状态值
     */
    public static void generateTest() {
        Flux<String> flux = Flux.generate(() -> 0, (status, sink) -> {
            sink.next("3 x " + status + " = " + 3 * status);
            if (status == 10) {
                sink.complete();
            }
            return status + 1;
        });
        flux.subscribe(System.out::println);

        Flux<String> atomicLongFlux = Flux.generate(AtomicLong::new, (state, sink) -> {
            long i = state.getAndIncrement();
            sink.next("3 x " + i + " = " + 3 * i);
            if (i == 10) {
                sink.complete();
            }
            return state;
        }, (state) -> System.out.println("state: " + state));
        atomicLongFlux.subscribe(System.out::println);
    }

    public static void handleTest() {
        Flux<String> alphabet = Flux.just(-1, 30, 13, 9, 20)
                .handle((i, sink) -> {
                    String letter = alphabet(i);
                    if (letter != null) {
                        sink.next(letter);
                    }
                });
        alphabet.subscribe(System.out::println);
    }

    private static String alphabet(int letterNumber) {
        if (letterNumber < 1 || letterNumber > 26) {
            return null;
        }
        int letterIndexAscii = 'A' + letterNumber - 1;
        return "" + (char) letterIndexAscii;
    }

    public static void main(String[] args) {
        handleTest();
    }
}
