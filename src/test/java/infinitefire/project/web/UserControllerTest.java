package infinitefire.project.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import infinitefire.project.support.test.AbstractIntegrationTest;

public class UserControllerTest extends AbstractIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);
    
    @Test
    public void form() throws Exception {
        ResponseEntity<String> result = template.getForEntity("/user/new", String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        log.debug("body : {}", result.getBody());
    }
}
