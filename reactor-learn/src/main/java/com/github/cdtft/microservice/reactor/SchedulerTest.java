package com.github.cdtft.microservice.reactor;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author : wangcheng
 * @date : 2020年07月09日 10:25
 */
public class SchedulerTest {

    public static void publishOnTest() {
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);
        Flux<String> flux = Flux.range(1, 2)
                .map(i -> i + 10)
                .publishOn(s)
                .map(i -> "value " + i);
        new Thread(() -> flux.subscribe(System.out::println)).start();
    }

    public static void main(String[] args) {
        publishOnTest();
    }
}
