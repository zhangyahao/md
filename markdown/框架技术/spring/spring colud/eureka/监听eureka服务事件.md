1. 在Eureka服务中进行检测通知，Eureka中提供了事件监听的方式来支持扩展。

      1.  EurekaInstanceCanceledEvent 服务下线事件
        
      2.  EurekaInstanceRegisteredEvent 服务注册事件
          
      3.  EurekaInstanceRenewedEvent 服务续约事件
        
      4.  EurekaRegistryAvailableEvent Eureka注册中心启动事件
        
      5.  EurekaServerStartedEvent Eureka Server启动事件


````java
import com.netflix.appinfo.InstanceInfo;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;/**
 * eureka事件监听
 * 例如：用于监听eureka服务停机通知
 *
 */
@Component
public class EurekaStateChangeListener {    
     /**
     * EurekaInstanceCanceledEvent 服务下线事件
     * @param event
     */
    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        System.out.println(event.getServerId() + "\t" + event.getAppName() + " 服务下线");
    }    /**
     * EurekaInstanceRegisteredEvent 服务注册事件
     * @param event
     */
    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        System.out.println(instanceInfo.getAppName() + "进行注册");
    }    /**
     * EurekaInstanceRenewedEvent 服务续约事件
     * @param event
     */
    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        System.out.println(event.getServerId() + "\t" + event.getAppName() + " 服务进行续约");
    }    /**
     * EurekaRegistryAvailableEvent Eureka注册中心启动事件
     * @param event
     */
    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        System.out.println("注册中心 启动");
    }    /**
     * EurekaServerStartedEvent Eureka Server启动事件
     * @param event
     */
    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        System.out.println("Eureka Server 启动");
    }
}


````