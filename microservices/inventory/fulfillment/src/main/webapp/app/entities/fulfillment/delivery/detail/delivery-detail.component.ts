import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IDelivery } from '../delivery.model';

@Component({
  selector: 'jhi-delivery-detail',
  templateUrl: './delivery-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class DeliveryDetailComponent {
  delivery = input<IDelivery | null>(null);

  previousState(): void {
    window.history.back();
  }
}
