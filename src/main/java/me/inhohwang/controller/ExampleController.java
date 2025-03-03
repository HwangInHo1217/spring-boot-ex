package me.inhohwang.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ExampleController {


    @GetMapping("/thymeleaf/example")
    public String thymeleafExample(Model model) {
        Person personEx = new Person();
        personEx.setAge(20);
        personEx.setName("John");
        personEx.setId(1L);
        personEx.setHobbies(List.of("운동","독서"));
        model.addAttribute("person", personEx);
        model.addAttribute("today", LocalDate.now());
        return "example";

    }
}
@Getter
@Setter
class Person {
    private String name;
    private Long id;
    private int age;
    private List<String> hobbies;
}