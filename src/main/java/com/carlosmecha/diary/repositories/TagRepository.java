package com.carlosmecha.diary.repositories;

import com.carlosmecha.diary.models.Tag;
import org.springframework.data.repository.CrudRepository;

/**
 * Tag repository.
 *
 * Created by Carlos on 12/25/16.
 */
public interface TagRepository extends CrudRepository<Tag, String> {
}
