document.addEventListener('DOMContentLoaded', () => {
    const formAgregarAuditor = document.getElementById('back-office-form-agregar-auditor'); // Selecciona el formulario de register

    function cerrarSesion(){
        localStorage.removeItem('token');
        localStorage.removeItem('userId');
        localStorage.removeItem('userImg');
        document.cookie = "token=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT"; // Borra la cookie
        window.location.href = '/';
        
    }

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
        }

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
            const response = await fetch('/apiv1/back_office/auditors/create', {
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
    // Asignar el evento de envío del formulario de registrarse
    formAgregarAuditor.addEventListener('submit', handleSubmitAgregarAuditor);

});