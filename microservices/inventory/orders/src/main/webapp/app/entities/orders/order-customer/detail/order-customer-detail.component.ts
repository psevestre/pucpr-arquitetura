import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IOrderCustomer } from '../order-customer.model';

@Component({
  selector: 'jhi-order-customer-detail',
  templateUrl: './order-customer-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class OrderCustomerDetailComponent {
  orderCustomer = input<IOrderCustomer | null>(null);

  previousState(): void {
    window.history.back();
  }
}
