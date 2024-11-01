function calcularTotal() {
    let total = 0;
    document.querySelectorAll('.form-check-input').forEach((checkbox, index) => {
        if (checkbox.checked) {
            const cantidad = parseFloat(document.querySelector(`input[name="detalles[${index}].cantidad"]`).value) || 0;
            const precio = parseFloat(checkbox.parentElement.querySelector('.form-check-label').textContent.match(/\$\d+(\.\d+)?/)[0].substring(1));
            total += cantidad * precio;
        }
    });
    document.getElementById('total').value = `$${total.toFixed(2)}`;
}

