package spring.boot.endterm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import spring.boot.endterm.dto.request.CourseRequest;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @CreationTimestamp
    @Column(name = "created_at" , nullable = false , updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at" , nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "center_id" , nullable = false)
    private Center center;

    @ManyToMany(mappedBy = "courses"  , fetch = FetchType.EAGER)
    private List<User> users;

    public Course(CourseRequest request , Center center) {
        this.name = request.getName();
        this.price = request.getPrice();
        this.center = center;
    }
}
