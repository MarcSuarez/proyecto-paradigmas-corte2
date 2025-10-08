import { DatasetManager } from './modules/datasetManager.js';
import { PointsManager } from './modules/pointsManager.js';
import { ChartManager } from './modules/chartManager.js';
import { UIManager } from './modules/uiManager.js';
import { getData, postData } from './api.js';

const appState = {
    datasetId: null,
    dataset: null,
    dataPoints: [],
    regression: null
};

let datasetManager;
let pointsManager;
let chartManager;
let uiManager;

document.addEventListener('DOMContentLoaded', init);

function init() {
    const urlParams = new URLSearchParams(window.location.search);
    appState.datasetId = urlParams.get('id');
    
    if (!appState.datasetId) {
        alert('No se especificó un dataset válido');
        window.location.href = 'index.html';
        return;
    }
    
    uiManager = new UIManager();
    datasetManager = new DatasetManager(appState, uiManager);
    chartManager = new ChartManager();
    pointsManager = new PointsManager(appState, uiManager, chartManager, loadRegression);
    
    setupEventListeners();
    loadInitialData();
}

function setupEventListeners() {
    const backButton = document.getElementById('backButton');
    backButton.addEventListener('click', () => {
        window.location.href = 'index.html';
    });
    
    const addPointButton = document.getElementById('addPointButton');
    addPointButton.addEventListener('click', () => {
        pointsManager.openAddModal();
    });
    
    const calculateRegression = document.getElementById('calculateRegression');
    calculateRegression.addEventListener('click', async () => {
        await calculateRegressionHandler();
    });
}

async function loadInitialData() {
    try {
        uiManager.showLoading();
        await datasetManager.loadDataset();
        await pointsManager.loadPoints();
        await loadRegression();
        uiManager.hideLoading();
    } catch (error) {
        uiManager.showError('Error al cargar los datos del dataset');
    }
}

async function loadRegression() {
    try {
        const regression = await getData(`/api/regresiones/dataset/${appState.datasetId}`);
        
        if (regression && regression.m !== undefined) {
            appState.regression = regression;
            updateRegressionInfo(regression);
            chartManager.updateChart(appState.dataPoints, regression);
        } else {
            appState.regression = null;
        }
    } catch (error) {
        appState.regression = null;
        hideRegressionInfo();
    }
}

async function calculateRegressionHandler() {
    try {
        if (appState.dataPoints.length < 2) {
            alert('Se necesitan al menos 2 puntos para calcular la regresión lineal');
            return;
        }

        const regression = await postData(`/api/regresiones/dataset/${appState.datasetId}`, {});
        
        appState.regression = regression;
        updateRegressionInfo(regression);
        chartManager.updateChart(appState.dataPoints, regression);
        
        alert('¡Regresión calculada exitosamente!');
    } catch (error) {
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

window.updateRegressionInfo = updateRegressionInfo;

function hideRegressionInfo() {
    const regressionInfo = document.getElementById('regressionInfo');
    regressionInfo.classList.add('hidden');
}

export { appState, pointsManager, chartManager, loadRegression };
