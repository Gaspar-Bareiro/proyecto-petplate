//cargar el areglo de ingredientes si no lo tiene todavia
// Obtener los ingredientes del localStorage
// Obtener los ingredientes del localStorage
let ingredientesLocales = JSON.parse(localStorage.getItem('ingredientes'));
let ingredientes = ['ERROR']; // Inicialización de 'ingredientes' como una variable mutable

// Verificar si no existe en localStorage o si el arreglo está vacío
if (!ingredientesLocales || ingredientesLocales.length === 0) {
    // Realizar la solicitud GET al endpoint /apiv1/ingredients
    fetch('/apiv1/ingredients', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    })
    .then(response => {
        if (response.ok) {
            return response.json(); // Convertir la respuesta a JSON
        } else {
            throw new Error('Error en la respuesta del servidor');
        }
    })
    .then(data => {
        // Guardar los ingredientes en el localStorage
        localStorage.setItem('ingredientes', JSON.stringify(data));
        
        // Asignar el arreglo de ingredientes a la variable
        ingredientes = data;
        
    })
    .catch(error => {
        // Manejar cualquier error que ocurra durante la solicitud
        console.error('Error al obtener los ingredientes:', error);
    });
} else {
    // Si ya hay ingredientes almacenados
    ingredientes = ingredientesLocales; // Asignar el valor del localStorage
}


//SCRIP PARA ABRIR MODALES LOGIN Y REGISTER (hasta la linea 52 aprox)
// clases para abrir y cerrar la ventana modal de login--------------------------

// Selecciona todos los elementos que van a abrir el diálogo
const openModalButtonsLogin = document.querySelectorAll('.OPEN-modal-iniciar-sesion');

// Selecciona todos los elementos que van a cerrar el diálogo
const closeModalButtonsLogin = document.querySelectorAll('.CLOSE-modal-iniciar-sesion');

// Selecciona la ventana modal que se va a abrir o cerrar
const modalLogin = document.querySelector('.modal-iniciar-sesion');

// Añade el evento de clic a cada botón de apertura
openModalButtonsLogin.forEach(button => {
    button.addEventListener('click', function() {
        modalLogin.setAttribute('open', ''); // Muestra el diálogo
        formLogin.reset(); // Limpia el formulario Login
        formRegister.reset(); //limpia el formulario de register 
    });
});

// Añade el evento de clic a cada botón de cierre
closeModalButtonsLogin.forEach(button => {
    button.addEventListener('click', function() {
        modalLogin.removeAttribute('open'); // Elimina el atributo 'open' para cerrar el diálogo
        formLogin.reset(); // Limpia el formulario Login
        formRegister.reset(); //limpia el formulario de register
    });
});

// clases para abrir y cerrar la ventana modal de register--------------------------

// Selecciona todos los elementos que van a abrir el diálogo
const openModalButtonsRegister = document.querySelectorAll('.OPEN-modal-crear-cuenta');

// Selecciona todos los elementos que van a cerrar el diálogo
const closeModalButtonsRegister = document.querySelectorAll('.CLOSE-modal-crear-cuenta');

// Selecciona la ventana modal que se va a abrir o cerrar
const modalRegister = document.querySelector('.modal-crear-cuenta');

// Añade el evento de clic a cada botón de apertura
openModalButtonsRegister.forEach(button => {
    button.addEventListener('click', function() {
        modalRegister.setAttribute('open', ''); // Muestra el diálogo
    });
});

// Añade el evento de clic a cada botón de cierre
closeModalButtonsRegister.forEach(button => {
    button.addEventListener('click', function() {
        modalRegister.removeAttribute('open'); // Elimina el atributo 'open' para cerrar el diálogo
    });
});

//clase para abrir la ventana modal de buscar receta-----------------------------------------------

// Selecciona el elemento que va abrir la ventana modal
const openModalButtonBuscar = document.querySelectorAll('.OPEN-modal-buscar-receta');

// Selecciona la ventana modal a abrir
const modalBuscarReceta = document.querySelector('.modal-buscar-receta'); // Corregido el selector aquí

// Añade el evento de clic a cada botón de apertura
openModalButtonBuscar.forEach(button => {
    button.addEventListener('click', function() {
        modalBuscarReceta.setAttribute('open', ''); // Ajustado para establecer el atributo 'open' en el primer elemento seleccionado
    });
});


// codigo para centrar el boton de buscar receta
function igualarAncho() {
    // Obtener los elementos
    var elemento1 = document.getElementsByClassName("nav__list__item--flex")[0];
    var elemento2 = document.getElementsByClassName("item--logo")[0];

    // Obtener el ancho del primer elemento
    var anchoElemento1 = window.getComputedStyle(elemento1, null).getPropertyValue("width");

    // Asignar el mismo ancho al segundo elemento
    elemento2.style.width = anchoElemento1;
}

// Ejecutar la función después de que el contenido haya cargado
window.onload = igualarAncho;


// Script check box personalizado-----------------------------------------------------------------------


// Función para inicializar un combobox personalizado
function initCustomSelect(animalId, tipoId, opcionesMap) {
    // Selecciona los elementos del DOM para el combobox de animales y tipos, usando los IDs proporcionados
    const selectedAnimal = document.getElementById(animalId + '-animal-select-selected');
    const itemsAnimal = document.getElementById(animalId + '-animal-select-items');
    const selectedTipo = document.getElementById(tipoId + '-tipo-select-selected');
    const itemsTipo = document.getElementById(tipoId + '-tipo-select-items');
    const tipoContainer = document.getElementById(tipoId + '-tipo-custom-select');

    // Abrir/cerrar el combobox de animal cuando se hace clic en el elemento seleccionado
    selectedAnimal.addEventListener('click', () => {
        // Alterna entre mostrar ('block') y ocultar ('none') el menú de opciones
        itemsAnimal.style.display = itemsAnimal.style.display === 'block' ? 'none' : 'block';
    });


    // Gestiona la selección de una opción del combobox de animales
    itemsAnimal.addEventListener('click', (event) => {
        // Verifica si el elemento tiene un atributo data-value, lo que indica que es una opción válida
        if (event.target.dataset.value) {
            // Actualiza el texto mostrado en el combobox de animales
            selectedAnimal.textContent = event.target.textContent;
            // Llama a la función para actualizar el combobox de tipos con las opciones correspondientes
            actualizarComboBoxTipo(selectedAnimal.textContent, selectedTipo, itemsTipo, tipoContainer, opcionesMap);
        }
        // Oculta el menú de opciones después de seleccionar
        itemsAnimal.style.display = 'none';
    });

    // Abrir/cerrar el combobox de tipo (solo si está habilitado)
    selectedTipo.addEventListener('click', () => {
        // Solo abre el menú si la opacidad es 1, es decir, si el combobox de tipos está habilitado
        if (tipoContainer.style.opacity === '1') {
            // Alterna entre mostrar y ocultar las opciones de tipo
            itemsTipo.style.display = itemsTipo.style.display === 'block' ? 'none' : 'block';
        }
    });

    // Cierra ambos menús desplegables si se hace clic fuera de ellos
    document.addEventListener('click', (event) => {
        // Si el clic no fue dentro del contenedor del combobox de animales, cierra su menú
        if (!event.target.closest(`#${animalId}-animal-custom-select`)) {
            itemsAnimal.style.display = 'none';
        }
        // Si el clic no fue dentro del contenedor del combobox de tipos, cierra su menú
        if (!event.target.closest(`#${tipoId}-tipo-custom-select`)) {
            itemsTipo.style.display = 'none';
        }
    });

    // --- NUEVA LÓGICA: Al cargar la página, verificar si el animal ya es válido ---
    const animalActual = selectedAnimal.textContent; // Guarda el valor actual del animal
    if (opcionesMap[animalActual]) {
        // Si el valor actual de `selectedAnimal` tiene opciones válidas
        const tipoActual = selectedTipo.textContent; // Guarda el valor actual del tipo
        actualizarComboBoxTipo(animalActual, selectedTipo, itemsTipo, tipoContainer, opcionesMap);
        selectedTipo.textContent = tipoActual; // Restaura el valor del tipo después de la actualización
    }
}

// Función para actualizar las opciones del combobox "tipo" según el animal seleccionado
function actualizarComboBoxTipo(valorAnimal, selectedTipo, itemsTipo, tipoContainer, opcionesMap) {
    // Restablece el texto del combobox "tipo" a su valor por defecto
    selectedTipo.textContent = 'Tipo';
    // Obtiene el conjunto de opciones según el animal seleccionado, si no hay coincidencia, usa un arreglo vacío
    const opciones = opcionesMap[valorAnimal] || [];

    // Limpia el contenido actual del menú de opciones de tipo
    itemsTipo.innerHTML = '';
    if (opciones.length > 0) {
        // Si hay opciones, habilita el combobox de tipo ajustando la opacidad
        tipoContainer.style.opacity = '1';
        // Recorre cada opción y la agrega al combobox
        opciones.forEach((opcion) => {
            // Crea un nuevo div para la opción
            const newOption = document.createElement('div');
            // Asigna el valor de data-value para la opción
            newOption.setAttribute('data-value', opcion);
            // Define el texto que se muestra en la opción
            newOption.textContent = opcion;
            
            // Agrega un evento de clic a cada opción del menú
            newOption.addEventListener('click', () => {
                // Actualiza el texto del combobox de tipo con la opción seleccionada
                selectedTipo.textContent = opcion;
                // Cierra el menú desplegable
                itemsTipo.style.display = 'none';
            });

            // Agrega la nueva opción al contenedor de opciones
            itemsTipo.appendChild(newOption);
        });
    } else {
        // Si no hay opciones, deshabilita el combobox de tipo ajustando la opacidad y cerrando el menú
        tipoContainer.style.opacity = '0.5';
        itemsTipo.style.display = 'none';
    }
}

// Definir las opciones para cada animal
const opcionesMap = {
    'Perro': ["Pequeño", "Mediano", "Grande"],
    'Gato': ["Pequeño", "Mediano", "Grande"],
    'Tortuga': ["Terrestre", "Marina"],
    'Ave': ["Periquito", "Canario", "Agapornis", "Cacatúa", "Ninfa", "Loro Gris Africano", "Diamante Mandarín", "Cotorra", "Pionus", "Guacamayo"]
};

// Inicializar los dos comboboxes personalizados
initCustomSelect('BR', 'BR', opcionesMap); // Inicializa el combobox para "buscar recetas"



//combo box para ingredientes -------------------------------------------------------------------------------------


//termina array ingredientes
// Selecciona los componentes del DOM
const ingredienteInput = document.querySelector('.ingrediente-input'); // Input donde se escriben los ingredientes
const itemsIngrediente = document.querySelector('.ingrediente-select-items'); // Lista de opciones
const resultados = document.querySelector('.resultados'); // Contenedor donde se muestran los ingredientes seleccionados
let selectedIndex = -1; // Índice del elemento seleccionado en la lista

// Función para filtrar los ingredientes según el texto ingresado
const filtrarIngredientes = (texto) => {
    return ingredientes.filter(ingrediente => ingrediente.toLowerCase().startsWith(texto.toLowerCase())); // Filtra ingredientes que comienzan con el texto
};

// Función para actualizar las opciones en el combo box
const actualizarOpciones = (opciones) => {
    itemsIngrediente.innerHTML = ''; // Limpiar la lista actual
    const opcionesAmostrar = opciones.slice(0, 4); // Mostrar solo las primeras 4 opciones
    opcionesAmostrar.forEach(opcion => {
        const div = document.createElement('div'); // Crear un nuevo div para cada opción
        div.textContent = opcion; // Establecer el texto del div
        div.dataset.value = opcion; // Añadir un atributo de datos
        itemsIngrediente.appendChild(div); // Añadir el div a la lista
    });
    itemsIngrediente.style.display = opcionesAmostrar.length ? 'block' : 'none'; // Mostrar u ocultar la lista
};

// Evento al escribir en el input
ingredienteInput.addEventListener('input', (e) => {
    const valor = e.target.value; // Obtener el valor del input
    const ingredientesFiltrados = filtrarIngredientes(valor); // Filtrar los ingredientes
    actualizarOpciones(ingredientesFiltrados); // Actualizar las opciones mostradas
    selectedIndex = -1; // Reiniciar el índice al filtrar

    // Seleccionar la primera opción si hay opciones disponibles
    if (ingredientesFiltrados.length > 0) {
        selectedIndex = 0; // Establecer el índice en 0
        resaltarOpcion(itemsIngrediente.querySelectorAll('div')); // Resaltar la primera opción
    }
});

// Evento al seleccionar un ingrediente de la lista
itemsIngrediente.addEventListener('click', (event) => {
    if (event.target.dataset.value) { // Verificar si se hizo clic en una opción
        const contenido = event.target.textContent; // Obtener el texto del ingrediente
        
        // Verificar si ya existe un div con el mismo contenido
        if (!existeDivConContenido(contenido)) {
            const div = document.createElement('div'); // Crear un nuevo div para el ingrediente seleccionado
            div.textContent = contenido; // Establecer el texto del div
            resultados.appendChild(div); // Añadir el div a la lista de resultados

            // Añadir el evento para eliminar el div al hacer clic en él
            div.addEventListener('click', () => eliminarDiv(div));
        }

        // Vaciar el input y cerrar el desplegable
        ingredienteInput.value = '';
        itemsIngrediente.style.display = 'none'; // Ocultar la lista de opciones
    }
});

// Función para eliminar un div
const eliminarDiv = (div) => {
    div.remove(); // Eliminar el div del DOM
};

// Función para verificar si ya existe un div con el mismo contenido
const existeDivConContenido = (contenido) => {
    const divsExistentes = document.querySelectorAll('.resultados div'); // Seleccionar todos los divs en resultados
    return Array.from(divsExistentes).some(div => div.textContent === contenido); // Comprobar si existe un div con el mismo texto
};

// Navegación con flechas
ingredienteInput.addEventListener('keydown', (event) => {
    const items = itemsIngrediente.querySelectorAll('div'); // Seleccionar todos los elementos de la lista
    if (event.key === 'ArrowDown') { // Si se presiona la flecha hacia abajo
        selectedIndex = (selectedIndex + 1) % items.length; // Mover hacia abajo en la lista
        resaltarOpcion(items); // Resaltar la opción actual
        event.preventDefault(); // Evitar el desplazamiento de la página
    } else if (event.key === 'ArrowUp') { // Si se presiona la flecha hacia arriba
        selectedIndex = (selectedIndex - 1 + items.length) % items.length; // Mover hacia arriba en la lista
        resaltarOpcion(items); // Resaltar la opción actual
        event.preventDefault(); // Evitar el desplazamiento de la página
    } else if (event.key === 'Enter') { // Si se presiona "Enter"
        if (selectedIndex >= 0 && selectedIndex < items.length) { // Verificar si hay una opción seleccionada
            const contenido = items[selectedIndex].textContent; // Obtener el texto de la opción seleccionada
            agregarIngrediente(contenido); // Agregar el ingrediente a la lista de resultados
            itemsIngrediente.style.display = 'none'; // Cerrar el desplegable
        }
    }
});

// Resaltar opción actual
const resaltarOpcion = (items) => {
    items.forEach(item => item.style.backgroundColor = ''); // Limpiar resaltado de todas las opciones
    if (selectedIndex >= 0 && selectedIndex < items.length) {
        items[selectedIndex].style.backgroundColor = '#e0e0e0'; // Resaltar opción seleccionada
    }
};

// Función para agregar ingrediente a la lista de resultados
const agregarIngrediente = (contenido) => {
    if (!existeDivConContenido(contenido)) { // Verificar si el ingrediente ya fue agregado
        const div = document.createElement('div'); // Crear un nuevo div para el ingrediente
        div.textContent = contenido; // Establecer el texto del div
        resultados.appendChild(div); // Añadir el div a la lista de resultados
        
        // Añadir el evento para eliminar el div al hacer clic en él
        div.addEventListener('click', () => eliminarDiv(div));
    }
    ingredienteInput.value = ''; // Limpiar el input
    itemsIngrediente.style.display = 'none'; // Cerrar el desplegable
};

// Evento para cambiar el fondo de los elementos al pasar el mouse
itemsIngrediente.addEventListener('mouseover', (event) => {
    const items = itemsIngrediente.querySelectorAll('div'); // Seleccionar todos los elementos de la lista
    items.forEach(item => {
        if (item !== event.target) {
            item.style.backgroundColor = '#fff'; // Cambiar a blanco
        }
    });
});

// Evento para restaurar el fondo al salir del mouse
itemsIngrediente.addEventListener('mouseout', (event) => {
    const items = itemsIngrediente.querySelectorAll('div'); // Seleccionar todos los elementos de la lista
    items.forEach(item => {
        item.style.backgroundColor = ''; // Restaurar el fondo original
    });
});


// Cierra el desplegable si se hace clic fuera de él
document.addEventListener('click', (event) => {
    if (!event.target.closest('.buscar-por-ingrediente')) { // Verificar si el clic fue fuera del combo box
        itemsIngrediente.style.display = 'none'; // Cerrar la lista de opciones
    }
});

// Selecciona el botón y el contenedor de resultados
const closeButton = document.querySelector('.modal-buscar-receta__close-button');

// Añade el evento de click al botón
closeButton.addEventListener('click', (event) => {
    resultados.innerHTML = ''; // Borra todo el contenido del div 'resultados'
    ingredienteInput.value = ''; // Borra el contenido del combo box
});

