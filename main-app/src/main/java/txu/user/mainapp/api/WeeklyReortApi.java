package txu.user.mainapp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import txu.user.mainapp.base.AbstractApi;
import txu.user.mainapp.dto.*;
import txu.user.mainapp.entity.WeeklyReportEntity;
import txu.user.mainapp.service.WeeklyReportService;

import java.util.List;

@RestController
@RequestMapping("/weekly-report")
@RequiredArgsConstructor
public class WeeklyReortApi extends AbstractApi {

    private final WeeklyReportService weeklyReportService;

    @PostMapping("/get-presignedurl-for-get")
    public LinkDto getPreSignedUrlForGet(@RequestBody LinkRequest request) {
        LinkDto linkDto = new LinkDto();
        try {
            String pre_signed_url =  weeklyReportService.getPreSignedUrlForGet(request.getFilename());
            linkDto.setPre_signed_url(pre_signed_url);
        } catch (Exception e) {

        }
        return linkDto;
    }

    @PostMapping("/get-presignedurl-for-put")
    public LinkDto getPreSignedUrlForPut(@RequestBody LinkRequest request) {
        LinkDto linkDto = new LinkDto();
        try {
            return weeklyReportService.getPreSignedUrlForPut(request.getFilename());
        } catch (Exception e) {

        }
        return linkDto;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReport(@RequestBody UploadfileInfoRequest request) {
        try {
            WeeklyReportEntity weeklyReport = weeklyReportService.addReport(request);
            return ResponseEntity.ok(weeklyReport);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }

//    @PostMapping("/create")
//    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
//        try {
//            WeeklyReportEntity weeklyReport = weeklyReportService.create(file);
//            return ResponseEntity.ok(weeklyReport);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
//        }
//    }

    @PostMapping(value = "get-department-fromto")
    public List<WeeklyReportExtends> getDepartmentFromTo(@RequestBody FromDateToDateRequest request){
        return weeklyReportService. getDepartmentFromTo(request.getFrom(), request.getTo());
    }

    @PostMapping(value = "get-summary-fromto")
    public List<WeeklyReportExtends> getSummaryReportFromTo(@RequestBody FromDateToDateRequest request){
        return weeklyReportService. getSummaryReportFromTo(request.getFrom(), request.getTo());
    }

}
