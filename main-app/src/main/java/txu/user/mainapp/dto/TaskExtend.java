package txu.user.mainapp.dto;

import lombok.Getter;
import lombok.Setter;
import txu.user.mainapp.entity.TaskEntity;

@Getter
@Setter
public class TaskExtend  {
    private TaskEntity task;
    private String vaiTro;

    public TaskExtend(TaskEntity task, String vaiTro) {
        this.task = task;
        this.vaiTro = vaiTro;
    }
}
