package com.carlosmecha.diary.repositories;

import com.carlosmecha.diary.models.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Set;

/**
 * Created by carlos on 4/01/17.
 */
public interface PageRepository extends PagingAndSortingRepository<Page, Integer>{

    @Query("SELECT p FROM Page p WHERE p.notebook.id = :id")
    Set<Page> findAllByNotebookId(int id);

}
