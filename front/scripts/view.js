// Archivo principal de la vista de dataset
import { DatasetManager } from './modules/datasetManager.js';
import { PointsManager } from './modules/pointsManager.js';
import { ChartManager } from './modules/chartManager.js';
import { UIManager } from './modules/uiManager.js';
import { getData, postData } from './api.js';

// Estado global de la aplicación
const appState = {
    datasetId: null,
    dataset: null,
    dataPoints: [],
    regression: null
};

// Managers
let datasetManager;
let pointsManager;
let chartManager;
let uiManager;

// Inicialización
document.addEventListener('DOMContentLoaded', init);

function init() {
    // Obtener el ID del dataset de la URL
    const urlParams = new URLSearchParams(window.location.search);
    appState.datasetId = urlParams.get('id');
    
    if (!appState.datasetId) {
        alert('No se especificó un dataset válido');
        window.location.href = 'index.html';
        return;
    }
    
    // Inicializar managers
    uiManager = new UIManager();
    datasetManager = new DatasetManager(appState, uiManager);
    chartManager = new ChartManager();
    pointsManager = new PointsManager(appState, uiManager, chartManager, loadRegression);
    
    // Configurar eventos
    setupEventListeners();
    
    // Cargar datos iniciales
    loadInitialData();
}

function setupEventListeners() {
    // Botón volver
    const backButton = document.getElementById('backButton');
    backButton.addEventListener('click', () => {
        window.location.href = 'index.html';
    });
    
    // Botón añadir punto
    const addPointButton = document.getElementById('addPointButton');
    addPointButton.addEventListener('click', () => {
        pointsManager.openAddModal();
    });
    
    // Botón calcular regresión
    const calculateRegression = document.getElementById('calculateRegression');
    calculateRegression.addEventListener('click', async () => {
        await calculateRegressionHandler();
    });
}

async function loadInitialData() {
    try {
        uiManager.showLoading();
        
        // Cargar información del dataset
        await datasetManager.loadDataset();
        
        // Cargar puntos del dataset
        await pointsManager.loadPoints();
        
        // Cargar regresión si existe
        await loadRegression();
        
        uiManager.hideLoading();
    } catch (error) {
        console.error('Error al cargar datos:', error);
        uiManager.showError('Error al cargar los datos del dataset');
    }
}

async function loadRegression() {
    try {
        console.log('🔍 Cargando regresión para dataset:', appState.datasetId);
        const regression = await getData(`/api/regresiones/dataset/${appState.datasetId}`);
        
        if (regression && regression.m !== undefined) {
            console.log('✅ Regresión encontrada:', regression);
            appState.regression = regression;
            updateRegressionInfo(regression);
            chartManager.updateChart(appState.dataPoints, regression);
        } else {
            console.log('ℹ️ No hay regresión para este dataset');
            appState.regression = null;
        }
    } catch (error) {
        console.log('ℹ️ No hay regresión calculada aún:', error.message);
        appState.regression = null;
        hideRegressionInfo();
    }
}

async function calculateRegressionHandler() {
    try {
        // Validar que haya al menos 2 puntos
        if (appState.dataPoints.length < 2) {
            alert('Se necesitan al menos 2 puntos para calcular la regresión lineal');
            return;
        }

        console.log('📊 Calculando regresión para dataset:', appState.datasetId);
        
        // Llamar al endpoint para crear/calcular la regresión
        const regression = await postData(`/api/regresiones/dataset/${appState.datasetId}`, {});
        
        console.log('✅ Regresión calculada:', regression);
        
        // Actualizar estado
        appState.regression = regression;
        
        // Actualizar UI
        updateRegressionInfo(regression);
        
        // Actualizar gráfico con la línea de regresión
        chartManager.updateChart(appState.dataPoints, regression);
        
        alert('¡Regresión calculada exitosamente!');
    } catch (error) {
        console.error('❌ Error al calcular regresión:', error);
        if (error.message.includes('Ya existe una regresión')) {
            alert('Ya existe una regresión para este dataset. Recarga la página para verla.');
            await loadRegression();
        } else {
            alert(`Error al calcular la regresión: ${error.message}`);
        }
    }
}

function updateRegressionInfo(regression) {
    const regressionInfo = document.getElementById('regressionInfo');
    const slopeSpan = document.getElementById('slope');
    const interceptSpan = document.getElementById('intercept');
    const rSquaredSpan = document.getElementById('rSquared');
    
    if (regression && regression.m !== undefined && regression.b !== undefined) {
        slopeSpan.textContent = regression.m.toFixed(4);
        interceptSpan.textContent = regression.b.toFixed(4);
        rSquaredSpan.textContent = regression.r2 ? regression.r2.toFixed(4) : 'N/A';
        regressionInfo.classList.remove('hidden');
    }
}

// Hacer updateRegressionInfo accesible globalmente
window.updateRegressionInfo = updateRegressionInfo;

function hideRegressionInfo() {
    const regressionInfo = document.getElementById('regressionInfo');
    regressionInfo.classList.add('hidden');
}

// Exportar para uso global si es necesario
export { appState, pointsManager, chartManager, loadRegression };
