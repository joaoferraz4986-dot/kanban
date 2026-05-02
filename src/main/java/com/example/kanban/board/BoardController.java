package com.example.kanban.board;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardRepository repository;

    public BoardController( BoardRepository repository ) {
        this.repository = repository;
    }

    @GetMapping
    public List<Board> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> findById( @PathVariable Long id ) {
        return repository.findById( id )
                .map( ResponseEntity::ok )
                .orElse( ResponseEntity.notFound().build() );
    }

    @PostMapping
    public Board create( @RequestBody Board board ) {
        return repository.save(board);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Board> update( @PathVariable Long id, @RequestBody Board board ) {
        return repository.findById( id ).map(existing -> {
            existing.setName( board.getName() );
            return ResponseEntity.ok( repository.save(existing) );
        }).orElse( ResponseEntity.notFound().build() );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if ( !repository.existsById(id) ) return ResponseEntity.notFound().build();
        repository.deleteById( id );
        return ResponseEntity.noContent().build();
    }
}
