import { Component, OnInit } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { PriceHistoryService } from '../../entities/price-history/service/price-history.service';

@Component({
  selector: 'jhi-app-price-history-chart',
  templateUrl: './price-history-chart.component.html',
  styleUrls: ['./price-history-chart.component.scss'],
})
export class PriceHistoryChartComponent implements OnInit {
  constructor(private priceHistoryService: PriceHistoryService) {}

  ngOnInit(): void {
    Chart.register(...registerables);
    this.loadChart();
  }

  private loadChart(): void {
    const productId = '47bb45c2-8326-4bc8-9d52-627c17dab636'; // Replace with actual product ID
    this.priceHistoryService.getPriceHistoriesByProductId('s').subscribe(data => {
      console.log('Data:', data); // Debugging
      const labels = data.map(d => new Date(d.createdDate).toLocaleDateString());
      const oldPrices = data.map(d => d.oldPrice ?? 0);
      const newPrices = data.map(d => d.newPrice ?? 0);

      console.log('Labels:', labels); // Debugging
      console.log('Old Prices:', oldPrices); // Debugging
      console.log('New Prices:', newPrices); // Debugging

      new Chart('priceHistoryChart', {
        type: 'line',
        data: {
          labels: labels,
          datasets: [
            {
              label: 'Old Price',
              data: oldPrices,
              borderColor: 'red',
              backgroundColor: 'rgba(255, 0, 0, 0.2)',
              fill: false,
            },
            {
              label: 'New Price',
              data: newPrices,
              borderColor: 'blue',
              backgroundColor: 'rgba(0, 0, 255, 0.2)',
              fill: false,
            },
          ],
        },
        options: {
          responsive: true,
          scales: {
            x: {
              title: {
                display: true,
                text: 'Date',
              },
            },
            y: {
              title: {
                display: true,
                text: 'Price',
              },
              suggestedMin: 0,
            },
          },
        },
      });
    });
  }
}
