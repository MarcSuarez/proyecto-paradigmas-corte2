# Sistema de Análisis de Regresión Lineal

Aplicación web full-stack para gestión de datasets y cálculo de regresión lineal con visualización interactiva de datos.

## Tabla de Contenidos

- [Características](#características)
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Configuración](#instalación-y-configuración)
- [Ejecución del Proyecto](#ejecución-del-proyecto)
- [Uso de la Aplicación](#uso-de-la-aplicación)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)

## Características

- ✅ **Gestión de Datasets**: Crear, editar y eliminar conjuntos de datos
- ✅ **Gestión de Puntos**: Agregar, modificar y eliminar puntos de datos (x, y)
- ✅ **Cálculo Automático**: Regresión lineal con actualización automática al modificar puntos
- ✅ **Visualización Interactiva**: Gráficos con Chart.js mostrando puntos y línea de regresión
- ✅ **Métricas Estadísticas**: Cálculo de pendiente (m), intercepto (b) y coeficiente de determinación (R²)
- ✅ **Interfaz Moderna**: UI responsive con diseño limpio y profesional

## Requisitos Previos

### 1. Java Development Kit (JDK)
- **Versión requerida**: JDK 17 o superior
- **Verificar instalación**:
  ```bash
  java -version
  ```
- **Descargar**: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) o [OpenJDK](https://adoptium.net/)

### 2. MySQL
- **Versión requerida**: MySQL 8.0 o superior
- **Verificar instalación**:
  ```bash
  mysql --version
  ```
- **Descargar**: [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)

### 3. Navegador Web Moderno
- Chrome, Firefox, Safari o Edge (versión reciente)

## Instalación y Configuración

### Paso 1: Clonar el Repositorio

```bash
git clone https://github.com/MarcSuarez/proyecto-paradigmas-corte2.git
cd proyecto_final
```

### Paso 2: Configurar la Base de Datos

1. **Iniciar MySQL**:
   ```bash
   # En Linux/Mac
   sudo systemctl start mysql

   # En Windows (como servicio)
   net start MySQL80
   ```

2. **Crear la base de datos**:
   ```bash
   mysql -u root -p
   ```
   
   Dentro de MySQL, ejecutar:
   ```sql
   CREATE DATABASE regresion_db;
   EXIT;
   ```

3. **Configurar variables de entorno:**
   Navegar al directorio demo del proyecto:
   ```bash
   cd demo
   ```

   Crear el archivo .env en el directorio demo:
   ```bash
   # En Linux/Mac
   touch .env

   # En Windows
   type nul > .env
   ```
   Editar el archivo .env y agregar las siguientes variables con tus credenciales:

   ```bash
   DB_USER=Tu Data
   DB_PASSWORD=Tu Data
   DB_HOST=Tu Data
   DB_PORT=Tu Data
   DB_NAME=Tu Data
   ```

4. **Configuración automática del backend:**

El backend de Spring Boot leerá automáticamente el archivo .env y configurará la conexión a la base de datos. No es necesario modificar manualmente el archivo application.properties.

### Abrir servidor backend

1. **Navegar al directorio del backend**:
   ```bash
   cd demo
   ```

2. **Ejecutar el servidor**:
   ```bash
   # En Linux/Mac
   ./gradlew bootRun
   
   # En Windows
   gradlew.bat bootRun
   ```

3. **Verificar que el servidor esté corriendo**:
   - Deberías ver en la consola: `Started DemoApplicationKt in X seconds`
   - El servidor estará disponible en: `http://localhost:8080`

### Opción 2: Usando el IDE

1. Abrir el proyecto en IntelliJ IDEA o tu IDE preferido
2. Esperar a que Gradle sincronice las dependencias
3. Ejecutar la clase principal: `DemoApplication.kt`

### Abrir el server del Frontend

1. **Navegar al directorio del frontend**:
   ```bash
   cd ../front
   ```

2. **Abrir en el navegador**:
   - Abrir el archivo `index.html` directamente en el navegador, o
   - Usar un servidor local:
     ```bash
     # Con Python 3
     python -m http.server 8000
     
     # Con Node.js (si tienes http-server instalado)
     npx http-server -p 8000
     ```
   - Acceder a: `http://localhost:8000`

## Uso de la Aplicación

### 1. Página Principal (Gestión de Datasets)

- **Crear Dataset**: Clic en "Añadir Conjunto de Datos", ingresar nombre
- **Ver Detalles**: Clic en "Ver" para acceder a los puntos del dataset
- **Editar**: Clic en "Editar" para cambiar el nombre
- **Eliminar**: Clic en "Eliminar" para borrar el dataset

### 2. Vista de Dataset (Gestión de Puntos y Regresión)

- **Añadir Punto**: Clic en "Añadir Punto", ingresar valores X e Y
- **Editar Punto**: Clic en "Editar" en la tabla de puntos
- **Eliminar Punto**: Clic en "Eliminar" en la tabla de puntos
- **Calcular Regresión**: Clic en "Calcular Regresión Lineal"
  - Se necesitan al menos 2 puntos
  - La regresión se actualiza automáticamente al modificar puntos
- **Visualizar**: El gráfico muestra los puntos y la línea de regresión
- **Métricas**: Se muestran los valores de m (pendiente), b (intercepto) y R²

## Estructura del Proyecto

```
proyecto_final/
├── demo/                           # Backend (Spring Boot + Kotlin)
│   ├── src/
│   │   └── main/
│   │       ├── kotlin/
│   │       │   └── com/example/demo/
│   │       │       ├── controller/     # Controladores REST
│   │       │       ├── model/          # Entidades JPA
│   │       │       ├── repository/     # Repositorios
│   │       │       ├── service/        # Lógica de negocio
│   │       │       └── dto/            # Data Transfer Objects
│   │       └── resources/
│   │           └── application.properties
│   ├── build.gradle.kts
│   └── gradlew
│
└── front/                          # Frontend (HTML + CSS + JS)
    ├── index.html                  # Página principal
    ├── view.html                   # Vista de dataset
    ├── styles/
    │   ├── styles.css
    │   └── view.css
    └── scripts/
        ├── api.js                  # Funciones de API
        ├── index.js                # Lógica página principal
        ├── view.js                 # Lógica vista dataset
        └── modules/                # Módulos organizados
            ├── chartManager.js
            ├── datasetManager.js
            ├── pointsManager.js
            └── uiManager.js
```

## Tecnologías Utilizadas

### Backend
- **Lenguaje**: Kotlin
- **Framework**: Spring Boot 3.x
- **Base de Datos**: MySQL 8.0
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Gradle

### Frontend
- **HTML5** + **CSS3** + **JavaScript ES6+**
- **Chart.js**: Visualización de gráficos
- **Fetch API**: Comunicación con el backend
- **Módulos ES6**: Arquitectura modular

## Solución de Problemas

### Error: "Access denied for user"
- Verificar credenciales en `application.properties`
- Asegurar que el usuario tenga permisos en la base de datos

### Error: "Port 8080 already in use"
- Cambiar el puerto en `application.properties`:
  ```properties
  server.port=8081
  ```
- Actualizar `API_BASE_URL` en `front/scripts/api.js`

### Error: "Cannot connect to database"
- Verificar que MySQL esté corriendo
- Verificar que la base de datos `regresion_db` exista
- Verificar la URL de conexión en `application.properties`

### El frontend no carga datos
- Verificar que el backend esté corriendo en `http://localhost:8080`
- Abrir la consola del navegador (F12) para ver errores
- Verificar CORS si usas un servidor local para el frontend

## API Endpoints

### Datasets
- `GET /api/datasets` - Listar todos los datasets
- `GET /api/datasets/{id}` - Obtener un dataset
- `POST /api/datasets` - Crear dataset
- `PUT /api/datasets/{id}` - Actualizar dataset
- `DELETE /api/datasets/{id}` - Eliminar dataset

### Data Points
- `GET /api/datapoints/dataset/{id}` - Listar puntos de un dataset
- `POST /api/datapoints` - Crear punto
- `PUT /api/datapoints/{id}` - Actualizar punto
- `DELETE /api/datapoints/{id}` - Eliminar punto

### Regresiones
- `GET /api/regresiones/dataset/{id}` - Obtener regresión de un dataset
- `POST /api/regresiones/dataset/{id}` - Calcular/actualizar regresión
- `DELETE /api/regresiones/{id}` - Eliminar regresión

## Autor

Proyecto desarrollado como parte del curso de Paradigmas de programacion

## Licencia

Este proyecto es de uso académico.
