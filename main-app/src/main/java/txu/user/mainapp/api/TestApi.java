package txu.user.mainapp.api;

import org.springframework.web.bind.annotation.*;
import txu.user.mainapp.dao.DtoTest;

@CrossOrigin(origins = "*", allowCredentials = "false", maxAge = 86400, allowedHeaders = "*")
@RestController
public class TestApi {
    @PostMapping(value = "/test")
    public DtoTest test() {
//        throw new TxException("test");
        DtoTest test = new DtoTest();
        test.setTest("Hello World");
        return test;
    }
}
