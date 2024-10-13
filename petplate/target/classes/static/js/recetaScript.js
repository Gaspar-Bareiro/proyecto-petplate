

//SCRIPT PARA ALTERAR EL ESTADO DE LOS ICONOS 
document.addEventListener('DOMContentLoaded', () => {
    // Selecciona el elemento con la clase 'RCT-recomendar-receta'
    const imgElement = document.querySelector('.RCT-recomendar-receta');
    // Define las rutas de las imágenes
    const imgFav1 = 'img/Fav.png';
    const imgFav2 = 'img/Fav2.png';

    // Maneja el evento click
    imgElement.addEventListener('click', () => {
      // Alterna entre las dos imágenes
    if (imgElement.src.includes(imgFav1)) {
        imgElement.src = imgFav2;
    } else {
        imgElement.src = imgFav1;
    }
    });
});

//SCRIPT PARA ALTERAR EL ESTADO DE LOS ICONOS 
document.addEventListener('DOMContentLoaded', () => {
    // Selecciona el elemento con la clase 'RCT-reportar-receta'
    const imgElement = document.querySelector('.RCT-reportar-receta');
    // Define las rutas de las imágenes
    const imgFav1 = 'img/Report.png';
    const imgFav2 = 'img/Report2.png';

    // Maneja el evento click
    imgElement.addEventListener('click', () => {
      // Alterna entre las dos imágenes
    if (imgElement.src.includes(imgFav1)) {
        imgElement.src = imgFav2;
    } else {
        imgElement.src = imgFav1;
    }
    });
});

