package com.github.cdtft.microservice.reactor.subscriber;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

/**
 * @author : wangcheng
 * @date : 2020年07月08日 13:53
 */
public class SampleSubscriber<T> extends BaseSubscriber<T> {

    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        System.out.println("Subscribed");
        request(1);
    }

    @Override
    protected void hookOnNext(T value) {
        System.out.println(value);
        cancel();
    }
}
