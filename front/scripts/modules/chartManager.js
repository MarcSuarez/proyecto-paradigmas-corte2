// Gestor del gráfico
export class ChartManager {
    constructor() {
        this.chart = null;
        this.canvas = document.getElementById('regressionChart');
        this.regression = null;
    }

    updateChart(dataPoints, regression = null) {
        // Destruir gráfico anterior si existe
        if (this.chart) {
            this.chart.destroy();
        }

        // Si no hay puntos, no crear el gráfico
        if (!dataPoints || dataPoints.length === 0) {
            return;
        }

        // Guardar la regresión
        this.regression = regression;

        // Preparar datasets
        const datasets = [
            {
                label: 'Puntos de datos',
                data: dataPoints.map(p => ({ x: p.x, y: p.y })),
                backgroundColor: '#3498db',
                borderColor: '#2980b9',
                pointRadius: 6,
                pointHoverRadius: 8,
                type: 'scatter'
            }
        ];

        // Si hay regresión, agregar la línea
        if (regression && regression.m !== undefined && regression.b !== undefined) {
            const regressionLine = this.calculateRegressionLine(dataPoints, regression.m, regression.b);
            datasets.push({
                label: `Regresión: y = ${regression.m.toFixed(4)}x + ${regression.b.toFixed(4)}`,
                data: regressionLine,
                borderColor: '#e74c3c',
                backgroundColor: 'rgba(231, 76, 60, 0.1)',
                borderWidth: 3,
                pointRadius: 0,
                type: 'line',
                fill: false,
                tension: 0
            });
        }

        // Crear nuevo gráfico
        this.chart = new Chart(this.canvas, {
            type: 'scatter',
            data: { datasets },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: {
                        type: 'linear',
                        position: 'bottom',
                        title: {
                            display: true,
                            text: 'X',
                            font: {
                                size: 14,
                                weight: 'bold'
                            }
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: 'Y',
                            font: {
                                size: 14,
                                weight: 'bold'
                            }
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: true,
                        position: 'top'
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                if (context.dataset.type === 'scatter') {
                                    return `(${context.parsed.x}, ${context.parsed.y})`;
                                } else {
                                    return `y = ${context.parsed.y.toFixed(4)}`;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    calculateRegressionLine(dataPoints, m, b) {
        if (!dataPoints || dataPoints.length === 0) {
            return [];
        }

        // Obtener el rango de X
        const xValues = dataPoints.map(p => p.x);
        const minX = Math.min(...xValues);
        const maxX = Math.max(...xValues);
        
        // Extender un poco el rango para mejor visualización
        const range = maxX - minX;
        const padding = range * 0.1;
        const startX = minX - padding;
        const endX = maxX + padding;

        // Calcular puntos de la línea
        return [
            { x: startX, y: m * startX + b },
            { x: endX, y: m * endX + b }
        ];
    }

    destroy() {
        if (this.chart) {
            this.chart.destroy();
            this.chart = null;
        }
    }
}
