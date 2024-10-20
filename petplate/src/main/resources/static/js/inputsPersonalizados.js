

initCustomSelect('RC', 'RC', opcionesMap); // Inicializa el combobox para "crear recetas"

//ABRIR MODAL PAUTAS DE COMUNIDAD
// Selecciona el botón que va a abrir el diálogo
const openModalButtonPautas = document.querySelector('.OPEN-modal-pautas-comunidad');

// Selecciona la ventana modal que se va a abrir o cerrar
const modalPautas = document.querySelector('.modal-pautas-comunidad');

// Añade el evento de clic al botón de apertura
openModalButtonPautas.addEventListener('click', function() {
    modalPautas.setAttribute('open', ''); // Muestra el diálogo
});


//input type number --------------------------------------------------------------------------
const input = document.getElementById('CR-cantidad');

input.addEventListener('input', function() {
    // Guardar el valor actual
    let value = this.value;

    // Permitir solo dígitos, punto y coma
    value = value.replace(/[^0-9.,]/g, ''); // Permitir solo dígitos, punto y coma
    
    // Contar los puntos y comas
    const decimalCount = (value.match(/[.,]/g) || []).length;

    // Si hay más de un separador, eliminar el último ingresado
    if (decimalCount > 1) {
        // Mantener solo el primer punto o coma
        const firstSeparatorIndex = value.search(/[.,]/);
        value = value.slice(0, firstSeparatorIndex + 1) + value.slice(firstSeparatorIndex + 1).replace(/[.,]/g, '');
    }

    // Limitar a 6 caracteres (incluyendo el punto)
    if (value.length > 6) {
        value = value.slice(0, 6);
    }

    // Reemplazar comas por puntos para normalizar
    this.value = value.replace(/,/g, '.'); // Actualiza el valor del input
});

// desplegable UNIDAD ------------------------------------------------------------------------------------------------------

// Llama a la función con tus selectores
createCustomSelect('.BR-unidad-select-selected', '.BR-unidad-select-items');

//funcionalidad basica desplegable 
function createCustomSelect(selectedSelector, itemsSelector) {
    const selectedElement = document.querySelector(selectedSelector);
    const itemsElement = document.querySelector(itemsSelector);

    // Al hacer clic en el selected, el combobox alterna entre abierto y cerrado.
    selectedElement.addEventListener('click', () => {
        itemsElement.style.display = itemsElement.style.display === 'block' ? 'none' : 'block';
    });

    // Al hacer clic en una opción, actualiza el texto del combobox con la opción seleccionada y cierra el menú.
    itemsElement.addEventListener('click', (event) => {
        if (event.target.dataset.value) {
            selectedElement.textContent = event.target.textContent; // Actualiza el texto mostrado
            itemsElement.style.display = 'none'; // Cierra el desplegable
        }
    });

    // Cierra el desplegable si se hace clic fuera de él
    document.addEventListener('click', (event) => {
        if (!event.target.closest(selectedSelector)) {
            itemsElement.style.display = 'none';
        }
    });
}
//CODIGO ingredientes  --------------------------------------------------------------------------------------

// Referencias a los elementos del DOM
const ingredienteInputCR = document.querySelector('.CR-ingrediente-input'); // Referencia al input para el ingrediente
const itemsIngredienteCR = document.querySelector('.CR-ingrediente-select-items'); // Referencia a la lista desplegable de ingredientes
let selectedIndexCR = -1; // Índice del elemento seleccionado en la lista

// Funciones para manejar el desplegable
const abrirDesplegableCR = () => {
    itemsIngredienteCR.style.display = 'block'; // Muestra el desplegable
};

const cerrarDesplegableCR = () => {
    itemsIngredienteCR.style.display = 'none'; // Oculta el desplegable
    selectedIndexCR = -1; // Reinicia el índice al cerrar
};

const actualizarOpcionesCR = (texto) => {
    itemsIngredienteCR.innerHTML = ''; // Limpia las opciones actuales

    // Filtrar ingredientes que coinciden con el texto ingresado
    const ingredientesFiltrados = ingredientes.filter(ingrediente =>
        ingrediente.toLowerCase().startsWith(texto.toLowerCase()) // Filtra los ingredientes que empiezan con el texto
    );

    if (ingredientesFiltrados.length === 0) {
        cerrarDesplegableCR(); // Si no hay opciones, cierra el desplegable
        return;
    }

    // Mostrar las primeras 5 opciones filtradas
    ingredientesFiltrados.slice(0, 5).forEach((opcion, index) => {
        const div = document.createElement('div'); // Crea un nuevo div para la opción
        div.textContent = opcion; // Establece el texto del div
        div.classList.add('CR-opcion'); // Añade una clase para estilos
        div.dataset.value = opcion; // Almacena el valor de la opción

        // Evento al hacer clic en la opción
        div.addEventListener('click', () => seleccionarIngredienteCR(opcion)); // Selecciona el ingrediente al hacer clic
        itemsIngredienteCR.appendChild(div); // Añade el div a la lista de opciones
    });

    abrirDesplegableCR(); // Abre el desplegable para mostrar las opciones
};

// Función para seleccionar un ingrediente
const seleccionarIngredienteCR = (contenido) => {
    ingredienteInputCR.value = contenido; // Establece el valor del input al contenido seleccionado
    ingredienteInputCR.style.color = '#4a4'; // Cambia el color del texto a válido
    cerrarDesplegableCR(); // Cierra el desplegable
};

// Eventos para manejar interacciones con el input de ingrediente
ingredienteInputCR.addEventListener('focus', abrirDesplegableCR); // Abre el desplegable al enfocar el input
ingredienteInputCR.addEventListener('input', (e) => {
    const valor = e.target.value.trim(); // Obtiene el valor ingresado
    actualizarOpcionesCR(valor); // Actualiza las opciones del desplegable
    // Cambia el color del input según si el ingrediente es válido
    ingredienteInputCR.style.color = ingredientes.includes(valor.toLowerCase()) ? '#4a4' : '#000'; // Color según validez
});

// Evento para manejar la selección con el teclado
// Agrega una variable para rastrear si se usaron las flechas
let flechaUsadaCR = false;

ingredienteInputCR.addEventListener('keydown', (event) => {
    const opciones = itemsIngredienteCR.querySelectorAll('div'); // Obtiene todas las opciones
    if (event.key === 'Enter') {
        // Si se presiona Enter y no se han usado flechas
        if (!flechaUsadaCR && opciones.length > 0) {
            seleccionarIngredienteCR(opciones[0].dataset.value); // Selecciona el primer elemento
        } else if (opciones[selectedIndexCR]) {
            seleccionarIngredienteCR(opciones[selectedIndexCR].dataset.value); // Selecciona la opción actual
        }
        flechaUsadaCR = false; // Reinicia la variable
    } else if (event.key === 'ArrowDown') {
        flechaUsadaCR = true; // Indica que se usó la flecha hacia abajo
        selectedIndexCR = (selectedIndexCR + 1) % opciones.length; // Mueve hacia abajo en la lista de opciones
        resaltarOpcionCR(opciones); // Resalta la opción actual
    } else if (event.key === 'ArrowUp') {
        flechaUsadaCR = true; // Indica que se usó la flecha hacia arriba
        selectedIndexCR = (selectedIndexCR - 1 + opciones.length) % opciones.length; // Mueve hacia arriba en la lista de opciones
        resaltarOpcionCR(opciones); // Resalta la opción actual
    }
});

// Función para resaltar la opción actualmente seleccionada
const resaltarOpcionCR = (opciones) => {
    opciones.forEach((opcion, index) => {
        opcion.style.backgroundColor = index === selectedIndexCR ? '#e0e0e0' : ''; // Resalta la opción seleccionada
    });
};

// Evento para cerrar el desplegable si se hace clic fuera de este
document.addEventListener('click', (event) => {
    // Cierra el desplegable si el clic no es en el input o en la lista de opciones
    if (!event.target.closest('.CR-ingrediente-input') && !event.target.closest('.CR-ingrediente-select-items')) {
        cerrarDesplegableCR();
    }
});

// Agregar Ingredientes
const cantidadInputCR = document.querySelector('#CR-cantidad'); // Referencia al input de cantidad
const unidadSeleccionadaCR = document.querySelector('.BR-unidad-select-selected'); // Referencia a la unidad seleccionada
const agregarIngredienteCR = document.querySelector('.CR-agregar-ingrediente'); // Referencia al botón de agregar ingrediente
const resultadosCR = document.querySelector('.CR-resultados'); // Referencia al contenedor de resultados
const errorLabelCR = document.querySelector('.CR-error-capa-8'); // Referencia al label de errores

// Función para verificar si el ingrediente ya ha sido agregado
const ingredienteYaAgregadoCR = (ingrediente) => {
    // Obtiene todos los divs de ingredientes (que tienen la clase específica para ingredientes)
    const divsIngredientesCR = resultadosCR.querySelectorAll('.CR-resultado-ingrediente');
    
    // Comprueba si algún div ya contiene exactamente el mismo ingrediente
    return Array.from(divsIngredientesCR).some(div => div.textContent.trim() === ingrediente.trim());
};

// Función para agregar el ingrediente, cantidad y unidad a los resultados
const funcionAgregarIngredienteCR = () => {
    const ingredienteValorCR = ingredienteInputCR.value.trim(); // Obtiene el valor del ingrediente
    const cantidadValorCR = cantidadInputCR.value.trim(); // Obtiene el valor de cantidad
    const unidadValorCR = unidadSeleccionadaCR.textContent.trim(); // Obtiene la unidad seleccionada

    // Verifica que los campos no estén vacíos
    if (ingredienteValorCR && cantidadValorCR && unidadValorCR !== 'Medida' && Number(cantidadValorCR) > 0) {
        // Verifica si el ingrediente no ha sido agregado
        if (!ingredienteYaAgregadoCR(ingredienteValorCR)) {
            // Crear un nuevo div contenedor para el ingrediente
            const nuevoDivCR = document.createElement('div');

            // Crear div para el ingrediente
            const ingredienteDivCR = document.createElement('div');
            ingredienteDivCR.classList.add('CR-resultado-ingrediente'); // Añade una clase para el ingrediente
            ingredienteDivCR.textContent = ingredienteValorCR; // Establece el texto del ingrediente

            // Crear div para la cantidad
            const cantidadDivCR = document.createElement('div');
            cantidadDivCR.classList.add('CR-resultado-cantidad'); // Añade una clase para la cantidad
            cantidadDivCR.textContent = cantidadValorCR; // Establece el texto de la cantidad

            // Crear div para la unidad de medida
            const unidadDivCR = document.createElement('div');
            unidadDivCR.classList.add('CR-resultado-unidad'); // Añade una clase para la unidad de medida
            unidadDivCR.textContent = unidadValorCR; // Establece el texto de la unidad de medida

            // Añadir los divs al contenedor
            nuevoDivCR.appendChild(ingredienteDivCR); // Añade el div del ingrediente al contenedor
            nuevoDivCR.appendChild(cantidadDivCR); // Añade el div de la cantidad al contenedor
            nuevoDivCR.appendChild(unidadDivCR); // Añade el div de la unidad al contenedor
            resultadosCR.appendChild(nuevoDivCR); // Añade el contenedor a los resultados

            // Limpiar los campos después de agregar
            ingredienteInputCR.value = ''; // Limpia el input de ingrediente
            ingredienteInputCR.style.color = '#000'; // Cambia el color a negro
            cantidadInputCR.value = ''; // Limpia el input de cantidad
            unidadSeleccionadaCR.textContent = 'Medida'; // Restablece la unidad seleccionada
            errorLabelCR.style.display = 'none'; // Hacer invisible el label de error
        } else {
            // Mostrar mensaje de error si el ingrediente ya fue agregado
            errorLabelCR.textContent = 'El ingrediente ya ha sido agregado.'; 
            errorLabelCR.style.display = 'block'; // Hacer visible el label de error
        }
    } else {
        // Mostrar mensaje de error si los campos están vacíos
        //si no se selecciono un ingrediente
        if (!ingredienteValorCR) {
            errorLabelCR.textContent = 'Se debe seleccionar un ingrediente valido.'; 
        }else 
        //si la cantidad del ingrediente no se introdujo o es 0
        if (!cantidadValorCR || Number(cantidadValorCR) < 1) {
            errorLabelCR.textContent = 'La cantidad del ingrediente debe ser mayor a 0.'; 
        }else
        //se la medida no se selecciono
        if(unidadValorCR === 'Medida' ){
            errorLabelCR.textContent = 'No se selecciono ninguna unidad de medida para el ingrediente.';
        //por si acaso todo falla
        }else {
            errorLabelCR.textContent = 'Por favor, completa todos los campos correctamente.'; 
        }
        errorLabelCR.style.display = 'block'; // Hacer visible el label de error
    }
};

// Evento al hacer clic en el botón para agregar ingrediente
agregarIngredienteCR.addEventListener('click', funcionAgregarIngredienteCR);

// Código para la vista previa de imagen al subir ---------------------------------------------------------------------- 
const imagenInput = document.getElementById('imagenInput'); // Referencia al input de imagen
const imagenPreview = document.getElementById('CRimagenPreview'); // Referencia a la vista previa de la imagen

// Evento para manejar el cambio en el input de imagen
imagenInput.addEventListener('change', (event) => {
    const archivo = event.target.files[0]; // Obtiene el primer archivo seleccionado
    if (archivo) {
        const lector = new FileReader(); // Crea un nuevo FileReader
        lector.onload = (e) => {
            imagenPreview.src = e.target.result; // Establece la fuente de la imagen de vista previa
            imagenPreview.style.display = 'inline-block'; // Muestra la imagen de vista previa
        };
        lector.readAsDataURL(archivo); // Lee el archivo como una URL
    }
});



document.addEventListener('DOMContentLoaded', () => {
    // Selecciona el contenedor principal que tiene la clase 'CR-resultados'
    const resultadosContainer = document.querySelector('.CR-resultados');

    // Elimina los resultados al hacer clic
    // Verifica si el contenedor existe antes de aplicar eventos
    if (resultadosContainer) {
        // Agregar un evento al contenedor principal para que actúe en sus hijos
        resultadosContainer.addEventListener('click', (event) => {
            // Encuentra el div más cercano que contenga el ingrediente
            const clickedDiv = event.target.closest('.CR-resultado-ingrediente, .CR-resultado-cantidad, .CR-resultado-unidad');

            // Verifica que clickedDiv sea un hijo directo del contenedor
            if (clickedDiv) {
                // Elimina el div padre que contiene el ingrediente, cantidad y unidad
                const parentDiv = clickedDiv.parentNode; // Obtiene el div padre
                if (parentDiv && parentDiv.parentNode === resultadosContainer) {
                    resultadosContainer.removeChild(parentDiv); // Elimina el div padre
                }
            }
        });
    }
    
});
