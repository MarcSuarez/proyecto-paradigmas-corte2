import { getData, postData, putData, deleteData } from './api.js';

const ENDPOINT = '/api/datasets';

// Elementos DOM
const tableBody = document.getElementById('tableBody');
const addButton = document.getElementById('addButton');
const loadingMessage = document.getElementById('loadingMessage');
const errorMessage = document.getElementById('errorMessage');

// Estado de la aplicación
let data = [];

// Inicialización
document.addEventListener('DOMContentLoaded', init);

function init() {
    loadData();
    setupEventListeners();
}

function setupEventListeners() {
    addButton.addEventListener('click', handleAdd);
}

async function loadData() {
    try {
        showLoading();
        data = await getData(ENDPOINT);
        renderTable();
        hideLoading();
    } catch (error) {
        showError();
    }
}

function renderTable() {
    if (data.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="2" style="text-align: center;">No hay conjuntos de datos disponibles</td></tr>';
        return;
    }

    tableBody.innerHTML = data.map(item => `
        <tr>
            <td>${item.name || 'Sin nombre'}</td>
            <td class="actions-cell">
                <button class="btn btn-edit" onclick="handleEdit(${item.id})">
                    Editar
                </button>
                <button class="btn btn-delete" onclick="handleDelete(${item.id})">
                    Eliminar
                </button>
                <button class="btn btn-view" onclick="viewDataset(${item.id})">
                    Ver
                </button>
            </td>
        </tr>
    `).join('');
}

// Función para ver un dataset específico
window.viewDataset = function(id) {
    // Redirigir a una página de visualización o abrir un modal
    window.location.href = `view.html?id=${id}`;
};

// Funciones de utilidad
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const options = { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' };
    return new Date(dateString).toLocaleDateString('es-ES', options);
}

function showLoading() {
    loadingMessage.classList.remove('hidden');
    errorMessage.classList.add('hidden');
}

function hideLoading() {
    loadingMessage.classList.add('hidden');
}

function showError() {
    loadingMessage.classList.add('hidden');
    errorMessage.classList.remove('hidden');
}

// Manejadores de eventos
function handleAdd() {
    const name = prompt('Nombre del conjunto de datos:');
    
    if (name && name.trim() !== '') {
        const newDataset = {
            name: name.trim()
        };
        
        postData(ENDPOINT, newDataset)
            .then(() => {
                loadData();
            })
            .catch(error => {
                alert(`Error al crear el conjunto de datos: ${error.message}`);
            });
    }
}

window.handleEdit = async function(id) {
    const item = data.find(item => item.id === id);
    if (item) {
        const newName = prompt('Nuevo nombre:', item.name || '');
        
        if (newName !== null && newName !== '') {
            const updatedItem = {
                name: newName
            };
            
            try {
                await putData(`${ENDPOINT}/${id}`, updatedItem);
                loadData();
            } catch (error) {
                alert(`Error al actualizar: ${error.message}`);
            }
        }
    }
};

window.handleDelete = async function(id) {
    if (confirm('¿Estás seguro de que deseas eliminar este conjunto de datos?')) {
        try {
            await deleteData(`${ENDPOINT}/${id}`);
            // Actualizar la tabla después de eliminar
            data = data.filter(item => item.id !== id);
            renderTable();
        } catch (error) {
            alert(`Error al eliminar: ${error.message}`);
        }
    }
};