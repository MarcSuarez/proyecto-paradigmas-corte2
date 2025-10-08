const API_BASE_URL = 'http://localhost:8080';

async function handleResponse(response) {
    if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        const errorMessage = errorData.message || errorData.error || `Error HTTP: ${response.status}`;
        throw new Error(errorMessage);
    }
    return response.json();
}
export async function getData(endpoint) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`);
        return handleResponse(response);
    } catch (error) {
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
        throw error;
    }
}

export async function deleteData(endpoint) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'DELETE',
        });
        if (response.status === 200) {
            const text = await response.text();
            return text ? JSON.parse(text) : { success: true };
        } else if (response.status === 204) {
            return { success: true };
        } else {
            throw new Error(`Error HTTP: ${response.status}`);
        }
    } catch (error) {
        throw error;
    }
}