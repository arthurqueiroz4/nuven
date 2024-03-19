package com.java.nuven.infra.repository;

import com.java.nuven.application.dto.Params;
import com.java.nuven.domain.entity.Todo;
import com.java.nuven.domain.repository.TodoRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class PostgresTodoRepository implements TodoRepository {

    private final SpringTodoRepository todoRepository;

    public PostgresTodoRepository(SpringTodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public Optional<Todo> findById(UUID id) {
        return todoRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public Todo save(Todo entity) {
        return todoRepository.save(entity);
    }

    @Override
    public Page<Todo> findAll(Params params) {
        Pageable pageable = PageRequest.of(params.getPage(), params.getSize());
        Sort sort = Sort.by(Sort.Direction.ASC, "dueDate");
        return todoRepository.findAll(buildSpecification(params.getFilters()), pageable);
    }

    private Specification<Todo> buildSpecification(Map<String, Object> filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Object title = filters.get("title");
            if (Objects.nonNull(title)) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }

            Object description = filters.get("description");
            if (Objects.nonNull(description)) {
                predicates.add(criteriaBuilder.like(root.get("description"), "%" + description + "%"));
            }

            Object date = filters.get("date");
            if (Objects.nonNull(date)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                predicates.add(criteriaBuilder.equal(root.get("createdAt"), LocalDate.parse(date.toString(), formatter).atStartOfDay()));
            }

            predicates.add(criteriaBuilder.isNull(root.get("deletedAt")));
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), LocalDateTime.now()));
            query.orderBy(criteriaBuilder.asc(root.get("dueDate")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
