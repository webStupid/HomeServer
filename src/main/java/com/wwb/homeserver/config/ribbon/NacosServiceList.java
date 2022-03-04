package com.wwb.homeserver.config.ribbon;

import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * 监听nacos服务变化
 *
 * @author xx
 */
@Slf4j
class NacosServiceList {

    private NacosServiceDiscovery nacosServiceDiscovery;

    private String serviceName;

    private volatile List<Server> serverList;

    private Object lockObj = new Object();
    private boolean hadInit=false;

    public NacosServiceList(NacosServiceDiscovery nacosServiceDiscovery, String serviceName) {
        this.nacosServiceDiscovery = nacosServiceDiscovery;
        this.serviceName = parserServiceName(serviceName);
        this.serverList = Collections
                .synchronizedList(new ArrayList<>());
        resetServer();
        addListener();
    }

    private String parserServiceName(String name) {
        try {
            Optional<String> optional = nacosServiceDiscovery.getServices().stream().filter(c -> c.equalsIgnoreCase(name)).findFirst();
            if (optional.isPresent()) {
                return optional.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public List<Server> getServerList() {
        synchronized (lockObj) {
            if (!hadInit) {
                resetServer();
            }
            return serverList;
        }
    }

    private void resetServer() {
        synchronized (lockObj) {
            try {
                List<ServiceInstance> serviceInstances = nacosServiceDiscovery.getInstances(serviceName);
                if (!CollectionUtils.isEmpty(serviceInstances)) {
                    serverList.removeIf(item -> notContainServer(serviceInstances, item));
                    serviceInstances.forEach(item -> {
                        boolean contain = serverList.stream().anyMatch(c -> c.getHost().equals(item.getHost()) && c.getPort() == item.getPort());
                        if (!contain) {
                            serverList.add(new Server(item.getHost(), item.getPort()));
                        }
                    });
                } else {
                    serverList.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            hadInit=true;
        }
    }

    private boolean notContainServer(List<ServiceInstance> serviceInstances, Server server) {
        return !serviceInstances.stream().anyMatch(c -> c.getHost().equals(server.getHost()) && c.getPort() == server.getPort());
    }

    private void addListener() {
        try {
            Method method = NacosServiceDiscovery.class.getDeclaredMethod("namingService");
            if (method != null) {
                method.setAccessible(true);
                NamingService namingService = (NamingService) method.invoke(nacosServiceDiscovery);
                namingService.subscribe(serviceName, event -> {
                    NamingEvent namingEvent = (NamingEvent) event;
                    resetServer();
                    log.info("event:{}--{}", serviceName, namingEvent.getServiceName());
                });
                log.info("NamingService");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
