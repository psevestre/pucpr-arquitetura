import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOrderCustomer, NewOrderCustomer } from '../order-customer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrderCustomer for edit and NewOrderCustomerFormGroupInput for create.
 */
type OrderCustomerFormGroupInput = IOrderCustomer | PartialWithRequiredKeyOf<NewOrderCustomer>;

type OrderCustomerFormDefaults = Pick<NewOrderCustomer, 'id'>;

type OrderCustomerFormGroupContent = {
  id: FormControl<IOrderCustomer['id'] | NewOrderCustomer['id']>;
  customerId: FormControl<IOrderCustomer['customerId']>;
  name: FormControl<IOrderCustomer['name']>;
  email: FormControl<IOrderCustomer['email']>;
  addressLine1: FormControl<IOrderCustomer['addressLine1']>;
  addressLine2: FormControl<IOrderCustomer['addressLine2']>;
  zipCode: FormControl<IOrderCustomer['zipCode']>;
  city: FormControl<IOrderCustomer['city']>;
  country: FormControl<IOrderCustomer['country']>;
};

export type OrderCustomerFormGroup = FormGroup<OrderCustomerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrderCustomerFormService {
  createOrderCustomerFormGroup(orderCustomer: OrderCustomerFormGroupInput = { id: null }): OrderCustomerFormGroup {
    const orderCustomerRawValue = {
      ...this.getFormDefaults(),
      ...orderCustomer,
    };
    return new FormGroup<OrderCustomerFormGroupContent>({
      id: new FormControl(
        { value: orderCustomerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      customerId: new FormControl(orderCustomerRawValue.customerId, {
        validators: [Validators.required, Validators.maxLength(128)],
      }),
      name: new FormControl(orderCustomerRawValue.name, {
        validators: [Validators.required, Validators.maxLength(128)],
      }),
      email: new FormControl(orderCustomerRawValue.email, {
        validators: [Validators.required, Validators.maxLength(128)],
      }),
      addressLine1: new FormControl(orderCustomerRawValue.addressLine1, {
        validators: [Validators.required, Validators.maxLength(128)],
      }),
      addressLine2: new FormControl(orderCustomerRawValue.addressLine2, {
        validators: [Validators.required, Validators.maxLength(128)],
      }),
      zipCode: new FormControl(orderCustomerRawValue.zipCode, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      city: new FormControl(orderCustomerRawValue.city, {
        validators: [Validators.maxLength(128)],
      }),
      country: new FormControl(orderCustomerRawValue.country, {
        validators: [Validators.maxLength(64)],
      }),
    });
  }

  getOrderCustomer(form: OrderCustomerFormGroup): IOrderCustomer | NewOrderCustomer {
    return form.getRawValue() as IOrderCustomer | NewOrderCustomer;
  }

  resetForm(form: OrderCustomerFormGroup, orderCustomer: OrderCustomerFormGroupInput): void {
    const orderCustomerRawValue = { ...this.getFormDefaults(), ...orderCustomer };
    form.reset(
      {
        ...orderCustomerRawValue,
        id: { value: orderCustomerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrderCustomerFormDefaults {
    return {
      id: null,
    };
  }
}
