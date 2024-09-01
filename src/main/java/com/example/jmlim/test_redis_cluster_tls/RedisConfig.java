package com.example.jmlim.test_redis_cluster_tls;

import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.SslOptions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
public class RedisConfig {

    static {
        //String trustStorePath = RedisConfig.class.getClassLoader().getResource("cert/redis.jks").getPath();
        String trustStorePath = RedisConfig.class.getClassLoader().getResource("cert/client.jks").getPath();
        System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", "redis1");
    }
    
    @Bean(destroyMethod = "shutdown")
    public ClientResources clientResources() {
        return DefaultClientResources.create();
    }

    @Bean
    public RedisClusterClient redisClusterClient(ClientResources clientResources) {
        RedisURI node1 = RedisURI.create("rediss://127.0.0.1:6379");
        RedisURI node2 = RedisURI.create("rediss://127.0.0.1:6380");
        RedisURI node3 = RedisURI.create("rediss://127.0.0.1:6381");
        RedisURI node4 = RedisURI.create("rediss://127.0.0.1:6382");
        RedisURI node5 = RedisURI.create("rediss://127.0.0.1:6383");
        RedisURI node6 = RedisURI.create("rediss://127.0.0.1:6384");

        SslOptions sslOptions = SslOptions.builder()
                .jdkSslProvider()
                .build();

        @SuppressWarnings("deprecation")
        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
                .topologyRefreshOptions(ClusterTopologyRefreshOptions.builder()
                        .enableAdaptiveRefreshTrigger(ClusterTopologyRefreshOptions.RefreshTrigger.MOVED_REDIRECT, ClusterTopologyRefreshOptions.RefreshTrigger.PERSISTENT_RECONNECTS)
                        .adaptiveRefreshTriggersTimeout(30, TimeUnit.SECONDS)
                        .build())
                .sslOptions(sslOptions)
                .build();

        RedisClusterClient clusterClient = RedisClusterClient.create(clientResources, Arrays.asList(node1, node2, node3, node4, node5, node6));
        clusterClient.setOptions(clusterClientOptions);
                                    
        return clusterClient;
    }
}
