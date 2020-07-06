package com.github.cdtft.microservices.gateway.repository;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.utils.JSONUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * 动态读取nacos
 *
 * @author : wangcheng
 * @date : 2020年07月06日 14:58
 */
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {

    private final static Logger LOG = LoggerFactory.getLogger(NacosRouteDefinitionRepository.class);

    private static final String MICRO_SERVICE_DATA_ID = "micro-routes";

    private static final String MICRO_SERVICE_GROUP_ID = "micro-gateway";

    private final ApplicationEventPublisher applicationEventPublisher;

    private final NacosConfigManager nacosConfigManager;

    public NacosRouteDefinitionRepository(ApplicationEventPublisher applicationEventPublisher,
                                          NacosConfigManager nacosConfigManager) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.nacosConfigManager = nacosConfigManager;
        this.addListener();
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> routeDefinitionList = Lists.newArrayList();
        try {
            String content = nacosConfigManager.getConfigService().getConfig(MICRO_SERVICE_DATA_ID,
                    MICRO_SERVICE_DATA_ID, 5000);
            LOG.info("get route config from nacos content : {}", content);
            if (StringUtils.isNotBlank(content)) {
                routeDefinitionList = JSONObject.parseArray(content, RouteDefinition.class);
            }
            return Flux.fromIterable()
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }

    private void addListener() {
        try {
            nacosConfigManager.getConfigService().addListener(MICRO_SERVICE_DATA_ID, MICRO_SERVICE_GROUP_ID,
                    new Listener() {
                        @Override
                        public Executor getExecutor() {
                            return null;
                        }

                        @Override
                        public void receiveConfigInfo(String s) {
                            LOG.info("add listener receiveConfigInfo : {}", s);
                            applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
                        }
                    });
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }
}
