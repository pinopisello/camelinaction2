package camelinaction;

import org.apache.camel.spi.Registry;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringFtpToJMSWithPropertyPlaceholderTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
    	AbstractXmlApplicationContext springContext = new ClassPathXmlApplicationContext("camelinaction/SpringFtpToJMSWithPropertyPlaceholderTest.xml");
    	SpringCamelContext camelContext = (SpringCamelContext)springContext.getBean(SpringCamelContext.class);
    	Registry reg = camelContext.getRegistry();
        return springContext;
    }

    @Test
    public void testPlacingOrders() throws Exception {
        getMockEndpoint("mock:incomingOrders").expectedMessageCount(1);
        assertMockEndpointsSatisfied();
    }
}
