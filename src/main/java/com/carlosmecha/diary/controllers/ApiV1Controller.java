package com.carlosmecha.diary.controllers;

import com.carlosmecha.diary.models.Notebook;
import com.carlosmecha.diary.models.Tag;
import com.carlosmecha.diary.services.DiaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Api controller.
 *
 * Created by Carlos on 12/28/16.
 */
@RestController
@RequestMapping("/api/v1")
public class ApiV1Controller {

    private final static Logger logger = LoggerFactory.getLogger(ApiV1Controller.class);

    private DiaryService diary;

    @Autowired
    public ApiV1Controller(DiaryService diary) {
        this.diary = diary;
    }

    /**
     * Retrieves all stored notebooks.
     * @return List of notebooks.
     */
    @RequestMapping("/notebooks")
    public Iterable<Notebook> getNotebooks() {
        return diary.getNotebooks();
    }

    /**
     * Retrieves all stored tags.
     * @return List of tags.
     */
    @RequestMapping("/tags")
    public Iterable<Tag> getTags() {
        return diary.getTags();
    }

}
