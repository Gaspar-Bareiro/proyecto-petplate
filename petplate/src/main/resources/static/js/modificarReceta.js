document.addEventListener('DOMContentLoaded', () => {
    
    //funcion que maneja el envio de crear receta
    const handleSubmitCreateRecipe = async (event) => {
        event.preventDefault(); // Evita que el formulario se envíe de manera tradicional
        
        // Variables para crear receta
        const tituloRecetaInputModificarReceta = document.getElementById('titulo-receta-modificar-receta'); 
        const descripcionInputModificarReceta = document.getElementById('descripcion-receta-modificar-receta');
        const categoriaInputModificarReceta = document.getElementById('RC-animal-select-selected'); 
        const subcategoriaInputModificarReceta = document.getElementById('RC-tipo-select-selected'); 
        const imgInputModificarReceta = document.getElementById('imagenInput'); 
        const ingredientesInputModificarReceta = resultadosCR.querySelectorAll('.CR-resultado-ingrediente'); 
        const errorLabelModificarReceta = document.getElementById('error-label-modificar-receta'); 
    
        // Limpiar el mensaje de error al iniciar la verificación
        errorLabelModificarReceta.textContent = ''; 
        errorLabelModificarReceta.style.display = 'none'; 
    
        // Variables para las validaciones
        let isValid = true;
        const titulo = tituloRecetaInputModificarReceta.value.trim();
        const descripcion = descripcionInputModificarReceta.value.trim();
        const categoria = categoriaInputModificarReceta.textContent.trim();
        const subcategoria = subcategoriaInputModificarReceta.textContent.trim();
        const file = imgInputModificarReceta.files[0]; // Obtener el archivo seleccionado
        const maxSize = 5 * 1024 * 1024; // 5 MB
    
        // Verificación de que titulo no este en blanco
        if (!titulo) {
            errorLabelModificarReceta.textContent = 'El título de la receta no debe estar vacío.';
            isValid = false;
        }else

        // Verificación de que descripcion no este en blanco
        if (!descripcion) {
            errorLabelModificarReceta.textContent = 'La descripción no debe estar vacía.';
            isValid = false;
        }else
    
        // Verificación de longitud del título y descripción
        if (titulo.length < 5 || titulo.length > 200) {
            errorLabelModificarReceta.textContent = 'El título debe tener entre 5 y 200 caracteres.';
            isValid = false;
        }else
    
        if (descripcion.length < 100) {
            errorLabelModificarReceta.textContent = 'La descripción debe tener al menos 100 caracteres.';
            isValid = false;
        }else

        if (categoria === 'Animal') {
            errorLabelModificarReceta.textContent = 'Se debe seleccionar un Animal válido.';
            isValid = false;
        } else
    
        // Verificación de ingredientes
        if (ingredientesInputModificarReceta.length === 0) {
            errorLabelModificarReceta.textContent = 'Debes agregar al menos un ingrediente.';
            isValid = false;
        } else
        //si se ingreso una imagen verifica la imagen
        if (file) {
            //verifica el tamanio maximo de la imagen
            const validExtensions = ['jpg', 'jpeg', 'png'];
            const fileExtension = file.name.split('.').pop().toLowerCase(); // Obtener la extensión del archivo
            if (file.size > maxSize) {
                errorLabelModificarReceta.textContent = 'La imagen no debe superar los 5 MB.';
                isValid = false;
            }else
            // verifica si la imagen tiene una extencionm correcta 
            if (!validExtensions.includes(fileExtension)) {
                errorLabelModificarReceta.textContent = 'La imagen debe tener una extensión válida (.jpg, .jpeg o .png).';
                isValid = false
            }
            
        }
    
        // Si alguna verificación falla, muestra el mensaje de error y evita el envío del formulario
        if (!isValid) {
            errorLabelModificarReceta.style.display = 'block'; // Muestra el label con el error
            return;
        }
    
        // Construcción del objeto de la receta
        const ingredientesArray = Array.from(ingredientesInputModificarReceta).map((ingredienteDiv) => {
            const cantidadDiv = ingredienteDiv.nextElementSibling; // Asumiendo que la cantidad está justo después del div del ingrediente
            const unidadDiv = cantidadDiv.nextElementSibling; // Y la unidad justo después de la cantidad
    
            return {
                name: ingredienteDiv.textContent,
                quantity: parseFloat(cantidadDiv.textContent),
                unitOfMeasurement: unidadDiv.textContent
            };
        });
    
        const recetaData = new FormData();
        recetaData.append('token', localStorage.getItem('token')); // Token de sesión
        recetaData.append('title', titulo);
        recetaData.append('description', descripcion);
        recetaData.append('categoryName', categoria);
        if (subcategoria !== 'Tipo') {
            recetaData.append('subcategoryName', subcategoria);
        }
        
        // Verificar si se ha seleccionado una imagen antes de agregarla
        if (imgInputModificarReceta.files.length > 0) {
            recetaData.append('img', imgInputModificarReceta.files[0]); // Archivo de imagen
        }else{
            const emptyBlob = new Blob([], { type: 'image/jpeg' }); // Crea un archivo vacío con un tipo de archivo válido
            recetaData.append('img', emptyBlob, '');  // Envía el campo vacío
        }

        // Convertir el array de ingredientes a un JSON Blob
        const ingredientesJSON = JSON.stringify(ingredientesArray);
        const ingredientesBlob = new Blob([ingredientesJSON], { type: "application/json" });
        recetaData.append('ingredientes', ingredientesBlob);

        // Obtener la ruta actual
        const path = window.location.pathname;

        // Dividir la ruta en segmentos
        const segments = path.split('/');

        // Extraer el último segmento, que es la ID
        const idReceta = segments[segments.length - 1];
        // Enviar la receta a la API
        try {
            const response = await fetch('/apiv1/recipe/modify/' + idReceta, {
                method: 'PUT',
                body: recetaData
            });

            // Manejar respuestas de la API
            if (response.ok) {
                // Redirecciona al perfil del usuario en caso de éxito
                window.location.href = '/profile/' + localStorage.getItem('userId');
            } else {
                // Manejo de errores según el código de respuesta
                if (response.status === 401) {
                    const errorText = await response.text();
                    if (errorText.startsWith("API:")) {//si empieza con api significa que no le pertenece la receta
                        errorLabelModificarReceta.textContent = errorText.substring(4);
                    } else {// sino la sesion no es valida
                        errorLabelModificarReceta.textContent = 'Sesión inválida o expirada.';
                        // Vaciar los datos de la sesión
                        localStorage.removeItem('token');
                        localStorage.removeItem('userId');
                        localStorage.removeItem('userImg');
                        // Actualizar la interfaz de usuario (header)
                        let botonIniciarSesionHeader = document.getElementById('boton-iniciar-sesion-header');
                        let botonIrPerfilHeader = document.getElementById('boton-ir-a-perfil-header');
                        botonIniciarSesionHeader.style.display = 'block';
                        botonIrPerfilHeader.style.display = 'none';
                    }
                    
                }else if (response.status === 409) {
                    const errorText = await response.text();
                    if (errorText.startsWith("API:")) {
                        errorLabelModificarReceta.textContent = errorText.substring(4);
                    } else {
                        errorLabelModificarReceta.textContent = 'Error 409.';
                    }
                }else{
                    errorLabelModificarReceta.textContent = 'Ocurrió un error inesperado. Intente nuevamente más tarde.';
                }
                errorLabelModificarReceta.style.display = 'block'; // Muestra el mensaje de error
            }
        } catch (error) {
            console.error('Error en la solicitud:', error);
            errorLabelModificarReceta.textContent = 'Error al comunicarse con el servidor.';
            errorLabelModificarReceta.style.display = 'block'; // Muestra el mensaje de error
        }

        
    }

    const botonCrearReceta = document.getElementById('boton-modificar-receta'); // Selecciona el botón por su id

    // Asocia el evento de clic al botón "Crear Receta"
    botonCrearReceta.addEventListener('click', handleSubmitCreateRecipe);

});