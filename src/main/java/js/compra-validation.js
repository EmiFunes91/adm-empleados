function validarFormulario() {
        const clienteSelect = document.getElementById("cliente");
        const clienteError = document.getElementById("clienteError");

        if (!clienteSelect.value) {
            clienteError.style.display = "block";
            return false;
        }
        clienteError.style.display = "none";
        return true;
    }
ç