document.getElementById('buscarProducto').addEventListener('input', filtrarProductos);

function filtrarProductos() {
    const query = document.getElementById('buscarProducto').value.toLowerCase();
    const productos = document.querySelectorAll('#listaProductos li');

    productos.forEach(producto => {
        const productoTexto = producto.textContent.toLowerCase();
        producto.style.display = productoTexto.includes(query) ? 'block' : 'none';
    });
}

function agregarProducto(element) {
    const id = element.getAttribute('data-id');
    const nombre = element.textContent.split(' - $')[0];
    const precio = parseFloat(element.getAttribute('data-precio'));
    const stock = parseInt(element.getAttribute('data-stock'));

    const productosSeleccionados = document.getElementById('productosSeleccionados');
    const nuevoProducto = document.createElement('tr');

    nuevoProducto.innerHTML = `
        <td>${nombre}</td>
        <td><input type="number" value="1" min="1" max="${stock}" onchange="actualizarSubtotal(this, ${precio})"></td>
        <td>$${precio.toFixed(2)}</td>
        <td>$<span>${precio.toFixed(2)}</span></td>
        <td><button type="button" class="btn btn-danger btn-sm" onclick="eliminarProducto(this)">Eliminar</button></td>
    `;
    productosSeleccionados.appendChild(nuevoProducto);
    actualizarTotal();
}

function actualizarSubtotal(input, precio) {
    const cantidad = parseInt(input.value);
    const subtotal = precio * cantidad;
    input.parentElement.nextElementSibling.nextElementSibling.firstElementChild.textContent = subtotal.toFixed(2);
    actualizarTotal();
}

function eliminarProducto(button) {
    button.closest('tr').remove();
    actualizarTotal();
}

function actualizarTotal() {
    const subtotales = document.querySelectorAll('#productosSeleccionados tr td:nth-child(4) span');
    let total = Array.from(subtotales).reduce((acc, subtotal) => acc + parseFloat(subtotal.textContent), 0);
    document.getElementById('total').value = `$${total.toFixed(2)}`;
}

