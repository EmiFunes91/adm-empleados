package com.api.adm.service;

import com.api.adm.entity.Cliente;
import com.api.adm.exception.ResourceNotFoundException;
import com.api.adm.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para manejar la lógica de negocio de la entidad Cliente.
 */
@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Busca clientes por nombre o apellido, ignorando mayúsculas y minúsculas.
     *
     * @param keyword palabra clave de búsqueda.
     * @return lista de clientes que coinciden con el nombre o apellido.
     */
    public List<Cliente> buscarClientesPorNombreOApellido(String keyword) {
        return clienteRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(keyword, keyword);
    }

    /**
     * Obtiene todos los clientes registrados.
     *
     * @return lista de todos los clientes.
     */
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    /**
     * Obtiene un cliente por su ID.
     *
     * @param id identificador del cliente.
     * @return el cliente encontrado.
     * @throws ResourceNotFoundException si el cliente no es encontrado.
     */
    public Cliente obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con ID " + id + " no encontrado"));
    }

    /**
     * Crea un nuevo cliente en la base de datos.
     *
     * @param cliente datos del cliente a crear.
     * @return cliente creado.
     */
    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param id identificador del cliente a actualizar.
     * @param clienteActualizado datos actualizados del cliente.
     * @return cliente actualizado.
     * @throws ResourceNotFoundException si el cliente no es encontrado.
     */
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        Cliente clienteExistente = obtenerClientePorId(id);
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setApellido(clienteActualizado.getApellido());
        clienteExistente.setCorreo(clienteActualizado.getCorreo());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());
        clienteExistente.setDireccion(clienteActualizado.getDireccion());
        return clienteRepository.save(clienteExistente);
    }

    /**
     * Elimina un cliente de la base de datos.
     *
     * @param id identificador del cliente a eliminar.
     * @throws ResourceNotFoundException si el cliente no es encontrado.
     */
    public void eliminarCliente(Long id) {
        Cliente cliente = obtenerClientePorId(id);
        clienteRepository.delete(cliente);
    }
}




