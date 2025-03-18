import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOrderCustomer } from '../order-customer.model';
import { OrderCustomerService } from '../service/order-customer.service';

@Component({
  templateUrl: './order-customer-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OrderCustomerDeleteDialogComponent {
  orderCustomer?: IOrderCustomer;

  protected orderCustomerService = inject(OrderCustomerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orderCustomerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
