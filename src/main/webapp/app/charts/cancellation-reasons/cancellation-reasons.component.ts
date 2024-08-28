import { Component, OnInit, OnDestroy } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { Subscription } from 'rxjs';
import { ProductCancellationsService } from '../../entities/product-cancellations/service/product-cancellations.service';

@Component({
  selector: 'jhi-cancellation-reasons',
  templateUrl: './cancellation-reasons.component.html',
  styleUrls: ['./cancellation-reasons.component.scss'],
})
export class CancellationReasonsComponent implements OnInit, OnDestroy {
  private cancellationReasonsChart: Chart | undefined;
  private subscriptions: Subscription[] = [];

  constructor(private productCancellationsService: ProductCancellationsService) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    this.subscriptions.push(
      this.productCancellationsService.getProductCancellationReasons().subscribe(reasons => {
        this.createPieChart(this.processReasons(reasons));
      })
    );
  }

  ngOnDestroy(): void {
    this.destroyChart();
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  private processReasons(reasons: string[]): { labels: string[]; data: number[] } {
    const reasonCount = reasons.reduce((acc, reason) => {
      acc[reason] = (acc[reason] || 0) + 1;
      return acc;
    }, {} as { [key: string]: number });

    return {
      labels: Object.keys(reasonCount),
      data: Object.values(reasonCount),
    };
  }

  private createPieChart(chartData: { labels: string[]; data: number[] }): void {
    const ctx = (document.getElementById('cancellationReasonsChart') as HTMLCanvasElement).getContext('2d');

    if (ctx) {
      this.cancellationReasonsChart = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: chartData.labels,
          datasets: [
            {
              label: 'Cancellation Reasons',
              data: chartData.data,
              backgroundColor: [
                'rgba(255, 99, 132, 0.2)', // Soft Pink
                'rgba(54, 162, 235, 0.2)', // Soft Blue
                'rgba(255, 206, 86, 0.2)', // Soft Yellow
                'rgba(75, 192, 192, 0.2)', // Soft Green
                'rgba(153, 102, 255, 0.2)', // Soft Purple
                'rgba(255, 159, 64, 0.2)', // Soft Orange
              ],
              borderColor: [
                'rgba(255, 99, 132, 1)', // Darker Pink
                'rgba(54, 162, 235, 1)', // Darker Blue
                'rgba(255, 206, 86, 1)', // Darker Yellow
                'rgba(75, 192, 192, 1)', // Darker Green
                'rgba(153, 102, 255, 1)', // Darker Purple
                'rgba(255, 159, 64, 1)', // Darker Orange
              ],
              borderWidth: 1,
            },
          ],
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              position: 'top',
            },
            tooltip: {
              callbacks: {
                label: function (context) {
                  const label = context.label || '';
                  const value = context.raw || 0;
                  return `${label}: ${value}`;
                },
              },
            },
          },
        },
      });
    }
  }

  private destroyChart(): void {
    if (this.cancellationReasonsChart) {
      this.cancellationReasonsChart.destroy();
    }
  }
}
