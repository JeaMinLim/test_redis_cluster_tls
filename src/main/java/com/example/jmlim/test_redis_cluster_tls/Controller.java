package com.example.jmlim.test_redis_cluster_tls;

import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

@RestController
public class Controller {

    @RequestMapping("/")
    public String index() {
        String _message = "This is index page";
        return _message;
    }
    
    @RequestMapping("/connect")

    public String connectRedis() {

        RedisURI node1 = RedisURI.Builder.redis("localhost", 6379).withSsl(true).build();
        RedisURI node2 = RedisURI.Builder.redis("localhost", 6380).withSsl(true).build();
        RedisURI node3 = RedisURI.Builder.redis("localhost", 6381).withSsl(true).build();
        RedisURI node4 = RedisURI.Builder.redis("localhost", 6382).withSsl(true).build();
        RedisURI node5 = RedisURI.Builder.redis("localhost", 6383).withSsl(true).build();
        RedisURI node6 = RedisURI.Builder.redis("localhost", 6384).withSsl(true).build();

        RedisClusterClient clusterClient = RedisClusterClient.create(Arrays.asList(node1, node2, node3, node4, node5, node6));
        StatefulRedisClusterConnection<String, String> connection = clusterClient.connect();
        RedisAdvancedClusterCommands<String, String> syncCommands = connection.sync();

        String clusterNodes = syncCommands.clusterNodes();
        System.out.println("Cluster Nodes: \n" + clusterNodes);

        connection.close();
        clusterClient.shutdown();

        return "Cluster Nodes: \n" + clusterNodes;
    }
    
}
