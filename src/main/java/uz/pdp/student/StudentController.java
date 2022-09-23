package uz.pdp.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.pdp.subject.Subject;
import uz.pdp.subject.SubjectDao;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentDao studentDao;
    private final SubjectDao subjectDao;

    @GetMapping("/{pageStr}")
    public String viewStudent(@PathVariable String pageStr, Model model){
        int size = 2;
        int page = 1;

        if (pageStr != null){
            page = Integer.parseInt(pageStr);
        }

        List<Student> allStudents = studentDao.getAllStudents(size, page);
        int counOfStudents = studentDao.getCountOfStudents();
        model.addAttribute("students",allStudents);
        model.addAttribute("count",counOfStudents);
        model.addAttribute("size",size);
        model.addAttribute("page",page);
        return "view-student-form";
    }

    @GetMapping("/add-form")
    public String addStudentForm(Model model){
        model.addAttribute("subjectList",subjectDao.getAllSubjectForSelect());
        return "add-student-form";
    }

    @PostMapping
    public String saveStudent(Student student){
        studentDao.saveStudent(student);
        return "redirect:/students/1";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id,Model model){
        Student student = studentDao.getStudentById(id);
        System.out.println(student);
        List<Subject> allSubjectForSelect = subjectDao.getAllSubjectForSelect();
        model.addAttribute("student",student);
        model.addAttribute("subject",allSubjectForSelect);
        return "update-student-form";
    }

    @PostMapping("/update-form")
    public String updateStudent(Student student){
        System.out.println(student);
        studentDao.updateStudent(student);
        return "redirect:/students/1";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id){
        studentDao.delete(id);
        return "redirect:/students/1";
    }



}
