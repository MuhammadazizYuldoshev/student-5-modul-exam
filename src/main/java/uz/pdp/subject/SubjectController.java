package uz.pdp.subject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/subject")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectDao subjectDao;

    @GetMapping("/{pageStr}")
    public String showSubject(@PathVariable String pageStr, Model model){

        int size = 2;
        int page = 1;

        if (pageStr != null) {
            page = Integer.parseInt(pageStr);
        }

        List<Subject> allSubjects = subjectDao.getAllSubjects(size,page);
        model.addAttribute("subjects",allSubjects);
        model.addAttribute("totalElementsCount",subjectDao.getCountOfSubjects());
        model.addAttribute("size",size);
        model.addAttribute("currentPage",page);
        return "view-subject-form";

    }


    @GetMapping("/add-subject-form")
    public String addSubject(){

        return "add-subject-form";
    }

    @PostMapping
    public String saveSubject(Subject subject){
        subjectDao.saveSubject(subject);


        return "redirect:/subject/1";

    }


    @GetMapping("edit/{id}")
    public String updateSubject(@PathVariable("id") int id,Model model){

        Subject subject = subjectDao.getSubject(id);
        model.addAttribute("subject",subject);
        return "update-subject-form";

    }


    @RequestMapping(value = "/update-subject",method = RequestMethod.POST)
    public String update(Subject position){
        subjectDao.updatePosition(position);
        return "redirect:/subject/1";

    }


    @GetMapping("/delete/{id}")
    public String deletePosition(@PathVariable("id") int id){
        subjectDao.delete(id);
        return "redirect:/subject/1";
    }





}
