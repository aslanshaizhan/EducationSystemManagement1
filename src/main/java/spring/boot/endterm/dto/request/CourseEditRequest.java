package spring.boot.endterm.dto.request;

import lombok.Data;

@Data
public class CourseEditRequest {
    private Long id;
    private String name;
    private int price;
}
