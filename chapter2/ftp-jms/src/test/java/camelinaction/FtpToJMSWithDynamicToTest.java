package camelinaction;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.spi.Registry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class FtpToJMSWithDynamicToTest extends CamelTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {
        // create CamelContext
        CamelContext camelContext = super.createCamelContext();
        
        Registry contextRegistry =  camelContext.getRegistry();
        // connect to embedded ActiveMQ JMS broker
        ConnectionFactory connectionFactory =  new ActiveMQConnectionFactory("tcp://localhost:61616");
        camelContext.addComponent("jms",     JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        
        return camelContext;
    }
    
    @Test
    public void testPlacingOrders() throws Exception {
        getMockEndpoint("mock:incomingOrders").expectedMessageCount(1);
        assertMockEndpointsSatisfied();
    }
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // load file orders from src/data into the JMS queue
                from("file:src/data?noop=true")
                    .setHeader("myDest", constant("incomingOrders"))
                    .toD("jms:${header.myDest}");
                       
                // test that our route is working
                from("jms:incomingOrders")
                .to("mock:incomingOrders");                
            }
        };
    }
}
