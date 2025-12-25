package txu.user.mainapp.common;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    @KafkaListener(
            topics = "orders-events",
            groupId = "service-b-group"
    )
    public void consume(String message, Acknowledgment ack) {
        try {
            System.out.println("Received: " + message);
            ack.acknowledge();
        } catch (Exception e) {
            // retry
        }
    }
}