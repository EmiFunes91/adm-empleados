// archivo: src/main/resources/static/js/registro.js

document.addEventListener('DOMContentLoaded', function () {
    const passwordInput = document.getElementById('password');
    const passwordHelp = document.getElementById('passwordHelp');
    const successAlert = document.querySelector('.alert-success');

    // Mostrar el mensaje de ayuda mientras se escribe la contraseña
    passwordInput.addEventListener('input', () => {
        if (passwordInput.value) {
            passwordHelp.style.display = 'block';
        } else {
            passwordHelp.style.display = 'none';
        }
    });

    // Función para validar la contraseña
    function validarContrasena() {
        const password = passwordInput.value;
        const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_\-+=<>?]).{8,}$/;

        if (!regex.test(password)) {
            passwordHelp.classList.add('text-danger');
            passwordHelp.textContent = 'La contraseña debe tener al menos una letra mayúscula, un número y un símbolo.';
            return false; // Previene el envío del formulario si la contraseña no es válida
        } else {
            passwordHelp.classList.remove('text-danger');
            passwordHelp.classList.add('text-success');
            passwordHelp.textContent = 'La contraseña cumple con los requisitos.';
            return true;
        }
    }

    // Mostrar la alerta de éxito si el parámetro 'success' está presente en la URL
    if (new URLSearchParams(window.location.search).has('success')) {
        successAlert.style.display = 'block';
        setTimeout(() => {
            successAlert.style.display = 'none';
        }, 5000); // La alerta desaparecerá después de 5 segundos
    }

    window.validarContrasena = validarContrasena; // Hacer la función accesible desde el HTML
});


