const formLogin = document.getElementById('iniciar-sesion-form'); // Selecciona el formulario de Login
const formRegister = document.getElementById('crear-cuenta-form'); // Selecciona el formulario de register

document.addEventListener('DOMContentLoaded', () => {
    const botonCerrarSesionHeader = document.getElementById('boton-cerrar-sesion-header'); // selecciona el boton para ir al perfil del usuario


    botonCerrarSesionHeader.addEventListener('click', function() {
        localStorage.removeItem("token")
        localStorage.removeItem("userId")
        localStorage.removeItem("userImg")
        document.cookie = "token=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT"; // Borra la cookie
        //recarga la pagina
        const nuevaRuta = '/'; // Cambia esto por la ruta deseada

        // Verifica si ya está en la página
        if (window.location.pathname === nuevaRuta) {
            // Recarga la página si ya está en la ruta
            window.location.reload();
        } else {
            // Redirige si no está en la página
            window.location.href = nuevaRuta;
        }
    });
    
    function verificarSesion() {
        //obtengo las variables del localStorage
        let token = localStorage.getItem('token'); // Obtener el token desde localStorage
        let userImg = localStorage.getItem('userImg'); // Obtener la imagen del usuario
        let userId = localStorage.getItem('userId'); // Obtener la imagen del usuario
        let userRol = localStorage.getItem('userRol'); // Obtener el token desde localStorage

        // Obtengo los elementos del header mediante ID para cambiar sus propiedades
        let botonIniciarSesionHeader = document.getElementById('boton-iniciar-sesion-header'); // selecciona el boton para iniciar sesion
        let botonCrearRecetaHeader = document.getElementById('boton-crear-receta-header'); // selecciona el boton para crear receta
        let botonIrPerfilHeader = document.getElementById('boton-ir-a-perfil-header'); // selecciona el boton para ir al perfil del usuario
        let botonBackOficceHeader = document.getElementById('back-office-button'); // selecciona el boton para iniciar sesion
        // Verificar si el token existe; si es así, cambiar la vista a la del usuario logeado
        if (token !== null && token.trim() !== '') {
            
            // Ocultar el botón de iniciar sesión
            botonIniciarSesionHeader.style.display = 'none';
            
            // Mostrar el botón de crear receta
            let ruta = window.location.pathname;
            if (!ruta.startsWith("/recipe/create") && !ruta.startsWith("/recipe/modify")) {
                botonCrearRecetaHeader.style.display = 'block';
            }
            
            
            // Verificar si userImg es nulo y cambiar la ruta de la imagen de perfil si es necesario
            if (userImg === null || userImg.trim() === '' || userImg.trim() === 'null') {
                botonIrPerfilHeader.src = '/img/profile-img.png'; // Ruta por defecto si no hay imagen
            } else {
                botonIrPerfilHeader.src = '/user-pictures/' + userImg; // Asignar la imagen del usuario
            }
            
            // Mostrar la imagen de perfil
            botonIrPerfilHeader.style.display = 'block';

            botonCerrarSesionHeader.style.display = 'block'
            
            // Asignar redireccionamiento al hacer clic en la imagen de perfil
            botonIrPerfilHeader.addEventListener('click', function() {
                window.location.href = '/profile/' +  userId; // Cambia a la ruta del perfil
            });

            if (userRol === 'Administrador' && !ruta.startsWith("/backOffice")) {
                botonBackOficceHeader.style.display = 'inline-block';
            }


        } else {
            // Si no hay token, mostrar el botón de iniciar sesión
            botonIniciarSesionHeader.style.display = 'block';
            botonCrearRecetaHeader.style.display = 'none';
            botonIrPerfilHeader.style.display = 'none';
        }
    }

    verificarSesion()

    async function obtenerLikesUsuario() {
        // Obtener el token del localStorage
        const token = localStorage.getItem('token'); 

        // Si no hay token, abortar la solicitud
        if (!token) {
            console.error('No se encontró el token de usuario.');
            return;
        }
        // Realizar la solicitud POST al endpoint userLikes
        try {
            const response = await fetch(baseUrl +'/apiv1/userLikes', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ "token": token }) // Enviar el token en el cuerpo
            });

            // Manejar respuestas de la API
            if (response.ok) {
                const data = await response.json(); // Obtener los datos de los likes
                // Guardar los Likes en el localStorage
                localStorage.setItem('userLikes', JSON.stringify(data));
            } else {
                console.error('Ocurrió un error inesperado. Código de respuesta:', response.status);
            }
        } catch (error) {
            console.error('Error en la solicitud:', error);
            // Manejo de errores de comunicación con el servidor
        }
    }
    

    const handleSubmitLogin = async (event) => {
        //variablers para iniciarSesion
        
        let nombreUsuarioInputLogin = document.getElementById('nombre-usuario-iniciar-sesion'); // Input de nombre de usuario Login
        let contrasenaInputLogin = document.getElementById('contrasena-iniciar-sesion'); // Input de contraseña Login
        let errorLabelLogin = document.getElementById('error-label-iniciar-sesion'); // Label para los mensajes de error Login

        event.preventDefault(); // Evita el envío del formulario
        // Limpiar el mensaje de error al iniciar la verificación
        errorLabelLogin.textContent = ''; // vacia el label de error
        errorLabelLogin.style.display = 'none'; // Ocultar el label de error inicialmente

        //variables para las validaciones
        let isValid = true;
        let nombreUsuario = nombreUsuarioInputLogin.value.trim();
        let contrasena = contrasenaInputLogin.value;

        // Verificación si hay algún campo vacío
        if (!nombreUsuario || !contrasena) {
            errorLabelLogin.textContent = 'Todos los campos son obligatorios.';
            isValid = false;
        }  else
        // verifica si el nombre de usuario tiene entre 4 y 30 caracteres
        if (nombreUsuario.length < 4 || nombreUsuario.length > 30) {
            isValid = false;
        } else
        // verifica si el nombre de usuario tiene espacios
        if (nombreUsuario.includes(" ")) {
            isValid = false;
        } else
        //verifica que la contrasena contenga entre 8 y 20 caracteres
        if (contrasena.length < 8 || contrasena.length > 20) {
            isValid = false;
        } else
        //verifica que la contrasena no contenga espacios
        if (contrasena.includes(" ")) {
            isValid = false;
        }

        // Si alguna verificación falla, muestra el mensaje de error y evita el envío del formulario
        if (!isValid) {
            errorLabelLogin.textContent = 'El usuario o la contraseña son incorrectos.';
            errorLabelLogin.style.display = 'block'; // Muestra el label con el error
            return;
        }

        // Preparar los datos para el envío a la API
        let data = {
            user: nombreUsuario,
            password: contrasena
        };

        try {
            //respuesta
            const response = await fetch(baseUrl +'/apiv1/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });

            // Manejo de respuestas del servidor
            if (response.ok) {
                // Usuario logeado correctamente
                formLogin.reset(); // Limpia el formulario

                let responseData = await response.json(); // Convierte la respuesta en un objeto JSON
                let token = responseData.token; // Accede al campo 'token'
                let userId = responseData.userId; // Accede al campo 'userId'
                let userImg = responseData.userImg; // Accede al campo 'userImg'
                let userRol = responseData.userRol;
                
                // Guardar datos en el localStorage
                localStorage.setItem('token', token); //el token
                localStorage.setItem('userId', userId); //el id del usuario (para poder redireccionarlo a su perfil)

                localStorage.setItem("userImg", userImg)//la imagen del usuario para mostrarla en el header
                localStorage.setItem("userRol", userRol)

                if (userRol === 'Administrador') {
                    document.cookie = "token="+token+"; path=/; max-age=" + 60 * 60 * 24; // Expira en 24 horas
                }

                //vacia el areglo de ingredientes para refrescarlo
                localStorage.removeItem('ingredientes');

                await obtenerLikesUsuario()

                //recarga la pagina
                const nuevaRuta = '/'; // Cambia esto por la ruta deseada

                // Verifica si ya está en la página
                if (window.location.pathname === nuevaRuta) {
                    // Recarga la página si ya está en la ruta
                    window.location.reload();
                } else {
                    // Redirige si no está en la página
                    window.location.href = nuevaRuta;
                }

            } else if (response.status === 409 || response.status === 401) {
                errorLabelLogin.textContent = 'El usuario o la contraseña son incorrectos.';
                errorLabelLogin.style.display = 'block'; // Muestra el label con el error
            } else {

                // Manejo de otros errores (por ejemplo, 500)
                errorLabelLogin.textContent = 'Error inesperado. Por favor, intenta nuevamente más tarde.';
                errorLabelLogin.style.display = 'block'; // Muestra el label con el error
            }
            
        } catch (error) {
            console.error('Error al registrarse:', error);
            errorLabelLogin.textContent = 'Error de conexión. Por favor, intenta nuevamente.';
            errorLabelLogin.style.display = 'block'; // Muestra el label con el error
        }

    };

    formLogin.addEventListener('submit', handleSubmitLogin)




    //funcion para mandar el formulario de registrarse
    const handleSubmitRegister = async (event) => {
        //variables para crear cuenta
        let nombreUsuarioInputRegister = document.getElementById('nombre-usuario-crear-cuenta'); // Input de nombre de usuario register
        let correoInputRegister = document.getElementById('correo-electronico-crear-cuenta'); // Input de correo electrónico register
        let contrasenaInputRegister = document.getElementById('contrasena-crear-cuenta'); // Input de contraseña register
        let confirmarContrasenaInputRegister = document.getElementById('confirmar-contrasena-crear-cuenta'); // Input de confirmar contraseña register
        let errorLabelRegister = document.getElementById('error-label-crear-cuenta'); // Label para los mensajes de error register
        let nombreUsuarioInputLogin = document.getElementById('nombre-usuario-iniciar-sesion'); // Input de nombre de usuario Login
        
        


        event.preventDefault(); // Evita el envío del formulario

        // Limpiar el mensaje de error al iniciar la verificación
        errorLabelRegister.textContent = '';
        errorLabelRegister.style.display = 'none'; // Ocultar el label de error inicialmente

        // Variables de estado para las validaciones
        let isValid = true;
        let correo = correoInputRegister.value.trim();
        let nombreUsuario = nombreUsuarioInputRegister.value.trim();
        let contrasena = contrasenaInputRegister.value;
        let confirmarContrasena = confirmarContrasenaInputRegister.value.trim();
        let correoRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        // Verificación si hay algún campo vacío
        if (!nombreUsuario || !correo || !contrasena || !confirmarContrasena) {
            errorLabelRegister.textContent = 'Todos los campos son obligatorios.';
            isValid = false;
        } else

        // Verificación del nombre de usuario
        if (nombreUsuario.length < 4 || nombreUsuario.length > 30) {
            errorLabelRegister.textContent = 'El nombre de usuario debe contener entre 4 y 30 caracteres.';
            isValid = false;
        } else

        //verifica que el nombre de usuario no tenga espacios
        if (nombreUsuario.includes(" ")) {
            errorLabelRegister.textContent = 'El nombre de usuario no debe contener espacios.';
            isValid = false;
        } else

        //verifica si el correo tiene mas de 254 caractes
        if (correo.length > 254) {
            errorLabelRegister.textContent = 'El correo electrónico es demasiado largo.';
            isValid = false;
        } else

        //verifica si el correo contiene espacios
        if (correo.includes(" ")) {
            errorLabelRegister.textContent = 'El correo electrónico no debe contener espacios.';
            isValid = false;
        } else

        // Verificación del correo electrónico (usando una expresión regular básica)
        if (!correoRegex.test(correo)) {
            errorLabelRegister.textContent = 'El correo electrónico contiene un formato inválido.';
            isValid = false;
        } else

        
        // Verificación la contraseña tiene menos de 8 caracteres o mas de 20 caracteres
        if (contrasena.length < 8 || contrasena.length > 20) {
            errorLabelRegister.textContent = 'La contraseña debe contener entre 8 y 20 caracteres.';
            isValid = false;
        } else


        // Verificación la contraseña no contenga espacios
        if (contrasena.includes(" ")) {
            errorLabelRegister.textContent = 'La contraseña no debe contener espacios.';
            isValid = false;
        } else

        // Verificación de que las contraseñas coincida
        if (contrasena !== confirmarContrasena) {
            errorLabelRegister.textContent = 'Las contraseñas no coinciden.';
            isValid = false;
        }

        // Si alguna verificación falla, muestra el mensaje de error y evita el envío del formulario
        if (!isValid) {
            errorLabelRegister.style.display = 'block'; // Muestra el label con el error
            return;
        }

        // Preparar los datos para el envío a la API
        let data = {
            userName: nombreUsuario,
            userEmail: correo,
            userPassword: contrasena
        };

        try {
            const response = await fetch(baseUrl +'/apiv1/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });

            // Manejo de respuestas del servidor
            if (response.ok) {
                // Usuario registrado correctamente
                formRegister.reset(); // Limpia el formulario
                formLogin.reset(); //limpia el formulario de register
                //limpia el label de error de login
                const errorLabelLogin = document.getElementById('error-label-iniciar-sesion')
                errorLabelLogin.style.display = 'none';

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
                    errorLabelRegister.textContent = errorText.substring(4);
                } else {
                    // Si no, mostramos el mensaje por defecto
                    errorLabelRegister.textContent = 'El nombre de usuario o correo electrónico ya está en uso.';
                }
                errorLabelRegister.style.display = 'block'; // Muestra el label con el error
            } else {
                // Manejo de otros errores (por ejemplo, 500)
                errorLabelRegister.textContent = 'Error inesperado. Por favor, intenta nuevamente más tarde.';
                errorLabelRegister.style.display = 'block'; // Muestra el label con el error
            }
        } catch (error) {
            console.error('Error al registrarse:', error);
            errorLabelRegister.textContent = 'Error de conexión. Por favor, intenta nuevamente.';
            errorLabelRegister.style.display = 'block'; // Muestra el label con el error
        }

    };

    // Asignar el evento de envío del formulario de registrarse
    formRegister.addEventListener('submit', handleSubmitRegister);

});



