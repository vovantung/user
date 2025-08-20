package txu.user.mainapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import txu.common.grpc.GrpcConfig;

@Component
public class HrmConfiguration implements GrpcConfig {

    @Value("${server.grpc.port}")
    private int grpcPort;

    @Override
    public int getGrpcPort() {
        return grpcPort;
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Sử dụng mã hóa BCrypt
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    // Tùy chỉnh ClientHttpRequestFactory (cấu hình timeout, v.v...)
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);  // timeout kết nối (5 giây)
        factory.setReadTimeout(5000);     // timeout đọc (5 giây)
        return factory;
    }
}
