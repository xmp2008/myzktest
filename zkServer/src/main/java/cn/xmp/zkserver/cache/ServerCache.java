package cn.xmp.zkserver.cache;

import cn.xmp.zkserver.kit.ZKit;
import com.alibaba.fastjson.JSON;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Function: 服务器节点缓存
 *
 * @author crossoverJie
 *         Date: 2018/8/19 01:31
 * @since JDK 1.8
 */
@Slf4j
@Component
public class ServerCache {

    @Autowired
    private LoadingCache<String, String> cache;

    @Autowired
    private ZkClient zkClient;

    public void addCache(String key) {
        cache.put(key, key);
    }


    /**
     * 更新所有缓存/先删除 再新增
     *
     * @param currentChildren
     */
    public void updateCache(List<String> currentChildren) {
        cache.invalidateAll();
        for (String currentChild : currentChildren) {
            // currentChildren=ip-127.0.0.1:11212:9082 or 127.0.0.1:11212:9082
            String key ;
            if (currentChild.split("-").length == 2){
                key = currentChild.split("-")[1];
            }else {
                key = currentChild ;
            }
            addCache(key);
        }
    }


    /**
     * 获取所有的服务列表
     *
     * @return
     */
    public List<String> getServerList() {

        List<String> list = new ArrayList<>();

        if (cache.size() == 0) {
            List<String> allNode = getAllNode();
            for (String node : allNode) {
                String key = node.split("-")[1];
                addCache(key);
            }
        }
        for (Map.Entry<String, String> entry : cache.asMap().entrySet()) {
            list.add(entry.getKey());
        }
        return list;

    }

    /**
     * get all server node from zookeeper
     * @return
     */
    public List<String> getAllNode(){
        List<String> children = zkClient.getChildren("/route");
        log.info("Query all node =[{}] success.", JSON.toJSONString(children));
        return children;
    }

    /**
     * rebuild cache list
     */
    public void rebuildCacheList(){
        updateCache(getServerList()) ;
    }

}
