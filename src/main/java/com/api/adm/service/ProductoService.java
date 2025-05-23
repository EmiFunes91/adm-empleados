package com.api.adm.service;

import com.api.adm.entity.Producto;
import java.util.List;

public interface ProductoService {
    List<Producto> obtenerTodosLosProductos();
    Producto obtenerProductoPorId(Long id);
    Producto guardarProducto(Producto producto);
    Producto actualizarProducto(Long id, Producto producto);
    void eliminarProducto(Long id);
    void reducirStock(Long productoId, int cantidad);
    void aumentarStock(Long productoId, int cantidad);
    List<Producto> buscarProductos(String query);
    List<Producto> obtenerProductosActivos();
}

















