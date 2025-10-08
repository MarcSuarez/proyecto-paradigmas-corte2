// Gestor de la interfaz de usuario
export class UIManager {
    constructor() {
        this.loadingMessage = document.getElementById('loadingMessage');
        this.errorMessage = document.getElementById('errorMessage');
    }

    showLoading() {
        this.loadingMessage.classList.remove('hidden');
        this.errorMessage.classList.add('hidden');
    }

    hideLoading() {
        this.loadingMessage.classList.add('hidden');
    }

    showError(message = 'Error al cargar los datos') {
        this.loadingMessage.classList.add('hidden');
        this.errorMessage.classList.remove('hidden');
        this.errorMessage.textContent = message;
    }

    hideError() {
        this.errorMessage.classList.add('hidden');
    }
}
