

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
            console.log("200 master")
        }else(
            console.log("ocurrio un error pero se hizo la consulta")
        )
    } catch (error) {
        console.error('Error al buscar receta:', error);
    }

    //aqui se hace la consulta a la base de datos 

}

obtenerResultadosBusqueda()