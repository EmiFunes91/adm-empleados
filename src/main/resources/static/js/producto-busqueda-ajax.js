function buscarProductosAJAX() {
    const query = document.getElementById("query").value.trim();
    const productosTableBody = document.getElementById("productosTableBody");

    if (query === "") {
        location.reload();
        return;
    }

    fetch(`/api/productos/buscar?query=${query}`)
        .then(response => {
            if (!response.ok) throw new Error("Error en la búsqueda");
            return response.json();
        })
        .then(data => {
            productosTableBody.innerHTML = "";
            data.forEach(producto => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${producto.id}</td>
                    <td>${producto.nombre}</td>
                    <td>${producto.categoria}</td>
                    <td>${producto.precio}</td>
                    <td>${producto.stock}</td>
                    <td><img src="/images/${producto.imagenUrl}" alt="Imagen del producto" width="100"></td>
                    <td>
                        <a href="/productos/editar/${producto.id}" class="btn btn-primary btn-sm me-1">Editar</a>
                        <button class="btn btn-danger btn-sm" onclick="eliminarProducto(${producto.id})">Eliminar</button>
                    </td>
                `;
                productosTableBody.appendChild(row);
            });
        })
        .catch(error => console.error("Error al buscar productos:", error));
}

function eliminarProducto(id) {
    if (!confirm('¿Estás seguro de que deseas eliminar este producto?')) return;

    fetch(`/api/productos/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) throw new Error("Error al eliminar el producto");
        return response.text();
    })
    .then(() => {
        alert("Producto eliminado correctamente");
        buscarProductosAJAX(); // Para actualizar la lista tras la eliminación
    })
    .catch(error => console.error("Error al eliminar el producto:", error));
}





