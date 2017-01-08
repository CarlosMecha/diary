package com.carlosmecha.diary.controllers;

import com.carlosmecha.diary.models.User;
import com.carlosmecha.diary.services.DiaryService;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Pages controller.
 *
 * Created by Carlos on 12/28/16.
 */
@Controller
public class PagesController {

    private final static Logger logger = LoggerFactory.getLogger(PagesController.class);

    private DiaryService diary;
    private DateFormat formatter;

    @Autowired
    public PagesController(DiaryService diary) {
        this.diary = diary;
        this.formatter = new SimpleDateFormat("yyyy-MM-dd");
    }

    /**
     * Shows a form to create new ones.
     * @param model View model.
     * @param principal Authentication.
     * @return Template name.
     */
    @GetMapping("/")
    public String index(Model model, Principal principal) {
        User user = getLoggerUser(principal);
        model.addAttribute("name", user.getName());
        model.addAttribute("page", new PageForm());
        return "index";
    }

    /**
     * Creates the new page and redirects to the main page.
     * @param page Page.
     * @param result Result of binding.
     * @param redirectAttributes Redirect attributes.
     * @param principal Authentication.
     * @return Redirection.
     */
    @PostMapping("/")
    public String create(@ModelAttribute PageForm page,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Principal principal) {
        if(result.hasErrors()) {
            for(ObjectError e : result.getAllErrors()) {
                logger.debug("Error {}", e.getDefaultMessage());
            }
            redirectAttributes.addFlashAttribute("error", "Missing information!");
            return "redirect:/";
        }
        User user = getLoggerUser(principal);
        logger.debug("User {} is trying to create page for notebook {}", user.getLoginName(), page.notebookCode);

        if(page.notebookCode == null || page.notebookCode.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Missing information!");
            return "redirect:/";
        }

        diary.createPage(page.notebookCode,
                stringToDate(page.date), stringToSet(page.tagCodes),
                page.text, user);
        redirectAttributes.addFlashAttribute("message", "Page created");

        return "redirect:/";
    }

    private User getLoggerUser(Principal principal) {
        return diary.getUser(principal.getName());
    }

    private Set<String> stringToSet(String tags) {
        Set<String> set = new HashSet<>();
        if(tags != null && !tags.isEmpty()) {
            Collections.addAll(set, tags.split(","));
        }
        return set;
    }

    private Date stringToDate(String text) {
        try {
            return formatter.parse(text);
        } catch (ParseException e) {
            // TODO: Complain
            logger.warn("Date invalid {}", text);
            return new Date();
        }
    }

    /**
     * Expense form model.
     */
    public static class PageForm {

        @NotNull
        private float value;
        @NotEmpty
        private String notebookCode;
        @NotNull
        private String date;
        private String tagCodes;
        @NotEmpty
        private String text;

        public PageForm() {
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public String getNotebookCode() {
            return notebookCode;
        }

        public void setNotebookCode(String notebookCode) {
            this.notebookCode = notebookCode;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTagCodes() {
            return tagCodes;
        }

        public void setTagCodes(String tagCodes) {
            this.tagCodes = tagCodes;
        }

        @Override
        public String toString() {
            return "PageForm{" +
                    "value=" + value +
                    ", notebookCode='" + notebookCode + '\'' +
                    ", date=" + date +
                    ", tagCodes=" + tagCodes +
                    ", text='" + text + '\'' +
                    '}';
        }
    }

}
