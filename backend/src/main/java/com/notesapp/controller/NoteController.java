package com.notesapp.controller;

import com.notesapp.entity.Note;
import com.notesapp.repository.NoteRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@CrossOrigin(origins = {"http://localhost:63342", "http://localhost:5500", "http://127.0.0.1:5500", "http://localhost:3000"}, allowCredentials = "true")
public class NoteController {

    @Autowired
    private NoteRepository repository;

    @GetMapping
    public List<Note> getNotes(HttpSession session) {

        String username = (String) session.getAttribute("username");

        if (username == null) {
            throw new RuntimeException("User not logged in");
        }

        return repository.findByUsername(username);
    }

    @PostMapping
    public Note createNote(@RequestBody Note note,
                           HttpSession session) {

        String username = (String) session.getAttribute("username");

        if (username == null) {
            throw new RuntimeException("User not logged in");
        }

        note.setUsername(username);
        return repository.save(note);
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Long id,
                           @RequestBody Note updatedNote,
                           HttpSession session) {

        String username = (String) session.getAttribute("username");

        if (username == null) {
            throw new RuntimeException("User not logged in");
        }

        Note existingNote = repository.findById(id).orElse(null);

        if (existingNote != null &&
                existingNote.getUsername().equals(username)) {

            existingNote.setTitle(updatedNote.getTitle());
            existingNote.setContent(updatedNote.getContent());

            return repository.save(existingNote);
        }

        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id,
                           HttpSession session) {

        String username = (String) session.getAttribute("username");

        if (username == null) {
            throw new RuntimeException("User not logged in");
        }

        Note note = repository.findById(id).orElse(null);

        if (note != null &&
                note.getUsername().equals(username)) {

            repository.delete(note);

        }
    }
}