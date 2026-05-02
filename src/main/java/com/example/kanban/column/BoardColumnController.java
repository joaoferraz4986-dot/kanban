package com.example.kanban.column;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/columns")
public class BoardColumnController {

    private final BoardColumnRepository repository;

    public BoardColumnController( BoardColumnRepository repository ) {
        this.repository = repository;
    }

    @GetMapping
    public List<BoardColumn> findAll( @RequestParam(required = false) Long boardId ) {
        if ( boardId != null ) {
            return repository.findByBoardId( boardId );
        }
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardColumn> findById( @PathVariable Long id ) {
        return repository.findById( id )
                .map( ResponseEntity::ok )
                .orElse( ResponseEntity.notFound().build() );
    }

    @PostMapping
    public BoardColumn create( @RequestBody BoardColumn column ) {
        return repository.save(column);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardColumn> update( @PathVariable Long id, @RequestBody BoardColumn column ) {
        return repository.findById( id ).map( existing -> {
            existing.setName(         column.getName() );
            existing.setBoardId(      column.getBoardId() );
            existing.setPosition(     column.getPosition() );
            return ResponseEntity.ok( repository.save(existing) );
        }).orElse( ResponseEntity.notFound().build() );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete( @PathVariable Long id ) {
        if ( !repository.existsById(id) ) return ResponseEntity.notFound().build();
        repository.deleteById( id );
        return ResponseEntity.noContent().build();
    }
}
