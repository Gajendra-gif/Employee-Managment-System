package in.ashokit.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@Table(name = "emp")
@Data
public class Employee implements Serializable {

    @NotNull(message = "ID Can't be null")
    @Id
    @Positive(message = "Id must be positive")
    private Integer empno;

    @NotBlank(message = "Name can't be null")
    private String ename;

    @NotNull(message = "cant be null")
    @Positive(message = "must be positive")
    private Double sal;

    @NotBlank(message = "dept cant be blank")
    @Column(name = "DEPARTMENT")
    private String dept;

}
