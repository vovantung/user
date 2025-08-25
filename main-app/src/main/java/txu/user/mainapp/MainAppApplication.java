package txu.user.mainapp;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import txu.common.grpc.GrpcServer;
import txu.user.mainapp.grpc.HrmGrpcService;

import java.io.IOException;
import java.util.TimeZone;

@SpringBootApplication
public class MainAppApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Đặt TimeZone mặc định cho ứng dụng
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
//        SpringApplication.run(MainAppApplication.class, args);
        GrpcServer.start(MainAppApplication.class, HrmGrpcService.class);
    }

}
