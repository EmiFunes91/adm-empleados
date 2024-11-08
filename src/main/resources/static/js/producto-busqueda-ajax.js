function buscarProductosAJAX() {
  const query = document.getElementById("query").value;
  const csrfToken = document.querySelector('meta[name="_csrf"]').content;

  fetch(`/api/productos/buscar?query=${query}`, {
    method: 'GET',
    headers: {
      'X-CSRF-TOKEN': csrfToken,
      'Content-Type': 'application/json'
    }
  })
  .then(response => response.json())
  .then(data => {
    const productosTableBody = document.getElementById("productosTableBody");
    productosTableBody.innerHTML = "";
    data.forEach(producto => {
      productosTableBody.innerHTML += `
        <tr id="producto-${producto.id}">
          <td>${producto.id}</td>
          <td>${producto.nombre}</td>
          <td>${producto.categoria}</td>
          <td>${producto.precio}</td>
          <td>${producto.stock}</td>
          <td><img src="/images/${producto.imagenUrl}" alt="Imagen del producto" width="100"></td>
          <td>
            <a class="btn btn-primary btn-sm me-1" href="/productos/editar/${producto.id}">Editar</a>
            <button class="btn btn-danger btn-sm" onclick="confirmarEliminacion(${producto.id})">Eliminar</button>
          </td>
        </tr>`;
    });
  })
  .catch(error => console.error('Error en la búsqueda:', error));
}

// Función para confirmar y eliminar un producto
function confirmarEliminacion(id) {
  if (confirm("¿Estás seguro de que quieres eliminar este producto?")) {
    eliminarProducto(id);
  }
}

// Función para manejar la solicitud de eliminación
function eliminarProducto(id) {
  const csrfToken = document.querySelector('meta[name="_csrf"]').content;

  fetch(`/productos/eliminar/${id}`, {
    method: 'DELETE',
    headers: {
      'X-CSRF-TOKEN': csrfToken,
      'Content-Type': 'application/json'
    }
  })
  .then(response => {
    if (response.ok) {
      alert("Producto eliminado exitosamente.");
      // Actualizar la lista de productos después de la eliminación
      buscarProductosAJAX();
    } else {
      alert("Error al eliminar el producto.");
    }
  })
  .catch(error => console.error("Error en la solicitud de eliminación:", error));
}









