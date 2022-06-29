package cn.xmp.zkserver.kit;

import cn.xmp.zkserver.config.AppConfiguration;
import cn.xmp.zkserver.util.SpringBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class RegistryZK implements Runnable {

    private ZKit zKit;

    private AppConfiguration appConfiguration ;

    private String ip;
    private int cimServerPort;
    private int httpPort;

    public RegistryZK(String ip, int cimServerPort, int httpPort) {
        this.ip = ip;
        this.cimServerPort = cimServerPort;
        this.httpPort = httpPort ;
        zKit = SpringBeanFactory.getBean(ZKit.class) ;
        appConfiguration = SpringBeanFactory.getBean(AppConfiguration.class) ;
    }

    @Override
    public void run() {

        //创建父节点
        zKit.createRootNode();

        //是否要将自己注册到 ZK
        if (appConfiguration.isZkSwitch()){
            String path = appConfiguration.getZkRoot() + "/ip-" + ip + ":" + cimServerPort + ":" + httpPort;
            zKit.createNode(path);
            log.info("Registry zookeeper success, msg=[{}]", path);
        }

        //注册监听服务
        zKit.subscribeEvent(appConfiguration.getZkRoot());
    }
}