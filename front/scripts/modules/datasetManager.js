// Gestor del dataset
import { getData } from '../api.js';

export class DatasetManager {
    constructor(appState, uiManager) {
        this.appState = appState;
        this.uiManager = uiManager;
        this.titleElement = document.getElementById('datasetTitle');
    }

    async loadDataset() {
        try {
            const dataset = await getData(`/api/datasets/${this.appState.datasetId}`);
            this.appState.dataset = dataset;
            this.updateTitle();
            return dataset;
        } catch (error) {
            throw error;
        }
    }

    updateTitle() {
        if (this.appState.dataset) {
            this.titleElement.textContent = this.appState.dataset.name || 'Dataset sin nombre';
        }
    }
}
