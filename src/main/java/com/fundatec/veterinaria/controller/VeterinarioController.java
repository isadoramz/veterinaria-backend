package com.fundatec.veterinaria.controller;

import com.fundatec.veterinaria.domain.Cachorro;
import com.fundatec.veterinaria.domain.Veterinario;
import com.fundatec.veterinaria.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/veterinarios")
public class VeterinarioController {

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @GetMapping("{id}")
    public ResponseEntity<Veterinario> retornaVeterinario(@PathVariable("id") Long id) {
        Optional<Veterinario> resultado = veterinarioRepository.findById(id);

        if(resultado.isPresent()) {
            return new ResponseEntity<Veterinario>(resultado.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<Veterinario>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("{id}/cachorros")
    public ResponseEntity<List<Cachorro>> findAllCachorros(@PathVariable("id") Long id) {
      List<Cachorro> resultado = veterinarioRepository.findCachorrosById(id);
      if(!resultado.isEmpty()) {
          return new ResponseEntity<>(resultado, HttpStatus.OK);
      } else {
          return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
    }

    @GetMapping
    public ResponseEntity<List<Veterinario>> findAllByNome(@RequestParam(value = "nome", required = false) String nome) {
        List<Veterinario> resultado;

        //validar nome
        if (nome != null) {
            Optional<Veterinario> veterinario = veterinarioRepository.findByNome(nome);

            if(veterinario.isPresent()) {
                resultado = Collections.singletonList(veterinario.get());
            } else {
                resultado = Collections.emptyList();
            }
        } else {
            resultado = veterinarioRepository.findAll();
        }

        return new ResponseEntity(resultado, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity salvaVeterinario(@RequestBody Veterinario veterinario) {
        veterinarioRepository.save(veterinario);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable("id") long id) {
        veterinarioRepository.deleteById(id);
        return "deleted";
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") long id,
                                 @RequestBody Veterinario veterinario) {
        return veterinarioRepository.findById(id)
                .map(record -> {
                    record.setNome(veterinario.getNome());
                    record.setCpf(veterinario.getCpf());
                    Veterinario updated = veterinarioRepository.save(record);
                    return ResponseEntity.ok().body(updated);
                }).orElse(ResponseEntity.notFound().build());
    }
}
