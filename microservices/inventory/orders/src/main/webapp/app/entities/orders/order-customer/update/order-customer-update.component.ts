import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOrderCustomer } from '../order-customer.model';
import { OrderCustomerService } from '../service/order-customer.service';
import { OrderCustomerFormGroup, OrderCustomerFormService } from './order-customer-form.service';

@Component({
  selector: 'jhi-order-customer-update',
  templateUrl: './order-customer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrderCustomerUpdateComponent implements OnInit {
  isSaving = false;
  orderCustomer: IOrderCustomer | null = null;

  protected orderCustomerService = inject(OrderCustomerService);
  protected orderCustomerFormService = inject(OrderCustomerFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderCustomerFormGroup = this.orderCustomerFormService.createOrderCustomerFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderCustomer }) => {
      this.orderCustomer = orderCustomer;
      if (orderCustomer) {
        this.updateForm(orderCustomer);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orderCustomer = this.orderCustomerFormService.getOrderCustomer(this.editForm);
    if (orderCustomer.id !== null) {
      this.subscribeToSaveResponse(this.orderCustomerService.update(orderCustomer));
    } else {
      this.subscribeToSaveResponse(this.orderCustomerService.create(orderCustomer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderCustomer>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(orderCustomer: IOrderCustomer): void {
    this.orderCustomer = orderCustomer;
    this.orderCustomerFormService.resetForm(this.editForm, orderCustomer);
  }
}
