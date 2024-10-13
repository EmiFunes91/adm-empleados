document.addEventListener('DOMContentLoaded', function () {
    const passwordInput = document.getElementById('password');
    const passwordHelp = document.getElementById('passwordHelp');

    passwordInput.addEventListener('input', () => {
        if (passwordInput.value) {
            passwordHelp.style.display = 'block';
        } else {
            passwordHelp.style.display = 'none';
        }
    });

    function validarContrasena() {
        const password = passwordInput.value;
        const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_\-+=<>?]).{8,}$/;

        if (!regex.test(password)) {
            passwordHelp.classList.add('text-danger');
            passwordHelp.textContent = 'La contraseña debe tener al menos una letra mayúscula, un número y un símbolo.';
            return false;
        } else {
            passwordHelp.classList.remove('text-danger');
            passwordHelp.classList.add('text-success');
            passwordHelp.textContent = 'La contraseña cumple con los requisitos.';
            return true;
        }
    }

    window.validarContrasena = validarContrasena;
});



