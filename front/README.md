# Aplicación de Gestión de Datasets para Regresión Lineal

Esta es una aplicación web para gestionar conjuntos de datos utilizados en análisis de regresión lineal. La aplicación permite crear, ver, editar y eliminar conjuntos de datos, así como visualizar los datos y realizar análisis de regresión lineal.

## Características

- **Gestión de Datasets**: Crea, lee, actualiza y elimina conjuntos de datos.
- **Interfaz Intuitiva**: Diseño limpio y fácil de usar.
- **Visualización de Datos**: Muestra los datos en tablas interactivas.
- **Análisis de Regresión Lineal**: Realiza análisis de regresión lineal sobre los datos cargados.

## Requisitos Previos

- Navegador web moderno (Chrome, Firefox, Safari, Edge)
- Node.js y npm (para desarrollo)
- Servidor backend ejecutándose (ver la sección de configuración del backend)

## Instalación

1. Clona el repositorio:
   ```bash
   git clone [URL_DEL_REPOSITORIO]
   cd proyecto_final/front
   ```

2. Instala las dependencias (si es necesario):
   ```bash
   npm install
   ```

3. Configura la URL del backend:
   - Abre el archivo `scripts/api.js`
   - Asegúrate de que `API_BASE_URL` apunte a la URL correcta de tu backend

## Uso

1. Asegúrate de que el servidor backend esté en ejecución.

2. Abre el archivo `index.html` en tu navegador o inicia un servidor local:
   ```bash
   npx serve
   ```
   Luego abre http://localhost:5000 en tu navegador.

3. Interactúa con la aplicación:
   - Haz clic en "Añadir" para crear un nuevo conjunto de datos
   - Usa los botones de acción para editar o eliminar conjuntos de datos existentes
   - Haz clic en "Ver" para visualizar un conjunto de datos y realizar análisis

## Estructura del Proyecto

- `index.html` - Página principal de la aplicación
- `scripts/`
  - `api.js` - Funciones para interactuar con la API del backend
  - `index.js` - Lógica principal de la aplicación
- `styles/`
  - `index.css` - Estilos de la aplicación

## Configuración del Backend

Asegúrate de que tu servidor backend esté configurado correctamente y que la URL en `scripts/api.js` apunte a la dirección correcta de tu API.

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.
