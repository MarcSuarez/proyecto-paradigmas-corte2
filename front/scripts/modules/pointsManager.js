// Gestor de puntos de datos
import { getData, postData, putData, deleteData } from '../api.js';

export class PointsManager {
    constructor(appState, uiManager, chartManager, loadRegressionCallback = null) {
        this.appState = appState;
        this.uiManager = uiManager;
        this.chartManager = chartManager;
        this.loadRegressionCallback = loadRegressionCallback;
        
        // Elementos DOM
        this.tableBody = document.getElementById('pointsTableBody');
        this.noPointsMessage = document.getElementById('noPointsMessage');
        this.modal = document.getElementById('pointModal');
        this.modalTitle = document.getElementById('modalTitle');
        this.pointXInput = document.getElementById('pointX');
        this.pointYInput = document.getElementById('pointY');
        
        // Estado del modal
        this.editingPointId = null;
        
        // Configurar eventos del modal
        this.setupModalEvents();
        
        // Exponer funciones globalmente para los botones onclick
        window.editPoint = this.editPoint.bind(this);
        window.deletePoint = this.deletePoint.bind(this);
    }

    setupModalEvents() {
        const closeBtn = document.getElementById('closeModal');
        const cancelBtn = document.getElementById('cancelModal');
        const saveBtn = document.getElementById('savePoint');
        
        closeBtn.addEventListener('click', () => this.closeModal());
        cancelBtn.addEventListener('click', () => this.closeModal());
        saveBtn.addEventListener('click', () => this.handleSavePoint());
        
        // Cerrar modal al hacer clic fuera
        this.modal.addEventListener('click', (e) => {
            if (e.target === this.modal) {
                this.closeModal();
            }
        });
        
        // Permitir guardar con Enter
        this.pointYInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                this.handleSavePoint();
            }
        });
    }

    async loadPoints() {
        try {
            console.log('🔍 Cargando puntos para dataset:', this.appState.datasetId);
            const points = await getData(`/api/datapoints/dataset/${this.appState.datasetId}`);
            console.log('✅ Puntos recibidos:', points);
            this.appState.dataPoints = points;
            this.renderTable();
            // Actualizar gráfico manteniendo la regresión si existe
            this.chartManager.updateChart(points, this.appState.regression);
        } catch (error) {
            console.error('❌ Error al cargar puntos:', error);
            this.appState.dataPoints = [];
            this.renderTable();
            this.chartManager.updateChart([]);
        }
    }

    renderTable() {
        const points = this.appState.dataPoints;
        console.log('📊 Renderizando tabla con puntos:', points);
        
        if (!points || points.length === 0) {
            console.log('⚠️ No hay puntos para mostrar');
            this.tableBody.innerHTML = '';
            this.noPointsMessage.classList.remove('hidden');
            return;
        }
        
        console.log('✅ Mostrando', points.length, 'puntos en la tabla');
        this.noPointsMessage.classList.add('hidden');
        
        this.tableBody.innerHTML = points.map(point => `
            <tr>
                <td>${point.x}</td>
                <td>${point.y}</td>
                <td class="actions-cell">
                    <button class="btn btn-edit" onclick="editPoint(${point.id})">
                        Editar
                    </button>
                    <button class="btn btn-delete" onclick="deletePoint(${point.id})">
                        Eliminar
                    </button>
                </td>
            </tr>
        `).join('');
    }

    // Modal - Añadir punto
    openAddModal() {
        this.editingPointId = null;
        this.modalTitle.textContent = 'Añadir Punto';
        this.pointXInput.value = '';
        this.pointYInput.value = '';
        this.modal.classList.remove('hidden');
        this.pointXInput.focus();
    }

    // Modal - Editar punto
    editPoint(pointId) {
        const point = this.appState.dataPoints.find(p => p.id === pointId);
        if (!point) return;
        
        this.editingPointId = pointId;
        this.modalTitle.textContent = 'Editar Punto';
        this.pointXInput.value = point.x;
        this.pointYInput.value = point.y;
        this.modal.classList.remove('hidden');
        this.pointXInput.focus();
    }

    closeModal() {
        this.modal.classList.add('hidden');
        this.editingPointId = null;
        this.pointXInput.value = '';
        this.pointYInput.value = '';
    }

    async handleSavePoint() {
        const x = parseFloat(this.pointXInput.value);
        const y = parseFloat(this.pointYInput.value);
        
        // Validación
        if (isNaN(x) || isNaN(y)) {
            alert('Por favor ingrese valores numéricos válidos');
            return;
        }
        
        try {
            let response;
            if (this.editingPointId) {
                // Actualizar punto existente
                response = await this.updatePoint(this.editingPointId, x, y);
            } else {
                // Crear nuevo punto
                response = await this.createPoint(x, y);
            }
            
            this.closeModal();
            await this.loadPoints();
            
            // Si la respuesta incluye la regresión actualizada, usarla directamente
            if (response && response.regression) {
                console.log('✅ Regresión actualizada recibida del backend:', response.regression);
                this.appState.regression = response.regression;
                this.chartManager.updateChart(this.appState.dataPoints, response.regression);
                // Actualizar la UI de regresión
                if (window.updateRegressionInfo) {
                    window.updateRegressionInfo(response.regression);
                }
            } else if (this.loadRegressionCallback) {
                // Fallback: recargar la regresión si no vino en la respuesta
                await this.loadRegressionCallback();
            }
        } catch (error) {
            console.error('Error al guardar punto:', error);
            alert(`Error al guardar el punto: ${error.message}`);
        }
    }

    async createPoint(x, y) {
        const newPoint = {
            x: x,
            y: y,
            dataset: {
                id: this.appState.datasetId
            }
        };
        
        return await postData('/api/datapoints', newPoint);
    }

    async updatePoint(pointId, x, y) {
        const updatedPoint = {
            x: x,
            y: y,
            dataset: {
                id: this.appState.datasetId
            }
        };
        
        return await putData(`/api/datapoints/${pointId}`, updatedPoint);
    }

    async deletePoint(pointId) {
        if (!confirm('¿Estás seguro de que deseas eliminar este punto?')) {
            return;
        }
        
        try {
            const response = await deleteData(`/api/datapoints/${pointId}`);
            await this.loadPoints();
            
            // Si la respuesta incluye la regresión actualizada, usarla directamente
            if (response && response.regression) {
                console.log('✅ Regresión actualizada recibida del backend:', response.regression);
                this.appState.regression = response.regression;
                this.chartManager.updateChart(this.appState.dataPoints, response.regression);
                // Actualizar la UI de regresión
                if (window.updateRegressionInfo) {
                    window.updateRegressionInfo(response.regression);
                }
            } else if (response && response.message) {
                // Si no hay regresión (menos de 2 puntos), ocultarla
                this.appState.regression = null;
                this.chartManager.updateChart(this.appState.dataPoints, null);
            } else if (this.loadRegressionCallback) {
                // Fallback: recargar la regresión si no vino en la respuesta
                await this.loadRegressionCallback();
            }
        } catch (error) {
            console.error('Error al eliminar punto:', error);
            alert(`Error al eliminar el punto: ${error.message}`);
        }
    }
}
