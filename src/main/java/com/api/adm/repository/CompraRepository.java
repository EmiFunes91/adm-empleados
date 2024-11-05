package com.api.adm.repository;

import com.api.adm.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    @Query("SELECT c FROM Compra c LEFT JOIN FETCH c.detalles WHERE c.id = :id")
    Optional<Compra> findByIdWithDetalles(Long id);

    @Query("SELECT c FROM Compra c LEFT JOIN FETCH c.detalles")
    List<Compra> findAllWithDetalles();
}


