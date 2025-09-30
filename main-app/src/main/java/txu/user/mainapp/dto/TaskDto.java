package txu.user.mainapp.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private Date deadline;
    private Long assigneeId;
    private Long workflowId;
    private Integer currentLevel;
    private Date createdAt;
    private Date updatedAt;
    private String vaitro;

    public TaskDto(Long id, String title, String description, String status, String priority, Date deadline,
                   Long assigneeId, Long workflowId, Integer currentLevel, Date createdAt, Date updatedAt, String vaitro) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.deadline = deadline;
        this.assigneeId = assigneeId;
        this.workflowId = workflowId;
        this.currentLevel = currentLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.vaitro = vaitro;
    }
}
