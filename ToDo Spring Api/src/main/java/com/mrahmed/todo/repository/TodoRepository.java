package com.mrahmed.todo.repository;

import com.mrahmed.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Project: todo
 * @Author: M. R. Ahmed
 * @Created at: 12/12/2024
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
