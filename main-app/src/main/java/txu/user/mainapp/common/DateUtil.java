package txu.user.mainapp.common;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static LocalDate getStartOfWeek() {
        return LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public static LocalDate getEndOfWeek() {
        return LocalDate.now()
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date[] getWeekRange(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        System.out.println("TimeZone: " + cal.getTimeZone().getDisplayName());

        // Xác định ngày đầu tuần (theo mặc định: Chủ nhật, có thể chỉnh lại thành Thứ 2)
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek()+1);
        Date startOfWeek = cal.getTime();

        // Ngày cuối tuần = ngày đầu tuần + 6
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date endOfWeek = cal.getTime();

        return new Date[]{startOfWeek, endOfWeek};
    }
}
