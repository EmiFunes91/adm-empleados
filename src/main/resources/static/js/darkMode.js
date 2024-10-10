// darkMode.js

const body = document.getElementById('body');
const navbar = document.getElementById('navbar');
const darkModeToggle = document.getElementById('darkModeToggle');
const darkModeIcon = document.getElementById('darkModeIcon');

// Función para habilitar el modo oscuro
function enableDarkMode() {
    body.classList.add('dark-mode');
    navbar.classList.remove('bg-primary');
    navbar.classList.add('bg-dark');
    darkModeIcon.classList.replace('fa-sun', 'fa-moon');
    localStorage.setItem('darkMode', 'enabled');
}

// Función para deshabilitar el modo oscuro
function disableDarkMode() {
    body.classList.remove('dark-mode');
    navbar.classList.remove('bg-dark');
    navbar.classList.add('bg-primary');
    darkModeIcon.classList.replace('fa-moon', 'fa-sun');
    localStorage.setItem('darkMode', 'disabled');
}

// Cargar la preferencia de modo oscuro
if (localStorage.getItem('darkMode') === 'enabled') {
    enableDarkMode();
} else {
    disableDarkMode();
}

// Cambiar el modo oscuro y guardar la preferencia
darkModeToggle.addEventListener('click', () => {
    if (body.classList.contains('dark-mode')) {
        disableDarkMode();
    } else {
        enableDarkMode();
    }
});





