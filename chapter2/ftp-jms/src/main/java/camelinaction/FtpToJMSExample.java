package camelinaction;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * A route that polls an FTP server for new orders, downloads them, converts the order 
 * file into a JMS Message and then sends it to the JMS incomingOrders queue hosted 
 * on an embedded ActiveMQ broker instance.
 */
public class FtpToJMSExample {

    public static void main(String args[]) throws Exception {
        // create CamelContext
        CamelContext context = new DefaultCamelContext();
        
        // connect to embedded ActiveMQ JMS broker
        String url = "tcp://localhost:61616";
        ConnectionFactory connectionFactory =  new ActiveMQConnectionFactory(url);//<artifactId>activemq-all</artifactId>
        
        //JmsComponent e' aggiunto al context per abilitare endpoints tipo "jms:xyz"        
        context.addComponent("jms",JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));//<artifactId>camel-jms</artifactId>


        
        // add our route to the CamelContext : prende files da ftp://rider.com/orders e li mette in jms queue incomingOrders nel local ActiveMQ
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {      
            	//"jms" indica l uso del cpmponent JmsComponent creato e aggiunto al context su
                from("ftp://localhost/Users/glocon/Miei/Applicativi/apache-activemq-5.14-SNAPSHOT/orders?username=glocon&password=Pippo3792#").  to("jms:queue:incomingOrders");//<artifactId>camel-ftp</artifactId> per gestire ftp endpoints
            }
        });

        // start the route and let it do its work
        context.start();
        Thread.sleep(10000);

        // stop the CamelContext
        context.stop();
    }
}
