package com.carlosmecha.diary.repositories;

import com.carlosmecha.diary.models.Comment;
import com.carlosmecha.diary.models.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Set;

/**
 * Comment repository
 *
 * Created by carlos on 4/01/17.
 */
public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer>{

    @Query("SELECT c FROM Comment c WHERE c.page.id = :id")
    Set<Page> findAllByPageId(int id);

}
