document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('/auth/api/login', {  // URL para la API JWT
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('jwtToken', data.jwt); // Guardar el token en localStorage
            window.location.href = '/dashboard';  // Redirige a la página del dashboard
        } else if (response.status === 401) {
            // Mostrar mensaje de error si las credenciales son incorrectas
            document.getElementById('error-message').classList.remove('d-none');
        }
    } catch (error) {
        console.error('Error durante la autenticación:', error);
        document.getElementById('error-message').classList.remove('d-none');
    }
});



