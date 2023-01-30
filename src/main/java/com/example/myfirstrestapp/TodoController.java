package com.example.myfirstrestapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class TodoController {


    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    // Get-Request für den TomCatServer
    @GetMapping("/todo")
    public ResponseEntity<Todo> hello(@RequestParam(value = "id")int id) {
        // get todo from db by id
        Optional<Todo> todoInDb = todoRepository.findById(id);
        if(todoInDb.isPresent()) {
            return new ResponseEntity<Todo>(todoInDb.get(), HttpStatus.OK) ;
        } else {
            return new ResponseEntity("No todo found with id " + id, HttpStatus.NOT_FOUND);
        }



    }

    @GetMapping("/todoId")
    public String id(@RequestParam(value = "id")int id) {
        if(todoRepository.existsById(id)) {
            return "Ist da";
        }else {
            return "Ist nicht da";
        }
    }

    @PostMapping("/todo")
        //Es wird newTodos aus dem Rest-Api-Call übergeben und auf die Todo-Klasse geparst
        public ResponseEntity<Todo> create(@RequestBody Todo newTodos) {
            // save todo in database
            todoRepository.save(newTodos);
            // Hier wird einfach nur zurückgeschickt um zu prüfen ob es generell funktioniert
            return new ResponseEntity<Todo>(newTodos, HttpStatus.OK);
        }

    @GetMapping("/todo/all")
        public ResponseEntity<Iterable<Todo>>getAll(@RequestHeader("api-secret")String secret) {
        System.out.println("secret");

        var userBySecret = userRepository.findBySecret(secret);
        Iterable<Todo> allTodosInDB = todoRepository.findByUserId(userBySecret.get().getId());
        return new ResponseEntity<Iterable<Todo>>(allTodosInDB, HttpStatus.OK);

    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam(value = "id") int id) {
        Optional<Todo> todoInDB = todoRepository.findById(id);
        if(todoInDB.isPresent()) {
            todoRepository.deleteById(id);
            return new ResponseEntity("Todo Deleted", HttpStatus.CREATED);
        }
        return new ResponseEntity("No Todo found with id " + id,HttpStatus.NOT_FOUND);


    }
    // Komplette Entität austauschen
    @PutMapping("/todo")
    public ResponseEntity<Todo> edit(@RequestBody Todo editedTodo) {
        Optional<Todo> todoInDB = todoRepository.findById(editedTodo.getId());
        if(todoInDB.isPresent()) {
            //update
            Todo savedTodo = todoRepository.save(editedTodo);
            return new ResponseEntity<Todo>(savedTodo,HttpStatus.OK);
        }
        return new ResponseEntity("No todo to update found with id " + editedTodo,HttpStatus.NOT_FOUND);
    }

    // Nur einen Teil der Entität austauschen
    @PatchMapping("/todo/setDone")
    public ResponseEntity<Todo> setDone(@RequestParam(value = "isDone") boolean isDone,
                                        @RequestParam(value = "id")int id) {
        Optional<Todo> todoInDB = todoRepository.findById(id);
        if(todoInDB.isPresent()) {
            todoInDB.get().setIsDone(isDone);
            Todo savedTodo = todoRepository.save(todoInDB.get());
            return new ResponseEntity<Todo>(savedTodo,HttpStatus.OK);
        }
        return new ResponseEntity("No todo to update found with id " + id,HttpStatus.NOT_FOUND);
    }

}
