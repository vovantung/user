package txu.user.mainapp.service;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import txu.user.mainapp.dao.DepartmentDao;
import txu.user.mainapp.dao.WeeklyReportDao;
import txu.user.mainapp.dto.WeeklyReportExtends;
import txu.user.mainapp.entity.DepartmentEntity;
import txu.user.mainapp.entity.WeeklyReportEntity;
import txu.user.mainapp.security.CustomUserDetails;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static txu.user.mainapp.common.DateUtil.*;

@Service
@RequiredArgsConstructor
public class WeeklyReportService {

    private final MinioClient minioClient;
    private final WeeklyReportDao weeklyReportDao;
    private final DepartmentDao departmentDao;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.url}")
    private String url;

    public WeeklyReportEntity create(MultipartFile file) throws Exception {

        // Lấy thông tin người dùng gửi request thông qua token, mà lớp filter đã thực hiện qua lưu vào Security context holder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                userDetails = (CustomUserDetails) principal;
            } else {
                userDetails = null;
            }
        } else {
            userDetails = null;
        }

        // Nếu tồn tại những thông tin report trong tuần mà liên qua đến người dùng đang upload report hiện tại thì
        // xóa hết report đã upload trên minio và xóa hết dữ liệu lưu ở cơ sở dữ liệu (trong tuần hiện tại)
        List<WeeklyReportEntity> weeklyReportEntities = weeklyReportDao.getFromTo(toDate(getStartOfWeek()), toDate(getEndOfWeek()));
        weeklyReportEntities.forEach(weeklyReportExtends -> {
            if (weeklyReportExtends.getDepartment().getId() == userDetails.getDepartment_id()) {
                // Xóa file trên minio
                try {
                    minioClient.removeObject(
                            RemoveObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(weeklyReportExtends.getFilename())
                                    .build()
                    );
                    System.out.println("Deleted successfully: " + weeklyReportExtends.getFilename());
                } catch (Exception e) {
                    System.err.println("Error deleting file: " + e.getMessage());
                    throw new RuntimeException("File deletion failed", e);
                }
                // Xóa dữ liệu
                weeklyReportDao.remove(weeklyReportExtends);
            }
        });

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Ensure bucket exists
        boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        // Upload to MinIO
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filename)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        String fileUrl = String.format(url + "/%s/%s", bucketName, filename);

        // Save metadata
        DepartmentEntity department = null;
        if (userDetails != null) {
            department = departmentDao.findById(userDetails.getDepartment_id());
        }

        WeeklyReportEntity weeklyReport = new WeeklyReportEntity();
        weeklyReport.setFilename(filename);
        weeklyReport.setUrl(fileUrl);
        weeklyReport.setOriginName(file.getOriginalFilename());
        weeklyReport.setDepartment(department);
        weeklyReport.setUploadedAt(DateTime.now().toDate());
        return weeklyReportDao.save(weeklyReport);
    }

    public List<WeeklyReportExtends> getDepartmentFromTo(Date from, Date to) {
        // Lấy thông tin người dùng gửi request thông qua token, mà lớp filter đã thực hiện qua lưu vào Security context holder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                userDetails = (CustomUserDetails) principal;
            } else {
                userDetails = null;
            }
        } else {
            userDetails = null;
        }

        // Save metadata
        DepartmentEntity department = null;
        if (userDetails != null) {
            department = departmentDao.findById(userDetails.getDepartment_id());
        }
        assert department != null;
        List<WeeklyReportEntity> list = weeklyReportDao.getByDepartmentIdFromTo(
                from,
                to,
                department.getId());

        List<WeeklyReportExtends> results = new ArrayList<>();
        list.forEach(weeklyReport -> {

            ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");

            // Chuyển Date -> LocalDate theo Zone VN
            LocalDate localDate = weeklyReport.getUploadedAt().toInstant()
                    .atZone(zoneId)
                    .toLocalDate();

            // Lấy ngày đầu tuần (Thứ 2) và cuối tuần (Chủ Nhật)
            LocalDate startOfWeek = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate endOfWeek = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

            // Nếu bạn muốn trả lại kiểu java.util.Date:
            Date startDate = Date.from(startOfWeek.atStartOfDay(zoneId).toInstant());
            Date endDate = Date.from(endOfWeek.atTime(LocalTime.MAX).atZone(zoneId).toInstant());

//            System.out.println("Ngày gốc: " + weeklyReportEntity.getUploadedAt());
//            System.out.println("Đầu tuần: " + startDate);
//            System.out.println("Cuối tuần: " + endDate);

            // Lấy báo cáo của đơn vị tổng hợp trong tuần hiện tại mà báo cáo của đơn vị nghiệp vụ được chọn
            WeeklyReportEntity rs = weeklyReportDao.getSingleByDepartmentIdFromTo(startDate, endDate, 2L);

            WeeklyReportExtends temp = new WeeklyReportExtends();
            temp.setId(weeklyReport.getId());
            temp.setUrl(weeklyReport.getUrl());
            temp.setFilename(weeklyReport.getFilename());
            temp.setOriginName(weeklyReport.getOriginName());
            temp.setDepartment(weeklyReport.getDepartment());
            temp.setUploadedAt(weeklyReport.getUploadedAt());

            if (rs != null) {
                temp.setOriginNameReportEx(rs.getOriginName());
                temp.setDateReportEx(rs.getUploadedAt());
                temp.setUrlReportEx(rs.getUrl());
            }
            results.add(temp);
        });

        return results;
    }

    public List<WeeklyReportExtends> getSummaryReportFromTo(Date from, Date to) {
        // Lấy thông tin người dùng gửi request thông qua token, mà lớp filter đã thực hiện qua lưu vào Security context holder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                userDetails = (CustomUserDetails) principal;
            } else {
                userDetails = null;
            }
        } else {
            userDetails = null;
        }

        // Save metadata
        DepartmentEntity department;
        if (userDetails != null) {
            department = departmentDao.findById(userDetails.getDepartment_id());
        } else {
            department = null;
        }
        assert department != null;
        List<WeeklyReportEntity> list = weeklyReportDao.getByDepartmentIdFromTo(
                from,
                to,
                2L);
        List<WeeklyReportExtends> results = new ArrayList<>();
        list.forEach(weeklyReport -> {

            ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");

            // Chuyển Date -> LocalDate theo Zone VN
            LocalDate localDate = weeklyReport.getUploadedAt().toInstant()
                    .atZone(zoneId)
                    .toLocalDate();

            // Lấy ngày đầu tuần (Thứ 2) và cuối tuần (Chủ Nhật)
            LocalDate startOfWeek = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate endOfWeek = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

            // Nếu bạn muốn trả lại kiểu java.util.Date:
            Date startDate = Date.from(startOfWeek.atStartOfDay(zoneId).toInstant());
            Date endDate = Date.from(endOfWeek.atTime(LocalTime.MAX).atZone(zoneId).toInstant());

            // Lấy báo cáo của đơn vị nghiệp vụ trong tuần hiện tại mà báo cáo tổng hợp được chọn
            WeeklyReportEntity rs = weeklyReportDao.getSingleByDepartmentIdFromTo(startDate, endDate, department.getId());
            WeeklyReportExtends temp = new WeeklyReportExtends();
            temp.setId(weeklyReport.getId());
            temp.setUrl(weeklyReport.getUrl());
            temp.setFilename(weeklyReport.getFilename());
            temp.setOriginName(weeklyReport.getOriginName());
            temp.setDepartment(weeklyReport.getDepartment());
            temp.setUploadedAt(weeklyReport.getUploadedAt());
            if (rs != null) {
                temp.setOriginNameReportEx(rs.getOriginName());
                temp.setDateReportEx(rs.getUploadedAt());
                temp.setUrlReportEx(rs.getUrl());
            }
            results.add(temp);
        });
        return results;
    }
}
