// Constantes
const API_BASE_URL = 'http://localhost:8080';

// Función auxiliar para manejar las respuestas de fetch
async function handleResponse(response) {
    if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        const errorMessage = errorData.message || `Error HTTP: ${response.status}`;
        throw new Error(errorMessage);
    }
    return response.json();
}

// Funciones para interactuar con la API
export async function getData(endpoint) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`);
        return handleResponse(response);
    } catch (error) {
        console.error('Error en getData:', error);
        throw error;
    }
}

export async function postData(endpoint, data) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });
        return handleResponse(response);
    } catch (error) {
        console.error('Error en postData:', error);
        throw error;
    }
}

export async function putData(endpoint, data) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });
        return handleResponse(response);
    } catch (error) {
        console.error('Error en putData:', error);
        throw error;
    }
}

export async function deleteData(endpoint) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'DELETE',
        });
        // En algunas APIs, DELETE puede no devolver contenido, así que manejamos el status
        if (response.status === 200 || response.status === 204) {
            return { success: true };
        } else {
            throw new Error(`Error HTTP: ${response.status}`);
        }
    } catch (error) {
        console.error('Error en deleteData:', error);
        throw error;
    }
}