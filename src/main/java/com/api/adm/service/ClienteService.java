package com.api.adm.service;

import com.api.adm.entity.Cliente;
import com.api.adm.exception.ResourceNotFoundException;
import com.api.adm.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> buscarClientesPorNombreOApellido(String keyword) {
        return clienteRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(keyword, keyword);
    }

    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    public Cliente obtenerClientePorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
    }

    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        Cliente clienteExistente = obtenerClientePorId(id);
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setApellido(clienteActualizado.getApellido());
        clienteExistente.setDni(clienteActualizado.getDni());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());
        clienteExistente.setCorreo(clienteActualizado.getCorreo());
        clienteExistente.setDireccion(clienteActualizado.getDireccion());
        clienteExistente.setEstado(clienteActualizado.getEstado()); // Actualiza el estado
        return clienteRepository.save(clienteExistente);
    }

    public void eliminarCliente(Long id) {
        Cliente cliente = obtenerClientePorId(id);
        clienteRepository.delete(cliente);
    }
}





