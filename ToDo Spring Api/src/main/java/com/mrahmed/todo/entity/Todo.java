package com.mrahmed.todo.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @Project: todo
 * @Author: M. R. Ahmed
 * @Created at: 12/12/2024
 */
@Entity
//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "todos")
public class Todo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String date;

    private String type;

    private String priority;

}
