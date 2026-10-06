package com.flashcard.flashcard.controller;

import com.flashcard.flashcard.model.Flashcard;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {

    private final List<Flashcard> flashcards = new ArrayList<>();

    @GetMapping
    public List<Flashcard> getAllFlashcards() {
        return flashcards;
    }

    @GetMapping("/{id}")
    public Flashcard getFlashcard(@PathVariable Long id) {

        for (Flashcard flashcard : flashcards) {
            if (flashcard.getId().equals(id)) {
                return flashcard;
            }
        }

        return null;
    }

    @PostMapping
    public Flashcard addFlashcard(@RequestBody Flashcard flashcard) {

        flashcards.add(flashcard);

        return flashcard;
    }

    @DeleteMapping("/{id}")
    public String deleteFlashcard(@PathVariable Long id) {

        flashcards.removeIf(flashcard ->
                flashcard.getId().equals(id));

        return "Flashcard deleted successfully";
    }
}