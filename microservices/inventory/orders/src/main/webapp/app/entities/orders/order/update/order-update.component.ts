import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IOrderCustomer } from 'app/entities/orders/order-customer/order-customer.model';
import { OrderCustomerService } from 'app/entities/orders/order-customer/service/order-customer.service';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { OrderService } from '../service/order.service';
import { IOrder } from '../order.model';
import { OrderFormGroup, OrderFormService } from './order-form.service';

@Component({
  selector: 'jhi-order-update',
  templateUrl: './order-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrderUpdateComponent implements OnInit {
  isSaving = false;
  order: IOrder | null = null;
  orderStatusValues = Object.keys(OrderStatus);

  orderCustomersSharedCollection: IOrderCustomer[] = [];

  protected orderService = inject(OrderService);
  protected orderFormService = inject(OrderFormService);
  protected orderCustomerService = inject(OrderCustomerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrderFormGroup = this.orderFormService.createOrderFormGroup();

  compareOrderCustomer = (o1: IOrderCustomer | null, o2: IOrderCustomer | null): boolean =>
    this.orderCustomerService.compareOrderCustomer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ order }) => {
      this.order = order;
      if (order) {
        this.updateForm(order);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const order = this.orderFormService.getOrder(this.editForm);
    if (order.id !== null) {
      this.subscribeToSaveResponse(this.orderService.update(order));
    } else {
      this.subscribeToSaveResponse(this.orderService.create(order));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrder>>): void {
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

  protected updateForm(order: IOrder): void {
    this.order = order;
    this.orderFormService.resetForm(this.editForm, order);

    this.orderCustomersSharedCollection = this.orderCustomerService.addOrderCustomerToCollectionIfMissing<IOrderCustomer>(
      this.orderCustomersSharedCollection,
      order.customer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderCustomerService
      .query()
      .pipe(map((res: HttpResponse<IOrderCustomer[]>) => res.body ?? []))
      .pipe(
        map((orderCustomers: IOrderCustomer[]) =>
          this.orderCustomerService.addOrderCustomerToCollectionIfMissing<IOrderCustomer>(orderCustomers, this.order?.customer),
        ),
      )
      .subscribe((orderCustomers: IOrderCustomer[]) => (this.orderCustomersSharedCollection = orderCustomers));
  }
}
