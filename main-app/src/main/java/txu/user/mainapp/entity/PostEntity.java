package txu.user.mainapp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Entity
@Setter
//@Getter
@Table(name = "POST")

public class PostEntity implements Serializable {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_seq_gen")
    @SequenceGenerator(name = "emp_seq_gen", sequenceName = "EMP_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Getter
    @Column(name = "TITLE")
    private String title;

    @Getter
    @Column(name = "CONTENT")
    private String content;

//
    @Column(name = "UNSIGNED_TITLE")
    @Getter
    private String unsignedTitle;

    @Column(name = "CREATED_AT")
    private Date createdAt;
    public String getCreatedAt() {
//        return createdAt.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("[dd/MM/yyyy] HH:mm:ss"));
        return createdAt.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("[dd/MM/yyyy]"));
    }


    @Getter
    @JsonManagedReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CommentEntity> commentList;

    @Getter
    @ManyToOne
    @JoinColumn(name = "USERID")
//    @JsonBackReference
    private AccountEntity account;


}
