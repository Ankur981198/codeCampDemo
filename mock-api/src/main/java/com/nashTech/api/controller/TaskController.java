package com.nashTech.api.controller;

import com.nashTech.api.model.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private List<Task> tasks = new ArrayList<>();
    private AtomicLong nextId = new AtomicLong(1);

    public TaskController() {
        // Initialize with demo data
        tasks.add(new Task(nextId.getAndIncrement(), "Setup Docker", "Configure Docker for testing", false));
        tasks.add(new Task(nextId.getAndIncrement(), "Write Tests", "Create automation tests", false));
        tasks.add(new Task(nextId.getAndIncrement(), "Demo Prep", "Prepare demo presentation", true));

        System.out.println("📋 Mock API initialized with " + tasks.size() + " tasks");
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        System.out.println("📋 GET /api/tasks - Returning " + tasks.size() + " tasks");
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        System.out.println("🔍 GET /api/tasks/" + id);

        Task task = findTaskById(id);
        if (task != null) {
            return ResponseEntity.ok(task);
        }

        System.out.println("❌ Task " + id + " not found");
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        System.out.println("✏️ POST /api/tasks - Creating: " + task.getTitle());

        task.setId(nextId.getAndIncrement());
        tasks.add(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        System.out.println("🔄 PUT /api/tasks/" + id + " - Updating: " + updatedTask.getTitle());

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.getId().equals(id)) {
                updatedTask.setId(id);
                tasks.set(i, updatedTask);
                return ResponseEntity.ok(updatedTask);
            }
        }

        System.out.println("❌ Task " + id + " not found for update");
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        System.out.println("🗑️ DELETE /api/tasks/" + id);

        boolean removed = tasks.removeIf(task -> task.getId().equals(id));
        if (removed) {
            return ResponseEntity.noContent().build();
        }

        System.out.println("❌ Task " + id + " not found for deletion");
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Mock API is running! 🚀");
    }

    private Task findTaskById(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}