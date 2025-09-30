package txu.user.mainapp.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import txu.user.mainapp.base.AbstractApi;
import txu.user.mainapp.dto.TaskDto;
import txu.user.mainapp.dto.TaskExtend;
import txu.user.mainapp.dto.TaskRequest;
import txu.user.mainapp.service.TaskService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskApi extends AbstractApi {

    private final TaskService taskService;

//    @PostMapping(value = "/get-by-id", consumes = "application/json")
//    public List<TaskExtend> getById(@RequestBody TaskRequest request) {
//        return taskService.getByAssigned(request.getAssigneeId());
//    }

    @PostMapping(value = "/get-related", consumes = "application/json")
    public List<TaskDto> getRelated() {
        return taskService.getRelated();
    }

    @PostMapping(value = "/get-by-id", consumes = "application/json")
    public TaskExtend getById(@RequestBody TaskRequest request) {
        return taskService.getById(request.getTaskId());
    }

    @PostMapping(value = "/submit-task", consumes = "application/json")
    public boolean submitTask(@RequestBody TaskRequest request) {
        return taskService.submitTask(request.getTaskId());
    }

    @PostMapping(value = "/approve-task", consumes = "application/json")
    public boolean approveTask(@RequestBody TaskRequest request) {
        return taskService.approveTask(request.getTaskId());
    }





}
