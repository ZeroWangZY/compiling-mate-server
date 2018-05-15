package ecnu.compiling.compilingmate;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorld.class);

    @Test
    public void helloWorldTest() throws Exception{
        HelloWorld helloWorld = new HelloWorld();
        LOGGER.info(helloWorld.sayHello());
    }
}
