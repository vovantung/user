package txu.user.mainapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Setter
@Getter
@Table(name = "EMPLOYEE")

public class EmployeeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_seq_gen")
    @SequenceGenerator(name = "emp_seq_gen", sequenceName = "EMP_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "IdOrGenerated")
//    @Column(name = "id")
//    private String id;
//
//    private String name;

//    private Float price;
//
//    private String description;
//
//    private Date createDatetime;
//
//    private Date updateDatetime;

//    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
//    private RateAvg rateAvg;
//
//    @JsonManagedReference
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<ProductCategoryEntity> listProductCategory;
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
//    private List<CartItemEntity> listCartItemEntity;
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
//    private List<OrderItemEntity> listOrderItem;

}
