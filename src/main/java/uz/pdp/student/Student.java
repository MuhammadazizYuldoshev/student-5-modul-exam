package uz.pdp.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.subject.Subject;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Student {

    private Integer id;
    private String name;
    private String lastname;
    private String email;
    private Subject subject;
    private Integer subject_id;



}

