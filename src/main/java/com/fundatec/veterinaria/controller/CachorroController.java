package com.fundatec.veterinaria.controller;

import com.fundatec.veterinaria.domain.Cachorro;
import com.fundatec.veterinaria.domain.Veterinario;
import com.fundatec.veterinaria.repository.CachorroRepository;
import com.fundatec.veterinaria.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cachorros")
public class CachorroController {

    @Autowired
    private CachorroRepository cachorroRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @GetMapping("{id}")
    public ResponseEntity<Cachorro> retornaCachorro(@PathVariable("id") Long id) {
        Optional<Cachorro> resultado = cachorroRepository.findById(id);

        if(resultado.isPresent()) {
            return new ResponseEntity<Cachorro>(resultado.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<Cachorro>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping
    public ResponseEntity<List<Cachorro>> findAllByNome(@RequestParam(value = "nome", required = false) String nome) {
        List<Cachorro> resultado;

        //validar nome
        if (nome != null) {
            Optional<Cachorro> cachorro = cachorroRepository.findByNome(nome);

            if(cachorro.isPresent()) {
                resultado = Collections.singletonList(cachorro.get());
            } else {
                resultado = Collections.emptyList();
            }
        } else {
            resultado = cachorroRepository.findAll();
        }

        return new ResponseEntity(resultado, HttpStatus.OK);
    }




    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity salvaCachorro(@RequestBody Cachorro cachorro) {
        cachorroRepository.save(cachorro);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") long id,
                                 @RequestBody Cachorro cachorro) {
        return cachorroRepository.findById(id)
                .map(record -> {
                    record.setNome(cachorro.getNome());
                    record.setIdade(cachorro.getIdade());
                    record.setPeso(cachorro.getPeso());
                    record.setRaca(cachorro.getRaca());
                    Cachorro updated = cachorroRepository.save(record);
                    return ResponseEntity.ok().body(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable("id") long id) {
        cachorroRepository.deleteById(id);
        return "deleted";
    }

    @PatchMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity adicionaVeterinario(
            @RequestBody VeterinarioRequest veterinarioRequest,
            @PathVariable Long id
    ) {
        Optional<Cachorro> cachorroFromDB = cachorroRepository.findById(id);

        if (cachorroFromDB.isPresent()) {
            Cachorro cachorro = cachorroFromDB.get();

            Optional<Veterinario> veterinarioFromDB = veterinarioRepository.findById(veterinarioRequest.getId());

            if(veterinarioFromDB.isPresent()) {
                Veterinario veterinario = veterinarioFromDB.get();

                cachorro.setVeterinario(veterinario);

                cachorroRepository.save(cachorro);
                return new ResponseEntity(HttpStatus.OK);
            }
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    }
