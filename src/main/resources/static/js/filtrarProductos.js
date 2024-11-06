// Simular productos cargados (si se usan datos estáticos)
let productos = [
    {id: 1, nombre: "Producto 1", categoria: "Categoría A", precio: 100.0, stock: 10},
    {id: 2, nombre: "Producto 2", categoria: "Categoría B", precio: 200.0, stock: 20},
    // Agregar más productos según sea necesario
];

// Filtrar productos por nombre o categoría
function filtrarProductos() {
    const query = document.getElementById("buscarProducto").value.toLowerCase();
    const listaProductos = document.getElementById("listaProductos");
    listaProductos.innerHTML = "";

    const resultados = productos.filter(producto =>
        producto.nombre.toLowerCase().includes(query) ||
        producto.categoria.toLowerCase().includes(query)
    );

    resultados.forEach(producto => {
        const item = document.createElement("li");
        item.className = "list-group-item";
        item.setAttribute("data-id", producto.id);
        item.setAttribute("data-precio", producto.precio);
        item.setAttribute("data-stock", producto.stock);
        item.textContent = `${producto.nombre} - $${producto.precio} (Stock: ${producto.stock})`;
        item.onclick = () => agregarProducto(item);
        listaProductos.appendChild(item);
    });
}

// Agregar producto a la tabla de productos seleccionados
function agregarProducto(elemento) {
    const productoId = elemento.getAttribute("data-id");
    const productoPrecio = parseFloat(elemento.getAttribute("data-precio"));
    const productoStock = parseInt(elemento.getAttribute("data-stock"));
    const productoNombre = elemento.textContent.split(" - ")[0];

    if (productoStock <= 0) {
        alert("El producto no tiene stock disponible.");
        return;
    }

    const cantidadInput = document.createElement("input");
    cantidadInput.type = "number";
    cantidadInput.className = "form-control me-2";
    cantidadInput.value = 1;
    cantidadInput.min = 1;
    cantidadInput.max = productoStock;
    cantidadInput.oninput = updateTotal;

    const fila = document.createElement("tr");
    fila.innerHTML = `
        <td>${productoNombre}</td>
        <td></td>
        <td>$${productoPrecio.toFixed(2)}</td>
        <td>$<span class="subtotal">${productoPrecio.toFixed(2)}</span></td>
        <td><button class="btn btn-danger" onclick="removeProduct(this)">Eliminar</button></td>
    `;
    fila.cells[1].appendChild(cantidadInput);
    document.getElementById("productosSeleccionados").appendChild(fila);
    updateTotal();
}

// Actualizar el total
function updateTotal() {
    let total = 0;
    document.querySelectorAll("#productosSeleccionados tr").forEach(fila => {
        const cantidad = parseInt(fila.cells[1].querySelector("input").value) || 0;
        const precio = parseFloat(fila.cells[2].textContent.replace("$", "")) || 0;
        const subtotal = cantidad * precio;
        fila.querySelector(".subtotal").textContent = subtotal.toFixed(2);
        total += subtotal;
    });
    document.getElementById("total").value = total.toFixed(2);
}

// Eliminar producto
function removeProduct(button) {
    button.closest("tr").remove();
    updateTotal();
}




