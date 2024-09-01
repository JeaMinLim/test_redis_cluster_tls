package com.example.jmlim.test_redis_cluster_tls;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.cluster.RedisClusterClient;

import java.util.Arrays;
import java.util.List;

@RestController
public class Controller {

    @Autowired
    private RedisClusterClient redisClusterClient;

    @RequestMapping("/")
    public String index() {
        return 
        "<h1>This is index page <h1> <br /> <p> /connect/without or /connect/verify </p>";
    }
    
    @RequestMapping("/connect/without")
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

        String rawResult = syncCommands.clusterNodes();

        connection.close();
        clusterClient.shutdown();

        return rawResult;
    }   

    @RequestMapping("/connect/verify")
    public String connectRedisVerify() {
        RedisAdvancedClusterCommands<String, String> commands = redisClusterClient.connect().sync();
        String clusterNodes = commands.clusterNodes();
        redisClusterClient.shutdown();
        return clusterNodes;
    }
            
}

