package com.wwb.homeserver.config.ribbon;

import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author weibo
 */
@Slf4j
public class CustomRibbonLoadBalancerRule extends AbstractLoadBalancerRule {

    @Autowired
    NacosServiceDiscovery nacosServiceDiscovery;


    NacosServiceList nacosServiceList;

    private Object lockObj = new Object();

    /**
     * Concrete implementation should implement this method so that the configuration set via
     * {@link IClientConfig} (which in turn were set via Archaius properties) will be taken into consideration
     *
     * @param clientConfig
     */
    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    protected int chooseRandomInt(int serverCount) {
        return ThreadLocalRandom.current().nextInt(serverCount);
    }

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        super.setLoadBalancer(lb);
        if (lb instanceof BaseLoadBalancer) {
            BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) lb;
            String name = baseLoadBalancer.getName();
            synchronized (lockObj) {
                if (nacosServiceList == null) {
                    nacosServiceList = new NacosServiceList(nacosServiceDiscovery, name);
                }
            }
        }
    }

    @Override
    public Server choose(Object key) {
        ILoadBalancer loadBalancer = this.getLoadBalancer();
        if (loadBalancer instanceof BaseLoadBalancer) {
            List<Server> serverList = nacosServiceList.getServerList();
            if (!CollectionUtils.isEmpty(serverList)) {
                int index=chooseRandomInt(serverList.size());
                return serverList.get(index);
            }
        }
        List<Server> reachableServers = loadBalancer.getReachableServers();
        return reachableServers.get(0);
    }
}
