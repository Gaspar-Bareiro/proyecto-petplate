document.addEventListener('DOMContentLoaded', () => {
    
    //funcion que maneja el envio de crear receta
    const handleSubmitCreateRecipe = async (event) => {
        event.preventDefault(); // Evita que el formulario se envíe de manera tradicional
        
        // Variables para crear receta
        const tituloRecetaInputCrearReceta = document.getElementById('titulo-receta-crear-receta'); 
        const descripcionInputCrearReceta = document.getElementById('descripcion-receta-crear-receta');
        const categoriaInputCrearReceta = document.getElementById('RC-animal-select-selected'); 
        const subcategoriaInputCrearReceta = document.getElementById('RC-tipo-select-selected'); 
        const imgInputCrearReceta = document.getElementById('imagenInput'); 
        const ingredientesInputCrearReceta = resultadosCR.querySelectorAll('.CR-resultado-ingrediente'); 
        const errorLabelCrearReceta = document.getElementById('error-label-crear-receta'); 
    
        // Limpiar el mensaje de error al iniciar la verificación
        errorLabelCrearReceta.textContent = ''; 
        errorLabelCrearReceta.style.display = 'none'; 
    
        // Variables para las validaciones
        let isValid = true;
        const titulo = tituloRecetaInputCrearReceta.value.trim();
        const descripcion = descripcionInputCrearReceta.value.trim();
        const categoria = categoriaInputCrearReceta.textContent.trim();
        const subcategoria = subcategoriaInputCrearReceta.textContent.trim();
        const file = imgInputCrearReceta.files[0]; // Obtener el archivo seleccionado
        const maxSize = 5 * 1024 * 1024; // 5 MB
    
        // Verificación de que titulo no este en blanco
        if (!titulo) {
            errorLabelCrearReceta.textContent = 'El título de la receta no debe estar vacío.';
            isValid = false;
        }else

        // Verificación de que descripcion no este en blanco
        if (!descripcion) {
            errorLabelCrearReceta.textContent = 'La descripción no debe estar vacía.';
            isValid = false;
        }else
    
        // Verificación de longitud del título y descripción
        if (titulo.length < 5 || titulo.length > 200) {
            errorLabelCrearReceta.textContent = 'El título debe tener entre 5 y 200 caracteres.';
            isValid = false;
        }else
    
        if (descripcion.length < 100) {
            errorLabelCrearReceta.textContent = 'La descripción debe tener al menos 100 caracteres.';
            isValid = false;
        }else

        if (categoria === 'Animal') {
            errorLabelCrearReceta.textContent = 'Se debe seleccionar un Animal válido.';
            isValid = false;
        } else
    
        // Verificación de ingredientes
        if (ingredientesInputCrearReceta.length === 0) {
            errorLabelCrearReceta.textContent = 'Debes agregar al menos un ingrediente.';
            isValid = false;
        } else
        //si se ingreso una imagen verifica la imagen
        if (file) {
            //verifica el tamanio maximo de la imagen
            const validExtensions = ['jpg', 'jpeg', 'png'];
            const fileExtension = file.name.split('.').pop().toLowerCase(); // Obtener la extensión del archivo
            if (file.size > maxSize) {
                errorLabelCrearReceta.textContent = 'La imagen no debe superar los 5 MB.';
                isValid = false;
            }else
            // verifica si la imagen tiene una extencionm correcta 
            if (!validExtensions.includes(fileExtension)) {
                errorLabelCrearReceta.textContent = 'La imagen debe tener una extensión válida (.jpg, .jpeg o .png).';
                isValid = false
            }
            
        }
    
        // Si alguna verificación falla, muestra el mensaje de error y evita el envío del formulario
        if (!isValid) {
            errorLabelCrearReceta.style.display = 'block'; // Muestra el label con el error
            return;
        }
    
        // Construcción del objeto de la receta
        const ingredientesArray = Array.from(ingredientesInputCrearReceta).map((ingredienteDiv) => {
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
        if (imgInputCrearReceta.files.length > 0) {
            recetaData.append('img', imgInputCrearReceta.files[0]); // Archivo de imagen
        }else{
            const emptyBlob = new Blob([], { type: 'image/jpeg' }); // Crea un archivo vacío con un tipo de archivo válido
            recetaData.append('img', emptyBlob, '');  // Envía el campo vacío
        }

        // Convertir el array de ingredientes a un JSON Blob
        const ingredientesJSON = JSON.stringify(ingredientesArray);
        const ingredientesBlob = new Blob([ingredientesJSON], { type: "application/json" });
        recetaData.append('ingredientes', ingredientesBlob);

        // Enviar la receta a la API
        try {
            const response = await fetch('/apiv1/recipe/create', {
                method: 'POST',
                body: recetaData
            });

            // Manejar respuestas de la API
            if (response.ok) {
                // Redirecciona al perfil del usuario en caso de éxito
                window.location.href = '/profile/' + localStorage.getItem('userId');
            } else {
                // Manejo de errores según el código de respuesta
                if (response.status === 401) {
                    errorLabelCrearReceta.textContent = 'Sesión inválida o expirada.';
                    // Vaciar los datos de la sesión
                    localStorage.removeItem('token');
                    localStorage.removeItem('userId');
                    localStorage.removeItem('userImg');
                    // Actualizar la interfaz de usuario (header)
                    let botonIniciarSesionHeader = document.getElementById('boton-iniciar-sesion-header');
                    let botonIrPerfilHeader = document.getElementById('boton-ir-a-perfil-header');
                    botonIniciarSesionHeader.style.display = 'block';
                    botonIrPerfilHeader.style.display = 'none';
                }else if (response.status === 409) {
                    const errorText = await response.text();
                    if (errorText.startsWith("API:")) {
                        errorLabelCrearReceta.textContent = errorText.substring(4);
                    } else {
                        errorLabelCrearReceta.textContent = 'Error 409.';
                    }
                }else{
                    errorLabelCrearReceta.textContent = 'Ocurrió un error inesperado. Intente nuevamente más tarde.';
                }
                errorLabelCrearReceta.style.display = 'block'; // Muestra el mensaje de error
            }
        } catch (error) {
            console.error('Error en la solicitud:', error);
            errorLabelCrearReceta.textContent = 'Error al comunicarse con el servidor.';
            errorLabelCrearReceta.style.display = 'block'; // Muestra el mensaje de error
        }

        
    }

    const botonCrearReceta = document.getElementById('boton-crear-receta'); // Selecciona el botón por su id

    // Asocia el evento de clic al botón "Crear Receta"
    botonCrearReceta.addEventListener('click', handleSubmitCreateRecipe);

});