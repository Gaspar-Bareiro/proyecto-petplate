document.addEventListener('DOMContentLoaded', () => {
    function cargarEstiuloBotonModificarReceta(){
        // Obtener la ruta actual
        const path = window.location.pathname;

        // Dividir la ruta en segmentos
        const segments = path.split('/');

        // Extraer el último segmento, que es la ID
        const id = segments[segments.length - 1];

        let token = localStorage.getItem('token'); // Obtener el token desde localStorage

        if (token !== null && token.trim() !== ''){

            if(id === localStorage.getItem('userId')){
                // Seleccionar todos los elementos con la clase 'contenedor-imagen-perfil-contendor-receta'
                const elementos = document.querySelectorAll('.contenedor-imagen-perfil-contendor-receta');

                // Recorrer los elementos y cambiarles el display a 'block'
                elementos.forEach((elemento) => {
                    elemento.style.display = 'block';
                });
            }
        }
    }

    // Llamar a la función después de que se cargue el DOM
    cargarEstiuloBotonModificarReceta()
});