package ecnu.compiling.compilingmate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompilingMateApplicationTests {
	private static final Logger LOGGER = LoggerFactory.getLogger(CompilingMateApplicationTests.class);
	@Test
	public void contextLoads() {
	}

	@Test
	public void shouldAbleToGetObjectFromCore() {
		HelloWorld helloWorld = new HelloWorld();
		LOGGER.debug("get HelloWorld from api");
		LOGGER.info(helloWorld.sayHello());
	}

}
