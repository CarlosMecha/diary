package com.carlosmecha.diary.services;

import com.carlosmecha.diary.models.*;
import com.carlosmecha.diary.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Diary service
 *
 * Created by carlos on 7/01/17.
 */
@Service
public class DiaryService {

    private final static Logger logger = LoggerFactory.getLogger(DiaryService.class);

    private NotebookRepository notebooks;
    private PageRepository pages;
    private CommentRepository comments;
    private TagRepository tags;
    private UserRepository users;

    @Autowired
    public DiaryService(NotebookRepository notebooks, PageRepository pages, CommentRepository comments, TagRepository tags, UserRepository users) {
        this.notebooks = notebooks;
        this.pages = pages;
        this.comments = comments;
        this.tags = tags;
        this.users = users;
    }

    /**
     * Gets all notebooks.
     * @return List of notebooks.
     */
    public Iterable<Notebook> getNotebooks() {
        logger.debug("Looking for all notebooks.");
        return notebooks.findAll(new PageRequest(0, 1000, Sort.Direction.ASC, "code"));
    }

    /**
     * Gets all tags.
     * @return List of tags.
     */
    public Iterable<Tag> getTags() {
        logger.debug("Looking for all tags.");
        return tags.findAll();
    }

    /**
     * Retrieves an user by login name.
     * @param loginName User login name.
     * @return User if found, <code>null</code> otherwise.
     */
    public User getUser(String loginName) {
        logger.debug("Looking for user with name {}", loginName);
        return users.findOne(loginName);
    }

    /**
     * Creates a notebook using just the name.
     * @param name Notebook name.
     * @return New notebook.
     */
    public Notebook createNotebook(String name) {
        return createNotebook(null, name);
    }

    /**
     * Creates the notebook.
     * @param code Notebook code.
     * @param name Notebook name.
     * @return New notebook.
     */
    @Transactional
    public Notebook createNotebook(String code, String name) {
        logger.debug("Creating notebook {}", name);
        Notebook notebook = (code == null || code.isEmpty()) ? new Notebook(name) : new Notebook(code, name);
        notebooks.save(notebook);
        return notebook;
    }

    /**
     * Creates a new page.
     * @param notebookCode Notebook code.
     * @param date Date.
     * @param tagCodes A list of tags.
     * @param text Text.
     * @param requester The user who request the creation.
     * @return The new expense.
     */
    @Transactional
    public Page createPage(String notebookCode, Date date, Set<String> tagCodes, String text, User requester) {
        logger.debug("Creating expense of {}", notebookCode);
        Notebook notebook = notebooks.findOne(notebookCode);
        if(notebook == null) {
            // TODO: Complain
            notebook = new Notebook(notebookCode, notebookCode);
        }
        Set<Tag> tagSet = new HashSet<>();
        for(String tagCode : tagCodes) {
            Tag tag = tags.findOne(tagCode);
            if(tag == null) {
                tag = new Tag(tagCode);
            }
            tagSet.add(tag);
        }

        Page page = new Page(notebook, date, requester, tagSet);
        pages.save(page);

        Comment comment = new Comment(page, text, date, requester);
        comments.save(comment);

        return page;
    }

}
