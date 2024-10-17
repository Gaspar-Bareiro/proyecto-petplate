
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('crear-cuenta-form'); // Selecciona el formulario
    const nombreUsuarioInput = document.getElementById('nombre-usuario-crear-cuenta'); // Input de nombre de usuario
    const correoInput = document.getElementById('correo-electronico-crear-cuenta'); // Input de correo electrónico
    const contrasenaInput = document.getElementById('contrasena-crear-cuenta'); // Input de contraseña
    const confirmarContrasenaInput = document.getElementById('confirmar-contrasena-crear-cuenta'); // Input de confirmar contraseña
    const errorLabel = document.getElementById('error-label-crear-cuenta'); // Label para los mensajes de error
    const nombreUsuarioInputLogin = document.getElementById('nombre-usuario-iniciar-sesion'); // Input de nombre de usuario
    

    const handleSubmit = async (event) => {

        event.preventDefault(); // Evita el envío del formulario

        // Limpiar el mensaje de error al iniciar la verificación
        errorLabel.textContent = '';
        errorLabel.style.display = 'none'; // Ocultar el label de error inicialmente

        // Variables de estado para las validaciones
        let isValid = true;
        const correo = correoInput.value.trim();
        const nombreUsuario = nombreUsuarioInput.value.trim();
        const contrasena = contrasenaInput.value.trim();
        const confirmarContrasena = confirmarContrasenaInput.value.trim();
        const correoRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        // Verificación si hay algún campo vacío
        if (!nombreUsuario || !correo || !contrasena || !confirmarContrasena) {
            errorLabel.textContent = 'Todos los campos son obligatorios.';
            isValid = false;
        } else

        // Verificación del nombre de usuario
        if (nombreUsuario.length < 4 || nombreUsuario.length > 30) {
            errorLabel.textContent = 'El nombre de usuario debe contener entre 4 y 30 caracteres.';
            isValid = false;
        } else

        //verifica que el nombre de usuario no tenga espacios
        if (nombreUsuario.includes(" ")) {
            errorLabel.textContent = 'El nombre de usuario no debe contener espacios.';
            isValid = false;
        } else

        //verifica si el correo tiene mas de 254 caractes
        if (correo.length > 254) {
            errorLabel.textContent = 'El correo electrónico es demaciado largo.';
            isValid = false;
        } else

        //verifica si el correo contiene espacios
        if (correo.includes(" ")) {
            errorLabel.textContent = 'El correo electrónico no debe contener espacios.';
            isValid = false;
        } else

        // Verificación del correo electrónico (usando una expresión regular básica)
        if (!correoRegex.test(correo)) {
            errorLabel.textContent = 'El correo electrónico ingresado tiene un formato inválido.';
            isValid = false;
        } else

        
        // Verificación la contraseña tiene menos de 8 caracteres o mas de 20 caracteres
        if (contrasena.length < 8 || contrasena.length > 20) {
            errorLabel.textContent = 'La contraseña debe contener entre 8 y 20 caracteres.';
            isValid = false;
        } else


        // Verificación la contraseña no contenga espacios
        if (contrasena.includes(" ")) {
            errorLabel.textContent = 'La contraseña no debe contener espacios.';
            isValid = false;
        } else

        // Verificación de que las contraseñas coincida
        if (contrasena !== confirmarContrasena) {
            errorLabel.textContent = 'La confirmacion de la contraseña ingresada es distinta de la primera.';
            isValid = false;
        }

        // Si alguna verificación falla, muestra el mensaje de error y evita el envío del formulario
        if (!isValid) {
            errorLabel.style.display = 'block'; // Muestra el label con el error
            return;
        }

        // Preparar los datos para el envío a la API
        const data = {
            userName: nombreUsuario,
            userEmail: correo,
            userPassword: contrasena
        };

        try {
            const response = await fetch('/apiv1/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });

            // Manejo de respuestas del servidor
            if (response.ok) {
                // Usuario registrado correctamente
                form.reset(); // Limpia el formulario

                //cierra la ventana modal crear cuenta
                modalRegister.removeAttribute('open'); // Elimina el atributo 'open' para cerrar el diálogo register

                //abre la ventana modal iniciar sesion
                modalLogin.setAttribute('open', ''); // Muestra la ventana modal login

                //rellena el nombre de usuario con el nombre de usuario recien registrado
                nombreUsuarioInputLogin.value = nombreUsuario;

            } else if (response.status === 409) {
                // Conflicto: nombre de usuario o email ya está en uso
                const errorText = await response.text(); // Obtenemos la respuesta como texto plano

                if (errorText.startsWith("API:")) {
                    // Si el mensaje comienza con "API:", eliminamos esos caracteres iniciales
                    errorLabel.textContent = errorText.substring(4);
                } else {
                    // Si no, mostramos el mensaje por defecto
                    errorLabel.textContent = 'El nombre de usuario o correo electrónico ya está en uso.';
                }
                errorLabel.style.display = 'block'; // Muestra el label con el error
            } else if (response.status === 422) {
                // Entidad no procesable: validaciones fallidas
                const errorData = await response.json(); // Suponiendo que la API devuelve detalles de error en formato JSON
                errorLabel.textContent = 'Error en los datos ingresados. ' + errorData.message; // Cambia esto según la estructura de tu respuesta
                errorLabel.style.display = 'block'; // Muestra el label con el error
            } else {
                // Manejo de otros errores (por ejemplo, 500)
                errorLabel.textContent = 'Error inesperado. Por favor, intenta nuevamente más tarde.';
                errorLabel.style.display = 'block'; // Muestra el label con el error
            }
        } catch (error) {
            console.error('Error al registrarse:', error);
            errorLabel.textContent = 'Error de conexión. Por favor, intenta nuevamente.';
            errorLabel.style.display = 'block'; // Muestra el label con el error
        }

    };

    // Asignar el evento de envío del formulario
    form.addEventListener('submit', handleSubmit);
});

