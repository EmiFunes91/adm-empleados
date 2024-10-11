// darkMode.js
const body = document.getElementById('body');
const darkModeToggle = document.getElementById('darkModeToggle');
const darkModeIcon = document.getElementById('darkModeIcon');
const navbar = document.getElementById('navbar');

// Cargar la preferencia de modo oscuro
if (localStorage.getItem('darkMode') === 'enabled') {
    enableDarkMode();
}

// Cambiar el modo oscuro y guardar la preferencia
darkModeToggle.addEventListener('click', () => {
    if (body.classList.contains('dark-mode')) {
        disableDarkMode();
    } else {
        enableDarkMode();
    }
});

function enableDarkMode() {
    body.classList.add('dark-mode');
    navbar.classList.replace('bg-primary', 'bg-dark');
    darkModeIcon.classList.replace('fa-sun', 'fa-moon');
    localStorage.setItem('darkMode', 'enabled');
}

function disableDarkMode() {
    body.classList.remove('dark-mode');
    navbar.classList.replace('bg-dark', 'bg-primary');
    darkModeIcon.classList.replace('fa-moon', 'fa-sun');
    localStorage.setItem('darkMode', 'disabled');
}





