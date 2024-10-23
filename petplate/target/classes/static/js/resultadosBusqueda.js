

//funcion para realizar la consulta a la base de datos y obtener los resultados de la busqueda
async function obtenerResultadosBusqueda(){
    let categoriaBusqueda = localStorage.getItem("categoriaBusqueda")
    let subcategoriaBusqueda = localStorage.getItem("subcategoriaBusqueda")
    const ingredientesBusqueda = JSON.parse(localStorage.getItem("ingredientesBusqueda")) || [];
    const tituloResultado = document.getElementById('resultado-busqueda-titulo');

    //si no hay una categoria valida la deja como un String vacio
    if(categoriaBusqueda === 'Animal'  || categoriaBusqueda === null){
        categoriaBusqueda = '';
    }

    //si no hay una sub categoria valida la deja como un String vacio
    if(subcategoriaBusqueda === 'Tipo' || subcategoriaBusqueda === null){
        subcategoriaBusqueda = '';
    }
    if(categoriaBusqueda === '' && ingredientesBusqueda.length === 0){
        tituloResultado.textContent = "No hay resultados disponibles."
        return
    }

    // Preparar los datos para el envío a la API
    let data = {
        categoryName: categoriaBusqueda,
        subcategoryName: subcategoriaBusqueda,
        ingredientes: ingredientesBusqueda
    };

    try {
        //respuesta
        const response = await fetch('/apiv1/recipe/search', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        
        if (response.status === 200) {
            const resultadoBusqueda = await response.json(); // Aquí se obtiene el JSON

            // Comprobar si hay resultados
            if (resultadoBusqueda.length === 0) {
                tituloResultado.textContent = "No se encontraron resultados para su búsqueda.";
                return;
            }

            const contenedorResultados = document.getElementById('main-resultados-busqueda');
            // Limpiar resultados anteriores en <main>
            contenedorResultados.innerHTML = ''; // Limpia el contenido anterior

            // Agregar título de la sección
            const tituloSection = document.createElement('h1');
            tituloSection.classList.add('titulo');
            tituloSection.textContent = "Resultados";
            contenedorResultados.appendChild(tituloSection);

            // Iterar sobre las recetas y crear la estructura HTML
            resultadoBusqueda.forEach(receta => {
                const cartaReceta = document.createElement('div');
                cartaReceta.classList.add('carta-receta');

                cartaReceta.innerHTML = `
                    <img src="${'/img/'+receta.category + '-avatar.png'}" alt="Avatar" class="receta-avatar">
                    <div class="receta-detalles">
                        <a href="/recipe/${receta.recipeId}" class="receta-detalles__titulo">${receta.title}</a>
                        <p class="receta-detalles__ingredientes">
                            ${receta.ingredientes.map(ingrediente => `<span>${ingrediente}</span>`).join(', ')}
                        </p>
                    </div>
                    <div class="receta-rating">
                        <span class="receta-rating__valor">${receta.score}</span>
                        <img src="/img/estrella.png" class="receta-rating__estrella">
                    </div>
                `;

                // Agregar la carta de la receta al contenedor <main>
                contenedorResultados.appendChild(cartaReceta);
            });



        }else{
            tituloResultado.textContent = "No se encontraron resultados para su búsqueda.";
            return;
            
        }
    } catch (error) {
        console.error('Error al buscar receta:', error);
        tituloResultado.textContent = "No se encontraron resultados para su búsqueda.";
        return;
    }

    //aqui se hace la consulta a la base de datos 

}

obtenerResultadosBusqueda()