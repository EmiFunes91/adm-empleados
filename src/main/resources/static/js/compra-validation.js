const selectedProducts = new Map();
let totalAmount = 0;

function updateHiddenFields() {
    const container = document.getElementById("hiddenFieldsContainer");
    container.innerHTML = "";

    selectedProducts.forEach((cantidad, id) => {
        const productIdInput = document.createElement("input");
        productIdInput.type = "hidden";
        productIdInput.name = "productoIds";
        productIdInput.value = id;
        container.appendChild(productIdInput);

        const cantidadInput = document.createElement("input");
        cantidadInput.type = "hidden";
        cantidadInput.name = "cantidades";
        cantidadInput.value = cantidad;
        container.appendChild(cantidadInput);
    });
}

function updateTotal() {
    totalAmount = 0;
    document.querySelectorAll(".product-quantity").forEach((input) => {
        const cantidad = parseInt(input.value);
        const precio = parseFloat(input.getAttribute("data-price"));
        const subtotalCell = input.closest("tr").querySelector(".subtotal");
        const productId = input.closest("tr").getAttribute("data-product-id");

        if (!isNaN(cantidad) && !isNaN(precio)) {
            const subtotal = cantidad * precio;
            subtotalCell.textContent = subtotal.toFixed(2);
            totalAmount += subtotal;
            selectedProducts.set(productId, cantidad);
        }
    });
    document.getElementById("total").value = totalAmount.toFixed(2);
    updateHiddenFields();
}

function addProduct(product) {
    if (selectedProducts.has(product.id)) {
        alert("El producto ya está en la lista. Modifique la cantidad directamente.");
        return;
    }

    const productList = document.getElementById("productosSeleccionados");
    const row = document.createElement("tr");
    row.setAttribute("data-product-id", product.id);

    row.innerHTML = `
        <td>${product.nombre}</td>
        <td><input type="number" class="product-quantity form-control" min="1" max="${product.stock}"
                   value="1" data-price="${product.precio}" oninput="updateTotal()"></td>
        <td>${product.precio.toFixed(2)}</td>
        <td class="subtotal">${product.precio.toFixed(2)}</td>
        <td><button type="button" class="btn btn-danger" onclick="removeProduct(this, '${product.id}')">Eliminar</button></td>
    `;

    productList.appendChild(row);
    selectedProducts.set(product.id, 1);
    updateTotal();
}

function removeProduct(button, productId) {
    const row = button.closest("tr");
    row.remove();
    selectedProducts.delete(productId);
    updateTotal();
}

function filtrarProductos() {
    const query = document.getElementById("buscarProducto").value;
    if (query.trim() === "") return;

    fetch(`/api/productos/buscar?query=${query}`)
        .then(response => response.json())
        .then(data => {
            const listaProductos = document.getElementById("listaProductos");
            listaProductos.innerHTML = "";

            data.forEach(producto => {
                const item = document.createElement("li");
                item.className = "list-group-item";
                item.textContent = `${producto.nombre} - ${producto.categoria}`;
                item.onclick = () => addProduct(producto);
                listaProductos.appendChild(item);
            });
        })
        .catch(console.error);
}





