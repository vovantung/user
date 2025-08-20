package txu.user.mainapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Setter

@Table(name = "REPLY")

public class CommentEntity implements Serializable {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_seq_gen")
    @SequenceGenerator(name = "emp_seq_gen", sequenceName = "EMP_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Getter
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATED_AT")
    private Date createdAt;
    public String getCreatedAt() {
//        return createdAt.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("[dd/MM/yyyy] HH:mm:ss"));
        return createdAt.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("[dd/MM/yyyy]"));
    }

    @Getter
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    @JsonBackReference
    private PostEntity post;


    @Getter
    @ManyToOne
    @JoinColumn(name = "USERID")
//    @JsonBackReference
    private AccountEntity account;


}
