package txu.user.mainapp.api;

import org.springframework.web.bind.annotation.*;
import txu.user.mainapp.dao.DtoTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.annotation.KafkaListener;

@CrossOrigin(origins = "*", allowCredentials = "false", maxAge = 86400, allowedHeaders = "*")
@RestController
public class TestApi {


    @PostMapping(value = "/test1")
    public DtoTest test1() {
//        throw new TxException("test");
        DtoTest test = new DtoTest();


        // Ví dụ: một ngày java.util.Date
        Date date = new Date(); // ngày hiện tại

        // TimeZone cho Việt Nam
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");


        // Chuyển Date -> LocalDate theo Zone VN
        LocalDate l = date.toInstant()
                .atZone(zoneId)
                .toLocalDate();

        test.setDate(date.toString());
        test.setLocalDate(l.toString());
//        test.setDate_(Date.from(l.atStartOfDay(zoneId).toInstant()).toString());

        // Lấy ngày đầu tuần (Thứ 2) và cuối tuần (Chủ Nhật)
        LocalDate startOfWeek = l.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = l.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));



        // Nếu bạn muốn trả lại kiểu java.util.Date:
        Date startDate = Date.from(startOfWeek.atStartOfDay(zoneId).toInstant());
        Date endDate = Date.from(endOfWeek.atTime(LocalTime.MAX).atZone(zoneId).toInstant());

        test.setStart(startDate.toString());
        test.setEnd(endDate.toString());

//        // Lấy ngày đầu tuần (Thứ 2) và cuối tuần (Chủ Nhật)
//        LocalDate startOfWeek = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
//        LocalDate endOfWeek = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
//
//        // Nếu bạn muốn trả lại kiểu java.util.Date:
//        Date startDate = Date.from(startOfWeek.atStartOfDay(zoneId).toInstant());
//        Date endDate = Date.from(endOfWeek.atTime(LocalTime.MAX).atZone(zoneId).toInstant());
//
//        System.out.println("Ngày gốc: " + date);
//        System.out.println("Đầu tuần: " + startDate);
//        System.out.println("Cuối tuần: " + endDate);
//
//
//        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
//
//        // Chuyển Date -> LocalDate theo Zone VN
//        LocalDate from_ = from.toInstant().atZone(zoneId).toLocalDate();
//        LocalDate to_ = to.toInstant().atZone(zoneId).toLocalDate();
//
//        // from = 00:00:00
//        Date fromDate = Date.from(from_.atStartOfDay(zoneId).toInstant());
//
//        // to = 23:59:59.999
//        Date toDate = Date.from(to_.atTime(LocalTime.MAX).atZone(zoneId).toInstant());



        return test;
    }
}
