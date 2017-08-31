package camelinaction;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * A route that polls an FTP server for new orders, downloads them, converts the order 
 * file into a JMS Message and then sends it to the JMS incomingOrders queue hosted 
 * on an embedded ActiveMQ broker instance.
 */
public class FtpToJMSWithProcessorExample {

    public static void main(String args[]) throws Exception {
        // create CamelContext
        CamelContext context = new DefaultCamelContext();
        
        // connect to embedded ActiveMQ JMS broker
        String url = "tcp://localhost:61616";
        ConnectionFactory connectionFactory =  new ActiveMQConnectionFactory(url);
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        // add our route to the CamelContext
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("ftp://localhost/Miei/Applicativi/apache-activemq-5.14-SNAPSHOT/orders?username=glocon&password=Pippo3791&stepwise=false") 
                .process(new Processor() {                    
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("We just downloaded: " + exchange.getIn().getHeader("CamelFileName"));
                    }
                })
                .to("jms:queue:incomingOrders");
            }
        });

        // start the route and let it do its work
        context.start();
        Thread.sleep(100000);

        // stop the CamelContext
        context.stop();
    }
}
