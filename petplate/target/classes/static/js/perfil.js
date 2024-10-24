document.addEventListener('DOMContentLoaded', () => {

    const cambiarImagenItem = document.getElementById('boton-cambiar-img-perfil') //selecciona el boton para cambiar la imagen
    const fileInput = document.getElementById('fileInput'); // Selecciona el input files

    function cambiarEstiloPerfilSiEsMio(){
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

                //selecciona el hover de la imagen del perfil y le da un display block
                cambiarImagenItem.style.display = 'block';
            }
        }
    }

    // Llamar a la función después de que se cargue el DOM
    cambiarEstiloPerfilSiEsMio()
    
    
    //al hacer click en el cambiar imagen abre para seleccionar imagen
    cambiarImagenItem.addEventListener('click', function() {
        fileInput.click(); // Abre el diálogo de selección de archivo
    });

    
    async function cambiarImagenPerfil(){
        let isValid = true; // Inicializa isValid
        const file = fileInput.files[0]; // Obtiene el primer archivo seleccionado
        const errorLabelModificarImagen = document.getElementById('error-label-cambiar-imagen');
        
        errorLabelModificarImagen.style.display = 'none'; //por defecto oculta el label de error 

        const maxSize = 5 * 1024 * 1024; // 5 MB

        //verifica el tamanio maximo de la imagen
        const validExtensions = ['jpg', 'jpeg', 'png'];
        const fileExtension = file.name.split('.').pop().toLowerCase(); // Obtener la extensión del archivo

        // Verifica si se seleccionó un archivo
        if (!file) {
            errorLabelModificarImagen.textContent = 'No se ha seleccionado ningún archivo.';
            isValid = false;
        } else
        //verifica el tamanio de la imagen
        if (file.size > maxSize) {
            errorLabelModificarImagen.textContent = 'La imagen no debe superar los 5 MB.';
            isValid = false;
        }else
        // verifica si la imagen tiene una extencionm correcta 
        if (!validExtensions.includes(fileExtension)) {
            errorLabelModificarImagen.textContent = 'La imagen debe tener un formato válido (.jpg, .jpeg o .png).';
            isValid = false
        }

        // Si alguna verificación falla, muestra el mensaje de error y evita el envío del formulario
        if (!isValid) {
            errorLabelModificarImagen.style.display = 'block'; // Muestra el label con el error
            return;
        }
        
        //arma el objeto para el request 
        const requestData = new FormData();
        requestData.append('token', localStorage.getItem('token')); // Token de sesión
        requestData.append('img', file); // Archivo de imagen

        try {
            const response = await fetch('/apiv1/userImgChange', {
                method: 'POST',
                body: requestData
            });


            if (response.status === 200) {
            
                
                
                // Espera la resolución de la promesa y obtén el nuevo enlace
                const textResponse = (await response.text()).trim(); // Obtén la respuesta como texto
                // Cambia el src de la imagen
                localStorage.setItem("userImg", textResponse)

                setTimeout(() => {
                    location.reload(); // Recarga la página después de un pequeño retraso
                }, 1000); // 1000 ms 1 segundo

            }else{
                errorLabelModificarImagen.textContent = 'Error inesperado. Intente nuevamente más tarde.';
                errorLabelModificarImagen.style.display = 'block'; // Muestra el mensaje de error
            }

        } catch (error) {
            console.error('Error en la solicitud:', error);
            errorLabelModificarImagen.textContent = 'Error al comunicarse con el servidor.';
            errorLabelModificarImagen.style.display = 'block'; // Muestra el mensaje de error
        }
    }


    // se ejecuta cada vez que se selecciona un archivo
    fileInput.addEventListener('change', function(event) {
        cambiarImagenPerfil()
    });


});