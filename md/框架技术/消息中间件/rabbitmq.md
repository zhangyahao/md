####RabbitMQ简介
1. RabbitMQ是一个在AMQP基础上完整的，可复用的企业消息系统。它可以用于大型软件系统各个模块之间的高效通信，支持高并发，支持可扩展。使用Erlang语言编写。

2. 相关术语<br>
    1.  Broker：简单来说就是消息队列服务器实体。
    2.  Exchange：消息交换机，它指定消息按什么规则，路由到哪个队列。
    3.  Queue：消息队列载体，每个消息都会被投入到一个或多个队列。
    4.  Binding：绑定，它的作用就是把exchange和queue按照路由规则绑定起来。
    5.  Routing Key：路由关键字，exchange根据这个关键字进行消息投递。
    6.  vhost：虚拟主机，一个broker里可以开设多个vhost，用作不同用户的权限分离。
    7.  producer：消息生产者，就是投递消息的程序。
    8.  consumer：消息消费者，就是接受消息的程序。
    9.  channel：消息通道，在客户端的每个连接里，可建立多个channel，每个channel代表一个会话任务。
    
      ![模式](http://upload-images.jianshu.io/upload_images/683118-b22270d646a5aeef.png?imageMogr2/auto-orient/strip)
  
 
3. 主要模式   
    1. 生产者 消费者模式<br>
     生产者
     ```aidl
     package com.rabbitmq.test.T_helloworld;
      
     import com.rabbitmq.client.Channel;
     import com.rabbitmq.client.Connection;
     import com.rabbitmq.test.util.ConnectionUtil;
     
     public class Producer {
      
     	private final static String QUEUE_NAME = "test_queue";
      
         public static void main(String[] argv) throws Exception {
             // 获取到连接以及mq通道
             Connection connection = ConnectionUtil.getConnection();
             // 从连接中创建通道
             Channel channel = connection.createChannel();
      
             /*
              * 声明（创建）队列
              * 参数1：队列名称
              * 参数2：为true时server重启队列不会消失
              * 参数3：队列是否是独占的，如果为true只能被一个connection使用，其他连接建立时会抛出异常
              * 参数4：队列不再使用时是否自动删除（没有连接，并且没有未处理的消息)
              * 参数5：建立队列时的其他参数
              */
             channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      
             // 消息内容
             String message = "Hello World!";
             /*
              * 向server发布一条消息
              * 参数1：exchange名字，若为空则使用默认的exchange
              * 参数2：routing key
              * 参数3：其他的属性
              * 参数4：消息体
              * RabbitMQ默认有一个exchange，叫default exchange，它用一个空字符串表示，它是direct exchange类型，
              * 任何发往这个exchange的消息都会被路由到routing key的名字对应的队列上，如果没有对应的队列，则消息会被丢弃
              */
             channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
             System.out.println(" [生产者] Sent '" + message + "'");
      
             //关闭通道和连接
             channel.close();
             connection.close();
         }
     }


      ```
      消费者
      ```aidl
                   package com.rabbitmq.test.T_helloworld;
                    
                   import com.rabbitmq.client.Channel;
                   import com.rabbitmq.client.Connection;
                   import com.rabbitmq.client.QueueingConsumer;
                   import com.rabbitmq.test.util.ConnectionUtil;
                    
                   public class Consumer {
                    
                   	private final static String QUEUE_NAME = "test_queue";
                    
                       public static void main(String[] argv) throws Exception {
                    
                           // 获取到连接以及mq通道
                           Connection connection = ConnectionUtil.getConnection();
                           // 从连接中创建通道
                           Channel channel = connection.createChannel();
                    
                           // 声明队列(如果你已经明确的知道有这个队列,那么下面这句代码可以注释掉,如果不注释掉的话,也可以理解为消费者必须监听一个队列,如果没有就创建一个)
                           channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                    
                           // 定义队列的消费者
                           QueueingConsumer consumer = new QueueingConsumer(channel);
                          /*
                            * 监听队列
                            * 参数1:队列名称
                            * 参数2：是否发送ack包，不发送ack消息会持续在服务端保存，直到收到ack。  可以通过channel.basicAck手动回复ack
                            * 参数3：消费者
                            */ 
                           channel.basicConsume(QUEUE_NAME, true, consumer);
                    
                           // 获取消息
                           while (true) {
                               QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                               String message = new String(delivery.getBody());
                               System.out.println(" [消费者] Received '" + message + "'");
                           }
                       }
                   }

      ```
     注意事项：队列只会在它不存在的时候创建，多次声明并不会重复创建。信息的内容是字节数组，也就意味着你可以传递任何数据。
          接收端会不断等待服务器推送消息，然后在控制台输出。 
          
     2. Work Queues模式<br>
        主动的消息推送
        ![](http://upload-images.jianshu.io/upload_images/683118-1153d8d6c2a8d9a3.png?imageMogr2/auto-orient/strip)
        生产者
        ````aidl
        package com.rabbitmq.test.T_work;
         
        import com.rabbitmq.client.Channel;
        import com.rabbitmq.client.Connection;
        import com.rabbitmq.test.util.ConnectionUtil;
        public class Producer {
         
        	private final static String QUEUE_NAME = "test_queue_work";
         
            public static void main(String[] argv) throws Exception {
                // 获取到连接以及mq通道
                Connection connection = ConnectionUtil.getConnection();
                Channel channel = connection.createChannel();
         
                // 声明队列
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
         
                for (int i = 0; i < 50; i++) {
                    // 消息内容
                    String message = "" + i;
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                    System.out.println(" [生产者] Sent '" + message + "'");
                    //发送的消息间隔越来越长
                    Thread.sleep(i * 10);
                }
         
                channel.close();
                connection.close();
                }
             }

        ````
     消费者1
     ```aidl
           package com.rabbitmq.test.T_work;
            
           import com.rabbitmq.client.Channel;
           import com.rabbitmq.client.Connection;
           import com.rabbitmq.client.QueueingConsumer;
           import com.rabbitmq.test.util.ConnectionUtil;
            
           public class Consumer1 {
            
           	private final static String QUEUE_NAME = "test_queue_work";
            
               public static void main(String[] argv) throws Exception {
            
                   // 获取到连接以及mq通道
                   Connection connection = ConnectionUtil.getConnection();
                   Channel channel = connection.createChannel();
            
                   // 声明队列
                   channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                   
                   // 同一时刻服务器只会发一条消息给消费者(能者多劳模式)
                   //channel.basicQos(1);
            
                   // 定义队列的消费者
                   QueueingConsumer consumer = new QueueingConsumer(channel);
                   /*
                    * 监听队列，不自动返回ack包,下面手动返回
                    * 如果不回复，消息不会在服务器删除
                    */ 
                   channel.basicConsume(QUEUE_NAME, false, consumer);
            
                   // 获取消息
                   while (true) {
                       QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                       String message = new String(delivery.getBody());
                       System.out.println(" [消费者1] Received '" + message + "'");
                       //休眠
                       Thread.sleep(10);
                       // 手动返回ack包确认状态
                       channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                     //channel.basicReject(); channel.basicNack(); //可以通过这两个函数拒绝消息，可以指定消息在服务器删除还是继续投递给其他消费者
                   }
               }
           }


     ```
     消费者2
     ```aidl
           package com.rabbitmq.test.T_work;
            
           import com.rabbitmq.client.Channel;
           import com.rabbitmq.client.Connection;
           import com.rabbitmq.client.QueueingConsumer;
           import com.rabbitmq.test.util.ConnectionUtil;
            
           public class Consumer2 {
            
           	private final static String QUEUE_NAME = "test_queue_work";
            
               public static void main(String[] argv) throws Exception {
            
                   // 获取到连接以及mq通道
                   Connection connection = ConnectionUtil.getConnection();
                   Channel channel = connection.createChannel();
            
                   // 声明队列
                   channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            
                   // 同一时刻服务器只会发一条消息给消费者(能者多劳模式)
                   //channel.basicQos(1);
            
                   // 定义队列的消费者
                   QueueingConsumer consumer = new QueueingConsumer(channel);
                   // 监听队列，手动返回完成状态
                   channel.basicConsume(QUEUE_NAME, false, consumer);
            
                   // 获取消息
                   while (true) {
                       QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                       String message = new String(delivery.getBody());
                       System.out.println(" [消费者2] Received '" + message + "'");
                       // 休眠1秒
                       Thread.sleep(1000);
            
                       //反馈消息的消费状态
                       channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                   }
               }
           }


    ```
        