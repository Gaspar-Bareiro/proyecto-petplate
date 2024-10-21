

//SCRIPT PARA ALTERAR EL ESTADO DE LOS ICONOS 
document.addEventListener('DOMContentLoaded', () => {
    /*
    //SCRIPT PARA ALTERAR EL ESTADO DE LOS ICONOS 
    // Selecciona el elemento con la clase 'RCT-reportar-receta'
    const botonReportarReceta = document.querySelector('.RCT-reportar-receta');
    // Define las rutas de las imágenes
    const imgReportarGris = '/img/Report.png';
    const imgReportarRojo = '/img/Report2.png';

    // Maneja el evento click
    botonReportarReceta.addEventListener('click', () => {
    // Alterna entre las dos imágenes
    if (botonReportarReceta.src.includes(imgReportarGris)) {
        botonReportarReceta.src = imgReportarRojo;
    } else {
        botonReportarReceta.src = imgReportarGris;
    }
    });*/

    //codigo a mantenr el de arriva se elimina
    let userLiked = false;
    const botonDarLikeReceta = document.querySelector('.RCT-recomendar-receta'); // boton para dar like a receta
    function obtenerIdReceta(){
        // Obtener la URL actual
        const url = window.location.href;

        // Dividir la URL en partes usando '/'
        const partes = url.split('/');

        // Obtener el ID de la receta, que sería el último elemento
        return partes[partes.length - 1];
    }
    function actualizarUserLiked(){
        userLiked = false;
        let token = localStorage.getItem('token'); // Obtener el token desde localStorage
        if (token !== null && token.trim() !== ''){
            

            // Obtener el ID de la receta, que sería el último elemento
            const recetaId = obtenerIdReceta();

            
            let ArrayLikes = localStorage.getItem('userLikes'); // Obtener los likes desde el local storage
            
            // Verificar si ArrayLikes no es null y convertirlo a un array
            if (ArrayLikes) {
                // Asumimos que los likes están guardados como un JSON string
                ArrayLikes = JSON.parse(ArrayLikes); // Convertir el string a un array
                // Comprobar si el recetaId está en el array de likes
                if (ArrayLikes.includes(parseInt(recetaId, 10))) {
                    userLiked = true;
                }
            }
        }

    }
    function actualizarBotonLike(){
        let token = localStorage.getItem('token'); // Obtener el token desde localStorage
        if (token !== null && token.trim() !== ''){
            const imgFavGris = '/img/Fav.png';
            const imgFavDorado = '/img/Fav2.png';
            if (userLiked) {
                botonDarLikeReceta.src = imgFavDorado;
            }else{
                botonDarLikeReceta.src = imgFavGris;
            }
        }else{
            botonDarLikeReceta.style.display = 'none'; // Ocultar el botón
        }
    }
    actualizarUserLiked()
    actualizarBotonLike()

    async function darLikeReceta(){
        // Obtener el token del localStorage
        const token = localStorage.getItem('token'); 
        // Si no hay token, abortar la solicitud
        if (!token) {
            console.error('No se encontró el token de usuario.');
            return;
        }
        // Realizar la solicitud POST al endpoint /apiv1/userLikes
        try {
            const response = await fetch('/apiv1/recipe/addRecommendation/' + obtenerIdReceta(), {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ "token": token }) // Enviar el token en el cuerpo
            });

            // Manejar respuestas de la API
            if (response.ok) {
                //agrega al array de localStorage "userLikes" parseInt(obtenerIdReceta(), 10)
                let ArrayLikes = localStorage.getItem('userLikes'); // Obtener los likes desde el local storage
                ArrayLikes = JSON.parse(ArrayLikes); // Convertir el string a un array
                ArrayLikes.push(parseInt(obtenerIdReceta(), 10)); // Agrega el nuevo ID al array
                localStorage.setItem('userLikes', JSON.stringify(ArrayLikes)); // Guarda el array actualizado en localStorage
                userLiked = true;
                actualizarBotonLike()
            }else if (response.status === 401) {//sesion invalida o caducada
                localStorage.removeItem('token');
                localStorage.removeItem('userId');
                localStorage.removeItem('userImg');
                // Recargar la página actual
                location.reload();
            }else{
                console.error('Ocurrió un error inesperado. Código de respuesta:', response.status);
            }
        } catch (error) {
            console.error('Error en la solicitud:', error);
            // Manejo de errores de comunicación con el servidor
        }
    }

    async function sacarLikeReceta(){
        // Obtener el token del localStorage
        const token = localStorage.getItem('token'); 
        // Si no hay token, abortar la solicitud
        if (!token) {
            console.error('No se encontró el token de usuario.');
            return;
        }
        // Realizar la solicitud POST al endpoint /apiv1/userLikes
        try {
            const response = await fetch('/apiv1/recipe/removeRecommendation/' + obtenerIdReceta(), {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ "token": token }) // Enviar el token en el cuerpo
            });

            // Manejar respuestas de la API
            if (response.ok) {
                //agrega al array de localStorage "userLikes" parseInt(obtenerIdReceta(), 10)
                let ArrayLikes = localStorage.getItem('userLikes'); // Obtener los likes desde el local storage
                ArrayLikes = JSON.parse(ArrayLikes); // Convertir el string a un array
                ArrayLikes = ArrayLikes.filter(num => num !== parseInt(obtenerIdReceta(), 10));
                localStorage.setItem('userLikes', JSON.stringify(ArrayLikes)); // Guarda el array actualizado en localStorage
                userLiked = false;
                actualizarBotonLike()
            }else if (response.status === 401) {//sesion invalida o caducada
                localStorage.removeItem('token');
                localStorage.removeItem('userId');
                localStorage.removeItem('userImg');
                // Recargar la página actual
                location.reload();
            }else{
                console.error('Ocurrió un error inesperado. Código de respuesta:', response.status);
            }
        } catch (error) {
            console.error('Error en la solicitud:', error);
            // Manejo de errores de comunicación con el servidor
        }
    }


    botonDarLikeReceta.addEventListener('click', () => {
        if (userLiked) {//si el usuario ya le dio like
            //le saca el like a la receta
            sacarLikeReceta()
        }else{//si el usuario tadavia no le dio like
            //agrega like a la receta
            darLikeReceta()
        }
    });

    


});



