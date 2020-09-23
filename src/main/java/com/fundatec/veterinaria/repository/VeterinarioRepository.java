package com.fundatec.veterinaria.repository;

import com.fundatec.veterinaria.domain.Cachorro;
import com.fundatec.veterinaria.domain.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {
    Optional<Veterinario> findByNome(String nome);

    @Query(
            value = "SELECT cac FROM Cachorro cac where cac.veterinario = :id"
    )
    List<Cachorro> findCachorrosById(Long id);
}
