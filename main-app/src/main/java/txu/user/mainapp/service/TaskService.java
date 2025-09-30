package txu.user.mainapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import txu.common.exception.NotFoundException;
import txu.common.exception.UnauthorizedException;
import txu.user.mainapp.dao.TaskDao;
import txu.user.mainapp.dao.WorkflowLevelDao;
import txu.user.mainapp.dto.TaskDto;
import txu.user.mainapp.dto.TaskExtend;
import txu.user.mainapp.entity.AccountEntity;
import txu.user.mainapp.entity.TaskEntity;
import txu.user.mainapp.security.CustomUserDetails;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskDao taskDao;
    private final WorkflowLevelDao workflowLevelDao;

    public TaskExtend getById(Long taskId) {

        // Lấy thông tin người dùng gửi request thông qua token, mà lớp filter đã thực hiện qua lưu vào Security context holder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails = null;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                userDetails = (CustomUserDetails) principal;
            }
        }

        if (userDetails == null) {
            throw new UnauthorizedException("Unauthorized!");
        }

        TaskExtend task = taskDao.getById(taskId, userDetails.getId());
        if (task == null) {
            throw new NotFoundException("User is not found");
        }
        return task;
    }


    public List<TaskDto> getRelated() {

        // Lấy thông tin người dùng gửi request thông qua token, mà lớp filter đã thực hiện qua lưu vào Security context holder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails = null;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                userDetails = (CustomUserDetails) principal;
            }
        }

        if (userDetails == null) {
            throw new UnauthorizedException("Unauthorized!");
        }

        List<TaskDto> tasks = taskDao.getRelated(userDetails.getId());
        if (tasks == null) {
            throw new NotFoundException("User is not found");
        }
        return tasks;
    }


    public boolean submitTask(Long taskId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails = null;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                userDetails = (CustomUserDetails) principal;
            }
        }

        if (userDetails == null) {
            throw new UnauthorizedException("Unauthorized!");
        }

        TaskEntity task = taskDao.validateForSubmitTask(taskId, userDetails.getId());
        if (task == null) {
            // Không tồn tại task, hoặc task hiện tại không được assigned cho người dùng hiện tại,
            // hoặc task hiện tại không phải task để người dùng hiện tại submit(có thể là quyền approve)
            // hoặc người dùng hiện tại không thuộc workflow được gắn cho task hiện tại
            return false;
        }

        if (task.getCurrentLevel() > 2) {
            AccountEntity assigneeNext = taskDao.getUserInWorkflowLevel(taskId, task.getCurrentLevel());
            if (assigneeNext == null) {
                return false;
            }
            task.setAssignee(assigneeNext);
            task.setStatus("PENDING");
            taskDao.save(task);
        } else {
            AccountEntity assigneeNext = taskDao.getUserInWorkflowLevel(taskId, 2);
            if (assigneeNext == null) {
                return false;
            }
            task.setAssignee(assigneeNext);
            task.setStatus("PENDING");
            taskDao.save(task);
        }
        return true;
    }

    @Transactional
    public boolean approveTask(Long taskId) {
        // Lấy thông tin người dùng gửi request thông qua token, mà lớp filter đã thực hiện qua lưu vào Security context holder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = null;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                userDetails = (CustomUserDetails) principal;
            }
        }
        if (userDetails == null) {
            throw new UnauthorizedException("Unauthorized!");
        }

        TaskEntity task = taskDao.validateForApproveOrReject(taskId, userDetails.getId());
        if (task == null) {
            // Không tồn tại task, hoặc task hiện tại không được assigned cho người dùng hiện tại,
            // hoặc task hiện tại không phải task để người dùng hiện tại approve hoăck reject(có thể thuộc task để submit)
            // hoặc người dùng hiện tại không thuộc workflow được gắn cho task hiện tại
            return false;
        }

        int level = taskDao.getLevelNumberOfAssignedInTask(taskId, userDetails.getId());
//        int countMembers = taskDao.countMemberInTask(taskId, userDetails.getId());
        int countMembers = workflowLevelDao.countContinuousLevels(taskId);

        if(level < countMembers){
            // Người approve trung gian, chuyển lên cấp trên
            AccountEntity assigneeNext = taskDao.getUserInWorkflowLevel(taskId, level +1);
            if (assigneeNext == null) {
                return false;
            }
            task.setAssignee(assigneeNext);
            task.setCurrentLevel(level+1);
            task.setStatus("APPROVED");
            taskDao.save(task);
        }else {
            task.setStatus("DONE");
            task.setAssignee(null);
            taskDao.save(task);
            // Người approve cuối cùng trong workflow -> task Done
        }
        return true;
    }
}
