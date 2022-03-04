package com.wwb.homeserver.config.ribbon;

import com.netflix.loadbalancer.IRule;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;

@RibbonClients(defaultConfiguration = CustomLoadBalancerConfiguration.CustomLoadBalancerConfigurationImpl.class)
public class CustomLoadBalancerConfiguration {

    public static class CustomLoadBalancerConfigurationImpl {
        @Bean
        public IRule loadbalancerRule() {
            return new CustomRibbonLoadBalancerRule();
        }
    }
}
