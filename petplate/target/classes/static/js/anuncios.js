// Seleccionar los elementos del DOM
const anuncioIzquierdo = document.querySelector('.anuncioIzquierdo');
const anuncioDerecho = document.querySelector('.anuncioDerecho');


// Función para manejar el error de carga de la imagen
function manejarErrorDeImagen(elemento) {
    elemento.style.backgroundImage = 'none'; // Quitar la imagen de fondo
    elemento.textContent = '160x600'; // Añadir el texto de resolución
    elemento.style.display = 'flex'; // Usar flexbox para centrar el texto
    elemento.style.alignItems = 'center';
    elemento.style.justifyContent = 'center';
    elemento.style.fontSize = '24px'; // Ajustar el tamaño de la fuente
    elemento.style.color = 'black'; // Ajustar el color del texto
    elemento.style.backgroundColor = 'lightgray'; // Fondo para mejorar la visibilidad
}

// Comprobar si la imagen se carga correctamente
const comprobarImagen = (elemento, baseUrl) => {
    const extensiones = ['.png','.jpg', '.jpeg'];  // Lista de extensiones
    let intentos = 0;

    // Función interna para intentar cargar cada extensión
    const intentarCargarImagen = () => {
        if (intentos < extensiones.length) {
            const url = `${baseUrl}${extensiones[intentos]}`;
            const img = new Image();
            img.src = url;

            img.onload = () => {
                elemento.style.backgroundImage = `url('${url}')`; // Asignar la imagen si carga bien
            };

            img.onerror = () => {
                intentos++; // Intentar con la siguiente extensión
                intentarCargarImagen(); // Volver a intentar
            };
        } else {
            manejarErrorDeImagen(elemento); // Si no se cargó ninguna imagen, manejar el error
        }
    };

    intentarCargarImagen(); // Iniciar la carga de la imagen
};

// Comprobar ambas imágenes con sus respectivas rutas base
comprobarImagen(anuncioIzquierdo, '/ads-pictures/anuncioIzquierdo');
comprobarImagen(anuncioDerecho, '/ads-pictures/anuncioDerecho');
