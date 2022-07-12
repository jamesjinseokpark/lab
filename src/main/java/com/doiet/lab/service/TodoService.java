package com.doiet.lab.service;

import com.doiet.lab.model.TodoEntity;
import com.doiet.lab.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {

    public String testService()
    {
        return "TEST SERVICE";
    }

    @Autowired
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
        validate(entity);

        repository.save(entity);
        log.info("Entity Id : {} saved." + entity.getId());

        return repository.findByUserId(entity.getUserId());
    }

    public List<TodoEntity> retrieve(final String userId)
    {
        return repository.findByUserId(userId);
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

    public List<TodoEntity> update(final TodoEntity entity)
    {
        //1. 저장 엔티티의 유효성 체크
        validate(entity);
        //2. 리턴받은 엔티티ID를 날려 TodoEntity를 가져옴
        final Optional<TodoEntity> original = repository.findById(entity.getId());
        //3. 반환 된 TodoEntity가 존재하면 값을 새 entity값으로 덮는다
        if(original.isPresent())
        {
            final TodoEntity todo = original.get();
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            //4. DB에 새 값 저장
            repository.save(todo);
            //5. 사용자의 모든 Todo 리스트를 리턴
        }
        return retrieve(entity.getUserId());
    }

    public List<TodoEntity> delete(final TodoEntity entity)
    {
        validate(entity);

        try
        {
            repository.delete(entity);
        }
        catch(Exception e)
        {
            log.error("error deleting entity" , entity.getId(), e);

            throw new RuntimeException("error deleting entity" + entity.getId());
        }
        return retrieve(entity.getUserId());
    }
}
