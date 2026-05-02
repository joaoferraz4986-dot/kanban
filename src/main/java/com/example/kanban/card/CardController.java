package com.example.kanban.card;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardRepository repository;

    public CardController( CardRepository repository ) {
        this.repository = repository;
    }

    @GetMapping
    public List<Card> findAll( @RequestParam(required = false) Long columnId ) {
        if ( columnId != null ) {
            return repository.findByColumnId( columnId );
        }
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> findById( @PathVariable Long id ) {
        return repository.findById( id )
                .map( ResponseEntity::ok )
                .orElse( ResponseEntity.notFound().build() );
    }

    @PostMapping
    public Card create( @RequestBody Card card ) {
        return repository.save( card );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Card> update( @PathVariable Long id, @RequestBody Card card ) {
        return repository.findById( id ).map( existing -> {
            existing.setTitle(         card.getTitle() );
            existing.setDescription(   card.getDescription() );
            existing.setColumnId(      card.getColumnId() );
            existing.setPosition(      card.getPosition() );
            return ResponseEntity.ok(  repository.save(existing) );
        }).orElse(ResponseEntity.notFound().build() );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete( @PathVariable Long id ) {
        if ( !repository.existsById(id) ) return ResponseEntity.notFound().build();
        repository.deleteById( id );
        return ResponseEntity.noContent().build();
    }
}
