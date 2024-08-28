import { NgModule, LOCALE_ID } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import locale from '@angular/common/locales/en';
import { BrowserModule, Title } from '@angular/platform-browser';
import { ServiceWorkerModule } from '@angular/service-worker';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { NgxWebstorageModule } from 'ngx-webstorage';
import dayjs from 'dayjs/esm';
import { NgbDateAdapter, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import './config/dayjs';
import { SharedModule } from 'app/shared/shared.module';
import { TranslationModule } from 'app/shared/language/translation.module';
import { AppRoutingModule } from './app-routing.module';
import { HomeModule } from './home/home.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { NgbDateDayjsAdapter } from './config/datepicker-adapter';
import { fontAwesomeIcons } from './config/font-awesome-icons';
import { httpInterceptorProviders } from './core/interceptor';
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { PriceHistoryChartComponent } from './entities/my-chart/my-chart.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { FormsModule } from '@angular/forms';
import { OrderStatusComponent } from './charts/order-status/order-status.component';
import { TopProductsComponent } from './charts/top-products/top-products.component';
import { SalesByDayComponent } from './charts/sales-by-day/sales-by-day.component';
import { SalesByWeekComponent } from './charts/sales-by-week/sales-by-week.component';
import { SalesByMonthComponent } from './charts/sales-by-month/sales-by-month.component';
import { PaymentMethodComponent } from './charts/payment-method/payment-method.component';
import { CancellationReasonsComponent } from './charts/cancellation-reasons/cancellation-reasons.component';
import { SalesByHourComponent } from './charts/sales-by-hour/sales-by-hour.component';

@NgModule({
  imports: [
    FormsModule,
    CommonModule,

    BrowserModule,
    SharedModule,
    HomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    AppRoutingModule,
    // Set this to true to enable service worker (PWA)
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
    HttpClientModule,
    NgxWebstorageModule.forRoot({
      prefix: 'jhi',
      separator: '-',
      caseSensitive: true,
    }),
    TranslationModule,
  ],
  providers: [
    Title,
    { provide: LOCALE_ID, useValue: 'en' },
    {
      provide: NgbDateAdapter,
      useClass: NgbDateDayjsAdapter,
    },
    httpInterceptorProviders,
  ],
  declarations: [
    MainComponent,
    NavbarComponent,
    ErrorComponent,
    PageRibbonComponent,
    ActiveMenuDirective,
    FooterComponent,
    PriceHistoryChartComponent,
    DashboardComponent,
    OrderStatusComponent,
    TopProductsComponent,
    SalesByDayComponent,
    SalesByWeekComponent,
    SalesByMonthComponent,
    PaymentMethodComponent,
    CancellationReasonsComponent,
    SalesByHourComponent,
  ],
  bootstrap: [MainComponent],
})
export class AppModule {
  constructor(applicationConfigService: ApplicationConfigService, iconLibrary: FaIconLibrary, dpConfig: NgbDatepickerConfig) {
    applicationConfigService.setEndpointPrefix(SERVER_API_URL);
    registerLocaleData(locale);
    iconLibrary.addIcons(...fontAwesomeIcons);
    dpConfig.minDate = {
      year: dayjs().subtract(100, 'year').year(),
      month: 1,
      day: 1,
    };
  }
}
