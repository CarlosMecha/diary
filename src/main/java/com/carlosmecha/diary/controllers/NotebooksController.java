package com.carlosmecha.diary.controllers;

import com.carlosmecha.diary.models.User;
import com.carlosmecha.diary.services.DiaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

/**
 * Notebooks controller.
 * Thymeleaf and @RequestMapping don't work at class level.
 *
 * Created by Carlos on 12/29/16.
 */
@Controller
public class NotebooksController {

    private final static Logger logger = LoggerFactory.getLogger(NotebooksController.class);

    private DiaryService diary;

    @Autowired
    public NotebooksController(DiaryService diary) {
        this.diary = diary;
    }

    /**
     * Shows all notebooks and a form to create a new one.
     * @param model View model.
     * @param principal Authentication.
     * @return Template name.
     */
    @GetMapping("/notebooks")
    public String index(Model model, Principal principal) {
        User user = getLoggerUser(principal);
        model.addAttribute("name", user.getName());
        model.addAttribute("notebooks", diary.getNotebooks());
        model.addAttribute("notebook", new NotebookForm());
        return "notebooks";
    }

    /**
     * Create a new notebook and redirects to the main page.
     * @param notebook Notebook form.
     * @param redirectAttributes Redirect attributes.
     * @param principal Authentication.
     * @return Redirection.
     */
    @PostMapping("/notebooks")
    public String create(@ModelAttribute NotebookForm notebook,
                         RedirectAttributes redirectAttributes,
                         Principal principal) {
        User user = getLoggerUser(principal);
        logger.debug("User {} is trying to create notebook {}", user.getLoginName(), notebook.getName());

        if(notebook.getName() == null || notebook.getName().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "At least the name must be something!");
            return "redirect:/notebooks";
        }

        diary.createNotebook(notebook.getCode(), notebook.getName());
        redirectAttributes.addFlashAttribute("message", "Notebook " + notebook.getName() + " created.");

        return "redirect:/notebooks";
    }

    private User getLoggerUser(Principal principal) {
        return diary.getUser(principal.getName());
    }

    /**
     * Category form model.
     */
    public static class NotebookForm {

        private String code;
        private String name;

        public NotebookForm() {
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
