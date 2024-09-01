package com.gov.sistem.reservation.login.jpa.repository;

import com.gov.sistem.reservation.commons.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, String> {

    ClienteEntity findByCorreoCliente(String correo);

    List<ClienteEntity> findAllByCorreoCliente(String correo);

}
