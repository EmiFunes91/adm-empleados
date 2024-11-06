function buscarProductosAJAX() {
    const query = document.getElementById("query").value.trim();
    const productosTableBody = document.getElementById("productosTableBody");

    if (query === "") {
        // Si no hay texto, recargar la página para mostrar todos los productos activos
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
                        <form action="/api/productos/${producto.id}" method="post" style="display:inline;" onsubmit="return confirm('¿Estás seguro de que deseas eliminar este producto?')">
                            <input type="hidden" name="_method" value="delete">
                            <button type="submit" class="btn btn-danger btn-sm">Eliminar</button>
                        </form>
                    </td>
                `;
                productosTableBody.appendChild(row);
            });
        })
        .catch(error => console.error("Error al buscar productos:", error));
}




