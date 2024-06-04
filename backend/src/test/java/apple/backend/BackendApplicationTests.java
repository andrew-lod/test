package apple.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Configuration
public class BackendApplicationTests {
    // Note: Make sure docker container is running before running tests.
    private ApplicationContext context;

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Test
    void contextLoads() {
        assertNotNull(context);
    }

    @Test
    void main() {
        BackendApplication.main(new String[] {});
    }
}
