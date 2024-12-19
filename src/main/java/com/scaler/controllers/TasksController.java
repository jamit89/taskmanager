package com.scaler.controllers;

import java.text.ParseException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scaler.dto.CreateTaskDTO;
import com.scaler.dto.ErrorResponseDTO;
import com.scaler.dto.TaskResponseDTO;
import com.scaler.dto.UpdateTaskDTO;
import com.scaler.entities.TaskEntity;
import com.scaler.service.NotesService;
import com.scaler.service.TaskService;

@RestController
@RequestMapping("/tasks")
public class TasksController {
	private final TaskService taskService;
	private final NotesService notesService;
	private ModelMapper modelMapper = new ModelMapper();

	public TasksController(TaskService taskService, NotesService notesService) {
		this.taskService = taskService;
		this.notesService = notesService;
	}

	@GetMapping
	public ResponseEntity<List<TaskEntity>> getTasks() {
		var tasks = taskService.getTasks();
		return ResponseEntity.ok(tasks);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable("id") Integer id) {
		var task = taskService.getTaskById(id);
		var notes = notesService.getNotesForTask(id);
		if (task == null) {
			return ResponseEntity.notFound().build();
		}
		var taskResponse = modelMapper.map(task, TaskResponseDTO.class);
		taskResponse.setNotes(notes);
		return ResponseEntity.ok(taskResponse);
	}

	@PostMapping("")
	public ResponseEntity<TaskEntity> addTask(@RequestBody CreateTaskDTO body) throws ParseException {
		var task = taskService.addTask(body.getTitle(), body.getDescription(), body.getDeadline());

		return ResponseEntity.ok(task);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<TaskEntity> updateTask(@PathVariable("id") Integer id, @RequestBody UpdateTaskDTO body)
			throws ParseException {
		var task = taskService.updateTask(id, body.getDescription(), body.getDeadline(), body.getCompleted());

		if (task == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(task);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDTO> handleErrors(Exception e) {
		if (e instanceof ParseException) {
			return ResponseEntity.badRequest().body(new ErrorResponseDTO("Invalid date format"));
		}
		e.printStackTrace();
		return ResponseEntity.internalServerError().body(new ErrorResponseDTO("Internal Server Error"));
	}

}
