// Almacenar productos seleccionados
const selectedProducts = new Set();
let totalAmount = parseFloat(document.getElementById("totalAmount").textContent);

// Actualizar el total cuando cambia la cantidad
function updateTotal() {
    totalAmount = 0;
    document.querySelectorAll(".product-quantity").forEach((input) => {
        const cantidad = parseInt(input.value);
        const precio = parseFloat(input.getAttribute("data-price"));
        if (!isNaN(cantidad) && !isNaN(precio)) {
            totalAmount += cantidad * precio;
        }
    });
    document.getElementById("totalAmount").textContent = totalAmount.toFixed(2);
}

// Validar que el producto nuevo no esté ya seleccionado
function validateNewProductSelection() {
    const newProductSelect = document.getElementById("nuevoProductoId");
    const selectedProductId = newProductSelect.value;

    if (selectedProducts.has(selectedProductId)) {
        alert("El producto ya está en la compra. Modifique la cantidad directamente.");
        newProductSelect.value = "";
    }
}

// Agregar producto nuevo a la lista
function addProduct() {
    const newProductSelect = document.getElementById("nuevoProductoId");
    const selectedProductId = newProductSelect.value;
    const selectedProductName = newProductSelect.options[newProductSelect.selectedIndex].text;
    const maxStock = parseInt(newProductSelect.options[newProductSelect.selectedIndex].getAttribute("data-stock"));
    const precio = parseFloat(newProductSelect.options[newProductSelect.selectedIndex].getAttribute("data-price"));

    if (!selectedProductId) {
        alert("Seleccione un producto válido.");
        return;
    }

    selectedProducts.add(selectedProductId);

    const productList = document.getElementById("productList");
    const productItem = document.createElement("div");
    productItem.classList.add("d-flex", "mb-2", "align-items-center", "product-item");
    productItem.innerHTML = `
        <input type="text" class="form-control me-2" value="${selectedProductName}" readonly>
        <input type="number" class="form-control me-2 product-quantity" min="1" max="${maxStock}"
               value="1" data-price="${precio}" oninput="updateTotal()" required>
        <button type="button" class="btn btn-danger" onclick="removeProduct(this)">Eliminar</button>
    `;

    productList.appendChild(productItem);
    updateTotal();

    newProductSelect.value = "";
}

// Remover producto
function removeProduct(button) {
    const productItem = button.closest(".product-item");
    const productName = productItem.querySelector("input[type='text']").value;

    for (const option of document.getElementById("nuevoProductoId").options) {
        if (option.text === productName) {
            selectedProducts.delete(option.value);
            break;
        }
    }

    productItem.remove();
    updateTotal();
}


