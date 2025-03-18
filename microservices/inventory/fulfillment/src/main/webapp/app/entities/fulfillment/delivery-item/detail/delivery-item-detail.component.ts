import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDeliveryItem } from '../delivery-item.model';

@Component({
  selector: 'jhi-delivery-item-detail',
  templateUrl: './delivery-item-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class DeliveryItemDetailComponent {
  deliveryItem = input<IDeliveryItem | null>(null);

  previousState(): void {
    window.history.back();
  }
}
