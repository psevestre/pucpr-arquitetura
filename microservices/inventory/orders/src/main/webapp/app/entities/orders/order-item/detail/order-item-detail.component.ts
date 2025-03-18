import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IOrderItem } from '../order-item.model';

@Component({
  selector: 'jhi-order-item-detail',
  templateUrl: './order-item-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class OrderItemDetailComponent {
  orderItem = input<IOrderItem | null>(null);

  previousState(): void {
    window.history.back();
  }
}
