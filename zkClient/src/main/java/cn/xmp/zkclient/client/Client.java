package cn.xmp.zkclient.client;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Title:
 * @BelongProjecet myzktest
 * @BelongPackage cn.xmp.zkclient.client
 * @Description:
 * @Author: xmp
 * @Date: 2022/6/29 9:34
 */
@Component
public class Client {

    @Value("${app.zk.root}")
    private String zkRoot;
    @Value("${server.port}")
    private int httpPort;
    @Autowired
    private ZkClient zkClient;

    @PostConstruct
    private void registerTOZK() throws UnknownHostException {
        //客户端注册到zk,测试zk事件监听
        String path = zkRoot + "/ip-" + InetAddress.getLocalHost().getHostAddress() + ":" + httpPort;
        zkClient.createEphemeral(path);
    }
}
