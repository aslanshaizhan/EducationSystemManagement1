package spring.boot.endterm.dto.request;

import lombok.Data;

@Data
public class CourseRequest {
    private String name;
    private int price;
    private Long centerId;
}
