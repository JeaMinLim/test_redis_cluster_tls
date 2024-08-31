package com.example.jmlim.test_redis_cluster_tls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.models.partitions.Partitions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class Controller {

    @RequestMapping("/")
    public String index() {
        return "This is index page";
    }
    
    @RequestMapping("/connect")
    public String connectRedis() {
        List<RedisURI> redisURIs = Arrays.asList(
            RedisURI.Builder.redis("127.0.0.1", 6379)
                            .withSsl(true)
                            .withVerifyPeer(false)
                            .build(),
            RedisURI.Builder.redis("127.0.0.1", 6380)
                            .withSsl(true)
                            .withVerifyPeer(false)
                            .build(),
            RedisURI.Builder.redis("127.0.0.1", 6381)
                            .withSsl(true)
                            .withVerifyPeer(false)
                            .build(),
            RedisURI.Builder.redis("127.0.0.1", 6382)
                            .withSsl(true)
                            .withVerifyPeer(false)
                            .build(),
            RedisURI.Builder.redis("127.0.0.1", 6383)
                            .withSsl(true)
                            .withVerifyPeer(false)
                            .build(),
            RedisURI.Builder.redis("127.0.0.1", 6384)
                            .withSsl(true)
                            .withVerifyPeer(false)
                            .build()
        );

        RedisClusterClient clusterClient = RedisClusterClient.create(redisURIs);
        StatefulRedisClusterConnection<String, String> connection = clusterClient.connect();
        RedisAdvancedClusterCommands<String, String> syncCommands = connection.sync();

        String raw_result = syncCommands.clusterNodes();

        connection.close();
        clusterClient.shutdown();

        return raw_result;
    }    
}

