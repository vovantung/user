package txu.user.mainapp.dto;

import lombok.Getter;
import lombok.Setter;
import txu.user.mainapp.entity.WeeklyReportEntity;

import java.util.Date;

@Getter
@Setter
public class WeeklyReportExtends extends WeeklyReportEntity {
    String urlReportEx;
    String originNameReportEx;
    String filenameReportEx;
    Date dateReportEx;
}
