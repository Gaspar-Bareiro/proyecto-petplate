

// Función para manejar la eliminación del rol (ejemplo)
async function quitarRol(auditor) {

    // Preparar los datos para el envío a la API
    let data = {
        token: localStorage.getItem('token'),
        userName: auditor
    };

    try {
        //respuesta
        const response = await fetch(baseUrl +'/apiv1/back_office/auditors/remove', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        // Manejo de respuestas del servidor
        if (response.ok) {
            window.location.reload();
        } else if (response.status === 401) {
            cerrarSesion();
        } else {
            //si es otro error
            const errorText = await response.text(); // Obtenemos la respuesta como texto plano

            if (errorText.startsWith("API:")) {
                // Si el mensaje comienza con "API:", eliminamos esos caracteres iniciales
                errorLabelAgregarAuditor.textContent = errorText.substring(4);
            } else {
                errorLabelAgregarAuditor.textContent = 'Error inesperado. Por favor, intenta nuevamente más tarde.';
            }
            
            errorLabelAgregarAuditor.style.display = 'block'; // Muestra el label con el error
        }
        
    } catch (error) {
        console.error('Error al registrarse:', error);
        errorLabelAgregarAuditor.textContent = 'Error de conexión. Por favor, intenta nuevamente.';
        errorLabelAgregarAuditor.style.display = 'block'; // Muestra el label con el error
    }


}

//funcion para cerrar sesion en caso de 401
function cerrarSesion(){
        localStorage.removeItem('token');
        localStorage.removeItem('userId');
        localStorage.removeItem('userImg');
        document.cookie = "token=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT"; // Borra la cookie
        window.location.href = '/';    
}

// Función para vaciar el textarea y agregar todos los ingredientes
function vaciarYAgregarTodosLosIngredientes() {
    // Ordenar los ingredientes alfabéticamente
    const ingredientesOrdenados = ingredientes.sort((a, b) => a.localeCompare(b));
    
    // Mostrar los ingredientes en el textarea, separados por guiones
    textareaResultados.value = ingredientesOrdenados.join(' - ');
}


// Referencias a los elementos del DOM
const inputBusqueda = document.getElementById("back-office-input-busqueda-ingrediente");
const textareaResultados = document.getElementById("back-office-resutados-busqueda-ingredientes");

// Escuchar el evento 'input' en el campo de entrada
inputBusqueda.addEventListener("input", () => {
    // Obtener el texto ingresado y convertirlo a minúsculas para comparación
    const texto = inputBusqueda.value.toLowerCase();
    
    // Filtrar y ordenar los ingredientes que comienzan con el texto ingresado
    const ingredientesFiltrados = ingredientes
        .filter(ingrediente => ingrediente.toLowerCase().startsWith(texto))
        .sort((a, b) => a.localeCompare(b));
    
    // Mostrar los ingredientes en el textarea, separados por espacios
    textareaResultados.value = ingredientesFiltrados.join(' - ');
});


document.addEventListener('DOMContentLoaded', () => {
    const formAgregarAuditor = document.getElementById('back-office-form-agregar-auditor'); // Selecciona el formulario de rol
    const formAgregarIngrediente = document.getElementById('back-office-form-agregar-ingrediente'); // Selecciona el formulario de ingrediente

    vaciarYAgregarTodosLosIngredientes()

    //evento para agregar un auditor
    const handleSubmitAgregarAuditor = async (event) => {
        //variablers para dar rol auditor
        
        let nombreInput = document.getElementById('back-office-nombre'); // Input de nombre de usuario
        let confirmarNombreInput = document.getElementById('back-office-confirmar-nombre'); // Input confirmar nombre usuario
        let errorLabelAgregarAuditor = document.getElementById('error-label-agregar-auditor'); // Label para los mensajes de error

        event.preventDefault(); // Evita el envío del formulario
        // Limpiar el mensaje de error al iniciar la verificación
        errorLabelAgregarAuditor.textContent = ''; // vacia el label de error
        errorLabelAgregarAuditor.style.display = 'none'; // Ocultar el label de error inicialmente

        //variables para las validaciones
        isValid = true;
        let nombre = nombreInput.value.trim();
        let confirmarNombre = confirmarNombreInput.value.trim();
        //verifica que los campos no esten vacios
        if (!nombre || !confirmarNombre) {
            errorLabelAgregarAuditor.textContent = 'Todos los campos son obligatorios.';
            isValid = false
        }else

        //verifica si los nombres tienen sentido
        if (nombre.length < 4 || nombre.length > 30 || nombre.includes(" ")) {
            errorLabelAgregarAuditor.textContent = 'El usuario no existe.';
            isValid = false
        }else

        //verifica que los nombres coinsidan
        if(nombre !== confirmarNombre){
            errorLabelAgregarAuditor.textContent = 'Los usuarios no coinciden.';
            isValid = false
        }

        //muestra el mensaje de error
        if (!isValid) {
            errorLabelAgregarAuditor.style.display = 'block'; // Muestra el label con el error
            return;
        }

        // Preparar los datos para el envío a la API
        let data = {
            token: localStorage.getItem('token'),
            userName: nombre
        };

        try {
            //respuesta
            const response = await fetch(baseUrl +'/apiv1/back_office/auditors/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });

            // Manejo de respuestas del servidor
            if (response.ok) {
                
                
                window.location.reload();
            } else if (response.status === 401) {
                cerrarSesion();
            } else {
                //si es otro error
                const errorText = await response.text(); // Obtenemos la respuesta como texto plano

                if (errorText.startsWith("API:")) {
                    // Si el mensaje comienza con "API:", eliminamos esos caracteres iniciales
                    errorLabelAgregarAuditor.textContent = errorText.substring(4);
                } else {
                    errorLabelAgregarAuditor.textContent = 'Error inesperado. Por favor, intenta nuevamente más tarde.';
                }
                
                errorLabelAgregarAuditor.style.display = 'block'; // Muestra el label con el error
            }
            
        } catch (error) {
            console.error('Error al registrarse:', error);
            errorLabelAgregarAuditor.textContent = 'Error de conexión. Por favor, intenta nuevamente.';
            errorLabelAgregarAuditor.style.display = 'block'; // Muestra el label con el error
        }

    };

    //evento para agregar un ingrediente
    const handleSubmitAgregarIngrediente = async (event) => {
    
        
        let nombreInput = document.getElementById('back-office-nombre-ingrediente'); // Input de nombre del ingrediente
        let confirmarNombreInput = document.getElementById('back-office-confirmar-nombre-ingrediente'); // Input confirmar nombre ingrediente
        let errorLabelAgregarIngrediente = document.getElementById('error-label-agregar-ingrediente'); // Label para los mensajes de error

        errorLabelAgregarIngrediente.style.color = '#f55'

        event.preventDefault(); // Evita el envío del formulario
        // Limpiar el mensaje de error al iniciar la verificación
        errorLabelAgregarIngrediente.textContent = ''; // vacia el label de error
        errorLabelAgregarIngrediente.style.display = 'none'; // Ocultar el label de error inicialmente

        //variables para las validaciones
        isValid = true;
        let nombre = nombreInput.value.trim();
        let confirmarNombre = confirmarNombreInput.value.trim();

        //verifica que los campos no esten vacios
        if (!nombre || !confirmarNombre) {
            errorLabelAgregarIngrediente.textContent = 'Todos los campos son obligatorios.';
            isValid = false
        }else

        //verifica si los nombres tienen sentido
        if (nombre.length < 3 || nombre.length > 30) {
            errorLabelAgregarIngrediente.textContent = 'El nombre del ingrediente debe tener entre 3 y 30 caracteres.';
            isValid = false
        } else

        //verifica que los nombres coinsidan
        if(nombre !== confirmarNombre){
            errorLabelAgregarIngrediente.textContent = 'Los Ingredientes no coinciden.';
            isValid = false
        } 

        //muestra el mensaje de error
        if (!isValid) {
            errorLabelAgregarIngrediente.style.display = 'block'; // Muestra el label con el error
            return;
        }

        // Preparar los datos para el envío a la API
        let data = {
            token: localStorage.getItem('token'),
            ingredientName: nombre
        };

        try {
            //respuesta
            const response = await fetch(baseUrl +'/apiv1/back_office/ingredients/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });

            // Manejo de respuestas del servidor
            if (response.ok) {
                
                //muestra mensaje de exito
                errorLabelAgregarIngrediente.style.color = '#3e3'
                errorLabelAgregarIngrediente.textContent = 'Se agrego correctamente el ingrediente';
                errorLabelAgregarIngrediente.style.display = 'block'
                //vacia el areglo de ingredientes para refrescarlo
                localStorage.removeItem('ingredientes');
                await llenarArrayIngredientes()
                
                vaciarYAgregarTodosLosIngredientes()
            } else if (response.status === 401) {
                cerrarSesion();
            } else {
                //si es otro error
                const errorText = await response.text(); // Obtenemos la respuesta como texto plano

                if (errorText.startsWith("API:")) {
                    // Si el mensaje comienza con "API:", eliminamos esos caracteres iniciales
                    errorLabelAgregarIngrediente.textContent = errorText.substring(4);
                } else {
                    errorLabelAgregarIngrediente.textContent = 'Error inesperado. Por favor, intenta nuevamente más tarde.';
                }
                
                errorLabelAgregarIngrediente.style.display = 'block'; // Muestra el label con el error
            }
            
        } catch (error) {
            console.error('Error al registrarse:', error);
            errorLabelAgregarIngrediente.textContent = 'Error de conexión. Por favor, intenta nuevamente.';
            errorLabelAgregarIngrediente.style.display = 'block'; // Muestra el label con el error
        }

    };
    //----------------------cambiar anuncios---------------------------
    const leftChangeButton = document.getElementById('back-office-ads-button-left-change') //selecciona el boton para cambiar la imagen izquierdo
    const leftChangeInput = document.getElementById('addFileInputLeft')// input que contiene la imagen Izquieda
    const rightChangeButton = document.getElementById('back-office-ads-button-rigth-change') //selecciona el boton para cambiar la imagen  Derecha
    const rightChangeInput = document.getElementById('addFileInputRight')// input que contiene la imagen Derecha
    const bothChangeButton = document.getElementById('back-office-ads-button-both-change') //selecciona el boton para cambiar Ambas imagenes
    const bothChangeInput = document.getElementById('addFileInputBoth')// input que contiene Ambas imagenes


    //al hacer click en Izquierdo abre para seleccionar imagen
    leftChangeButton.addEventListener('click', function() {
        leftChangeInput.click(); // Selecciona el input files
    });
    rightChangeButton.addEventListener('click', function() {
        rightChangeInput.click(); // Selecciona el input files
    });
    bothChangeButton.addEventListener('click', function() {
        bothChangeInput.click(); // Selecciona el input files
    });

    //funcion para cambiar los anuncios
    async function cambiarImagenAnuncios(ubicacion) {

        let isValid = true; // Inicializa isValid
        const errorLabelModificarAnuncio = document.getElementById('error-label-agregar-anuncio');

        errorLabelModificarAnuncio.style.display = 'none'; //por defecto oculta el label de error

        if (ubicacion != "left" && ubicacion != "right" && ubicacion != "both") {
            errorLabelModificarAnuncio.textContent = "Localizacion del anuncio no valida"
            errorLabelModificarAnuncio.style.display = 'block'; // Muestra el label con el error
            return;
        }
        let file; // Declara fuera del bloque if
        
        //obtiene el archivo del input correspondiente
        if (ubicacion == "left") {
            file = leftChangeInput.files[0]; // Obtiene el primer archivo seleccionado
        }else if(ubicacion == "right"){
            file = rightChangeInput.files[0]; // Obtiene el primer archivo seleccionado
        }else{
            file = bothChangeInput.files[0]; // Obtiene el primer archivo seleccionado
        }

        if (!file) {
            errorLabelModificarAnuncio.textContent = "No se ha seleccionado ningún archivo";
            errorLabelModificarAnuncio.style.display = 'block'; // Muestra el label con el error
            return;
        }

        const maxSize = 5 * 1024 * 1024; // 5 MB

        //verifica el tamanio maximo de la imagen
        const validExtensions = ['jpg', 'jpeg', 'png'];
        const fileExtension = file.name.split('.').pop().toLowerCase(); // Obtener la extensión del archivo

        // Verifica si se seleccionó un archivo
        if (!file) {
            errorLabelModificarAnuncio.textContent = 'No se ha seleccionado ningún archivo.';
            isValid = false;
        } else
        //verifica el tamanio de la imagen
        if (file.size > maxSize) {
            errorLabelModificarAnuncio.textContent = 'La imagen no debe superar los 5 MB.';
            isValid = false;
        }else
        // verifica si la imagen tiene una extencionm correcta 
        if (!validExtensions.includes(fileExtension)) {
            errorLabelModificarAnuncio.textContent = 'La imagen debe tener un formato válido (.jpg, .jpeg o .png).';
            isValid = false
        }

        

        // Si alguna verificación falla, muestra el mensaje de error y evita el envío del formulario
        if (!isValid) {
            errorLabelModificarAnuncio.style.display = 'block'; // Muestra el label con el error
            return;
        }

        //arma el objeto para el request 
        const requestData = new FormData();
        requestData.append('token', localStorage.getItem('token')); // Token de sesión
        requestData.append('img', file); // Archivo de imagen
        requestData.append('location', ubicacion)

        try {
            const response = await fetch(baseUrl +'/apiv1/changeAds', {
                method: 'POST',
                body: requestData
            });


            if (response.status === 200) {

                setTimeout(() => {
                    location.reload(); // Recarga la página después de un pequeño retraso
                }, 1000); // 1000 ms 1 segundo

            }else{
                errorLabelModificarAnuncio.textContent = 'Error inesperado. Intente nuevamente más tarde.';
                errorLabelModificarAnuncio.style.display = 'block'; // Muestra el mensaje de error
            }

        } catch (error) {
            console.error('Error en la solicitud:', error);
            errorLabelModificarAnuncio.textContent = 'Error al comunicarse con el servidor.';
            errorLabelModificarAnuncio.style.display = 'block'; // Muestra el mensaje de error
        }
    }



    //se ejecuta la funcion cada vez que se selecciona un archivo
    // se ejecuta cada vez que se selecciona un archivo
    leftChangeInput.addEventListener('change', function() {
        cambiarImagenAnuncios("left")
    });
    // se ejecuta cada vez que se selecciona un archivo
    rightChangeInput.addEventListener('change', function() {
        cambiarImagenAnuncios("right")
    });
    // se ejecuta cada vez que se selecciona un archivo
    bothChangeInput.addEventListener('change', function() {
        cambiarImagenAnuncios("both")
    });

    const leftRemoveButton = document.getElementById('back-office-ads-button-left-remove') //selecciona el boton para eliminar la imagen izquierdo
    const bothRemoveButton = document.getElementById('back-office-ads-button-both-remove') //selecciona el boton para eliminar ambas imagenes
    const rightRemoveButton = document.getElementById('back-office-ads-button-rigth-remove') //selecciona el boton para eliminar la imagen derecho

    async function eliminarImagenAnuncios(ubicacion) {
        let isValid = true; // Inicializa isValid
        const errorLabelEliminarAnuncio = document.getElementById('error-label-eliminar-anuncio');

        errorLabelEliminarAnuncio.style.display = 'none'; //por defecto oculta el label de error

        if (ubicacion != "left" && ubicacion != "right" && ubicacion != "both") {
            errorLabelEliminarAnuncio.textContent = "Localizacion del anuncio no valida"
            errorLabelEliminarAnuncio.style.display = 'block'; // Muestra el label con el error
            return;
        }
        

        //arma el objeto para el request 
        let data = {
            token: localStorage.getItem('token'),
            location: ubicacion
        };

        try {
            //respuesta
            const response = await fetch(baseUrl +'/apiv1/removeAds', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });

            // Manejo de respuestas del servidor
            if (response.ok) {

                setTimeout(() => {
                    location.reload(); // Recarga la página después de un pequeño retraso
                }, 1000); // 1000 ms 1 segundo

            }else{
                errorLabelEliminarAnuncio.textContent = 'Error inesperado. Intente nuevamente más tarde.';
                errorLabelEliminarAnuncio.style.display = 'block'; // Muestra el mensaje de error
            }

        } catch (error) {
            console.error('Error en la solicitud:', error);
            errorLabelEliminarAnuncio.textContent = 'Error al comunicarse con el servidor.';
            errorLabelEliminarAnuncio.style.display = 'block'; // Muestra el mensaje de error
        }

    }

    //al hacer click en Izquierdo elimina los anuncios izquiedos
    leftRemoveButton.addEventListener('click', () => eliminarImagenAnuncios("left"));

    //al hacer click en Izquierdo elimina los anuncios
    bothRemoveButton.addEventListener('click', () => eliminarImagenAnuncios("both"));

    //al hacer click en Izquierdo elimina los anuncios izquiedos
    rightRemoveButton.addEventListener('click', () => eliminarImagenAnuncios("right"));


    //-----------------------termina cambiar anuncios--------------------


    // Asignar el evento de envío del formulario de agregar ingrediente
    formAgregarIngrediente.addEventListener('submit', handleSubmitAgregarIngrediente);

    // Asignar el evento de envío del formulario de dar rol auditor
    formAgregarAuditor.addEventListener('submit', handleSubmitAgregarAuditor);

    //funcion para obtener auditores
    async function obtenerAuditores() {
        // Obtener el token del localStorage
        const token = localStorage.getItem('token'); 

        // Si no hay token, abortar la solicitud
        if (!token) {
            cerrarSesion()
        }
        // Realizar la solicitud POST 
        try {
            const response = await fetch(baseUrl +'/apiv1/back_office/auditors', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ "token": token }) // Enviar el token en el cuerpo
            });

            // Manejar respuestas de la API
            if (response.ok) {
                const arrayAuditores = await response.json(); 

                const tablaAuditores = document.querySelector('.tabla-auditores');

                // Verificar si la tabla existe
                if (tablaAuditores) {
                    // Limpiar la tabla para evitar duplicados
                    tablaAuditores.innerHTML = `
                        <tr>
                            <td>Auditor</td>
                            <td>Quitar Rol</td>
                        </tr>
                    `;

                    // Insertar una fila para cada auditor
                    arrayAuditores.forEach(auditor => {
                        const fila = document.createElement('tr');
                        fila.innerHTML = `
                            <th>${auditor}</th>
                            <th><button onclick="quitarRol('${auditor}')">Quitar rol</button></th>
                        `;
                        tablaAuditores.appendChild(fila);
                    });
                }

            } else {
                console.error('Ocurrió un error inesperado. Código de respuesta:', response.status);
            }
        } catch (error) {
            console.error('Error en la solicitud:', error);
            // Manejo de errores de comunicación con el servidor
        }
    }
    //obtiene los auditores
    obtenerAuditores()

});