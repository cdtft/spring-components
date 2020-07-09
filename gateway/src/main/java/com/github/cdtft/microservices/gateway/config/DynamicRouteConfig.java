package com.github.cdtft.microservices.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.github.cdtft.microservices.gateway.repository.NacosRouteDefinitionRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangcheng
 * @date : 2020年07月09日 14:35
 */
@Configuration
@ConditionalOnProperty(prefix = "cdtft.gateway.dynamicRoute", name = "enable", havingValue = "true")
public class DynamicRouteConfig {

    private final ApplicationEventPublisher applicationEventPublisher;

    public DynamicRouteConfig(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Configuration
    @ConditionalOnProperty(prefix = "cdtft.gateway.dynamicRoute", name = "dataType", havingValue = "nacos",
            matchIfMissing = true)
    public class NacosDynamicRoute {

        private final NacosConfigManager nacosConfigManager;

        public NacosDynamicRoute(NacosConfigManager nacosConfigManager) {
            this.nacosConfigManager = nacosConfigManager;
        }

        @Bean
        public NacosRouteDefinitionRepository nacosRouteDefinitionRepository() {
            return new NacosRouteDefinitionRepository(applicationEventPublisher, nacosConfigManager);
        }
    }

}
