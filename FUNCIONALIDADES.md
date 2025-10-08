# 📊 Sistema de Regresión Lineal - Documentación

## 🎯 Funcionalidades Implementadas

### Backend (Spring Boot + Kotlin)

#### Endpoints de DataPoints
- **POST** `/api/datapoints` - Crear nuevo punto
- **GET** `/api/datapoints/dataset/{id}` - Obtener puntos por dataset
- **GET** `/api/datapoints/{id}` - Obtener punto por ID
- **PUT** `/api/datapoints/{id}` - Actualizar punto
- **DELETE** `/api/datapoints/{id}` - Eliminar punto
- **DELETE** `/api/datapoints/dataset/{datasetId}` - Eliminar todos los puntos de un dataset

#### Endpoints de Regresión
- **POST** `/api/regresiones/dataset/{datasetId}` - Calcular regresión lineal
- **GET** `/api/regresiones/dataset/{datasetId}` - Obtener regresión por dataset
- **GET** `/api/regresiones` - Listar todas las regresiones
- **GET** `/api/regresiones/{id}` - Obtener regresión por ID
- **DELETE** `/api/regresiones/{id}` - Eliminar regresión
- **DELETE** `/api/regresiones/dataset/{datasetId}` - Eliminar regresión por dataset

#### Cálculo de Regresión Lineal
El sistema calcula automáticamente:
- **m** (pendiente): `m = (n·ΣXY - ΣX·ΣY) / (n·ΣX² - (ΣX)²)`
- **b** (intercepto): `b = (ΣY - m·ΣX) / n`
- **R²** (coeficiente de determinación): Mide qué tan bien se ajusta la línea a los datos

### Frontend (Vanilla JS + Chart.js)

#### Gestión de Puntos
- ✅ Visualización en tabla
- ✅ Añadir puntos con modal
- ✅ Editar puntos existentes
- ✅ Eliminar puntos con confirmación
- ✅ Validación de datos numéricos

#### Visualización con Chart.js
- ✅ Gráfico de dispersión (scatter) para los puntos
- ✅ Línea de regresión superpuesta en rojo
- ✅ Leyenda con ecuación de la recta
- ✅ Tooltips informativos
- ✅ Escalas automáticas

#### Cálculo de Regresión
- ✅ Botón "Calcular Regresión"
- ✅ Validación mínima de 2 puntos
- ✅ Mostrar ecuación: `y = mx + b`
- ✅ Mostrar valor de R²
- ✅ Carga automática de regresión existente
- ✅ Manejo de errores (regresión duplicada, etc.)

## 🚀 Cómo Usar

### 1. Iniciar el Backend
```bash
cd demo
./gradlew bootRun
```
El servidor estará disponible en `http://localhost:8080`

### 2. Abrir el Frontend
Abre en tu navegador:
```
file:///ruta/a/front/view.html?id=1
```
Donde `id=1` es el ID del dataset que quieres visualizar.

### 3. Flujo de Trabajo
1. **Ver puntos**: Al cargar la página, se muestran todos los puntos del dataset
2. **Añadir puntos**: Click en "+ Añadir Punto", ingresar valores X e Y
3. **Editar/Eliminar**: Usar botones en cada fila de la tabla
4. **Calcular regresión**: Click en "Calcular Regresión"
5. **Ver resultados**: La ecuación y R² aparecen debajo del gráfico

## 🔧 Archivos Modificados

### Backend
- `model/Regresion.kt` - Agregado `@JsonIgnoreProperties`
- `model/DataPoint.kt` - Agregado `@JsonIgnoreProperties`
- `controller/RegresionController.kt` - Endpoints de regresión
- `service/RegresionService.kt` - Lógica de cálculo

### Frontend
- `scripts/view.js` - Lógica principal y manejo de regresión
- `scripts/modules/chartManager.js` - Visualización con Chart.js
- `scripts/modules/pointsManager.js` - CRUD de puntos
- `scripts/api.js` - Comunicación con backend

## 📐 Fórmulas Implementadas

### Regresión Lineal Simple
```
y = mx + b

Donde:
- m = (n·ΣXY - ΣX·ΣY) / (n·ΣX² - (ΣX)²)
- b = (ΣY - m·ΣX) / n
- R² = 1 - (SS_res / SS_tot)
```

### Coeficiente de Determinación (R²)
```
R² = 1 - (Σ(y_i - ŷ_i)²) / (Σ(y_i - ȳ)²)

Donde:
- y_i = valor real
- ŷ_i = valor predicho (mx_i + b)
- ȳ = media de y
```

## ⚠️ Validaciones

### Backend
- Mínimo 2 puntos para calcular regresión
- No permite regresiones duplicadas para el mismo dataset
- Validación de puntos alineados verticalmente
- Dataset debe existir

### Frontend
- Validación de valores numéricos
- Confirmación antes de eliminar
- Manejo de errores con alertas
- Mensajes informativos en consola

## 🎨 Características Visuales

- **Puntos de datos**: Azul (#3498db)
- **Línea de regresión**: Rojo (#e74c3c)
- **Grosor de línea**: 3px
- **Radio de puntos**: 6px (8px al hover)
- **Extensión de línea**: 10% más allá de los puntos para mejor visualización

## 📊 Ejemplo de Respuesta JSON

### Regresión
```json
{
  "id": 1,
  "dataset": {
    "id": 1,
    "name": "dataset 1"
  },
  "m": -0.6875,
  "b": 10.75,
  "r2": 0.9758064516129032
}
```

### DataPoint
```json
{
  "id": 4,
  "dataset": {
    "id": 1,
    "name": "dataset 1"
  },
  "x": 12.0,
  "y": 2.0
}
```

## 🐛 Problemas Resueltos

1. ✅ **Serialización infinita**: Agregado `@JsonIgnoreProperties("regression")` en modelos
2. ✅ **Métodos ambiguos**: Eliminado método duplicado en controller
3. ✅ **Puerto ocupado**: Implementado reinicio correcto del servidor
4. ✅ **Visualización de regresión**: Chart.js configurado para mostrar línea y puntos

## 🎓 Tecnologías Utilizadas

- **Backend**: Kotlin, Spring Boot 3.5.6, JPA/Hibernate, MySQL
- **Frontend**: Vanilla JavaScript (ES6 Modules), Chart.js 4.x, HTML5, CSS3
- **Build**: Gradle 8.14.3
- **Base de datos**: MySQL 8.0.42

---
**Fecha de implementación**: 2025-10-08
**Estado**: ✅ Completamente funcional
