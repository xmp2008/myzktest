package cn.xmp.zkclient.config;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Value("${app.zk.addr}")
    private String zkAddr;
    @Value("${app.zk.connect.timeout}")
    private int zkConnectTimeout;

    @Bean
    public ZkClient buildZKClient() {
        return new ZkClient(zkAddr, zkConnectTimeout);
    }

}
