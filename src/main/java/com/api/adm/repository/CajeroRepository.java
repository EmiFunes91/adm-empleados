package com.api.adm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.adm.entity.Cajero;

public interface CajeroRepository extends JpaRepository<Cajero, Long> {
}

