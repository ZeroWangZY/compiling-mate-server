package ecnu.compiling.compilingmate.syntax;

import ecnu.compiling.compilingmate.controller.SyntaxController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebAppConfiguration
@RunWith(value = SpringJUnit4ClassRunner.class)
public class SyntaxTest {
    private MockMvc mockMvc;

    @InjectMocks
    SyntaxController syntaxController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(syntaxController).build();
    }

    @Test
    public void parsingProcessingOutputTest() throws Exception {
        mockMvc.perform(get("/parsingProcessingOutput"))
                        .andDo(print());
    }
}
