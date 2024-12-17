package com.mrahmed.todo.ApiSocket;

import com.mrahmed.todo.Model.TodoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @Project: Todo
 * @Author: Md. Raju Ahmed
 * @Created at: 12/13/2024
 */
public interface TodoApi {
    @POST("todos/")
    Call<Void> createTodo(@Body TodoModel todo);

    @GET("todos/")
    Call<List<TodoModel>> getTodos();

}
