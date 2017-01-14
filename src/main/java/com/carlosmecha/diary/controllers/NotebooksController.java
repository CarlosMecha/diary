package com.carlosmecha.diary.controllers;

import com.carlosmecha.diary.models.Comment;
import com.carlosmecha.diary.models.Page;
import com.carlosmecha.diary.models.User;
import com.carlosmecha.diary.services.DiaryService;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private Parser parser;
    private HtmlRenderer renderer;

    @Autowired
    public NotebooksController(DiaryService diary, Parser parser, HtmlRenderer renderer) {
        this.diary = diary;
        this.parser = parser;
        this.renderer = renderer;
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
        redirectAttributes.addAttribute("name", user.getName());

        if(notebook.getName() == null || notebook.getName().isEmpty()) {
            redirectAttributes.addAttribute("error", "At least the name must be something!");
            return "redirect:/notebooks";
        }

        diary.createNotebook(notebook.getCode(), notebook.getName());
        redirectAttributes.addAttribute("message", "Notebook " + notebook.getName() + " created.");

        return "redirect:/notebooks";
    }

    @GetMapping("/notebooks/{code}/pages/{id}")
    public String getPage(@PathVariable("code") String notebookCode,
                          @PathVariable("id") String pageId,
                          Model model, Principal principal) {

        User user = getLoggerUser(principal);
        logger.debug("User {} is trying to access to notebook {}", user.getLoginName(), notebookCode);
        model.addAttribute("name", user.getName());

        int id;
        try {
            id = Integer.parseInt(pageId);
        } catch (NumberFormatException e) {
            String lower = pageId.toLowerCase();

            if("last".equals(lower) || "first".equals(lower)) {
                boolean first = "first".equals(lower);

                List<Integer> ids = diary.getPageIds(notebookCode);
                if(ids.isEmpty()) {
                    // Do something
                    return "redirect:/notebooks";
                }

                int index = first ? 0 : ids.size() - 1;

                model.addAttribute("page", diary.getPage(ids.get(index)));
                model.addAttribute("comments", renderComments(diary.getSortedComments(ids.get(index))));
                if(ids.size() > 1) {
                    if(first) {
                        model.addAttribute("next", ids.get(1));
                    } else {
                        model.addAttribute("prev", ids.get(index - 1));
                    }
                }

                return "page";

            } else {
                // Do something
                return "redirect:/notebooks";
            }
        }

        Page page = diary.getPage(id);
        if(page == null || !page.getNotebook().getCode().equals(notebookCode)) {
            // Do something
            return "redirect:/notebooks";
        }

        model.addAttribute("page", page);
        model.addAttribute("comments", renderComments(diary.getSortedComments(id)));
        List<Integer> ids = diary.getPageIds(notebookCode);
        int prev = -1;
        int i = 0;
        while(i < ids.size()) {
            int pId = ids.get(i);
            if(pId == id) {
                if(prev != -1) {
                    model.addAttribute("prev", prev);
                }
                if(i < ids.size() - 1) {
                    model.addAttribute("next", ids.get(i+1));
                }
                break;
            }
            prev = pId;
            i++;
        }

        return "page";
    }

    private List<RenderedComment> renderComments(List<Comment> comments) {
        return comments.stream().map(c ->
                    new RenderedComment(c.getWroteBy().getName(), c.getWroteOn(),
                            renderer.render(parser.parse(c.getContent()))))
                .collect(Collectors.toList());
    }

    private User getLoggerUser(Principal principal) {
        return diary.getUser(principal.getName());
    }

    /**
     * Category render model
     */
    public static class RenderedComment {

        private String wroteBy;
        private Date wroteOn;
        private String content;

        public RenderedComment(String wroteBy, Date wroteOn, String content) {
            this.wroteBy = wroteBy;
            this.wroteOn = wroteOn;
            this.content = content;
        }

        public String getWroteBy() {
            return wroteBy;
        }

        public Date getWroteOn() {
            return wroteOn;
        }

        public String getContent() {
            return content;
        }
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
