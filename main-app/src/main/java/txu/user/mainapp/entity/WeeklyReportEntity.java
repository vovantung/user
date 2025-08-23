package txu.user.mainapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Table(name = "WEEKLY_REPORT")
@Entity
public class WeeklyReportEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FILENAME")
    private String filename;

    @Column(name = "ORIGINNAME")
    private String originName;

    @Column(name = "URL")
    private String url;

    @Column(name = "UPLOADED_AT")
    private Date uploadedAt;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
//    @JsonIgnore
    private DepartmentEntity department;
}



