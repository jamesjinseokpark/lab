package com.doiet.lab.service;

import com.doiet.lab.model.TodoEntity;
import com.doiet.lab.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TodoService {

    public String testService()
    {
        return null;
    }

    private TodoRepository repository;

    public List<TodoEntity> create(final TodoEntity entity)
       {
           //Validations -> 검증 메서드 분리로 인한 주석처리 20220706
           /*if(entity ==null)
           {
               log.warn("Entity can't be null");
               throw new RuntimeException("Entity can't be null");
           }

           if(entity.getUserId() ==null)
           {
               log.warn("Unknown user.");
               throw new RuntimeException("Unknown user.");
           }*/

           repository.save(entity);

           log.info("Entity Id : {} saved." + entity.getId());

           return repository.findByUserId(entity.getUserId());
       }

    private void validate(final TodoEntity entity)
    {
        if(entity == null)
        {
            log.warn("Entity can't be null");
            throw new RuntimeException("Entity can't be null");
        }

        if(entity.getUserId() == null)
        {
            log.warn("Unknown user");
            throw new RuntimeException("Unknown user");
        }

    }
}
