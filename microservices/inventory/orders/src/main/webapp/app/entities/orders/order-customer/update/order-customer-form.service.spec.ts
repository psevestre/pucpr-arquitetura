import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../order-customer.test-samples';

import { OrderCustomerFormService } from './order-customer-form.service';

describe('OrderCustomer Form Service', () => {
  let service: OrderCustomerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrderCustomerFormService);
  });

  describe('Service methods', () => {
    describe('createOrderCustomerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrderCustomerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            customerId: expect.any(Object),
            name: expect.any(Object),
            email: expect.any(Object),
            addressLine1: expect.any(Object),
            addressLine2: expect.any(Object),
            zipCode: expect.any(Object),
            city: expect.any(Object),
            country: expect.any(Object),
          }),
        );
      });

      it('passing IOrderCustomer should create a new form with FormGroup', () => {
        const formGroup = service.createOrderCustomerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            customerId: expect.any(Object),
            name: expect.any(Object),
            email: expect.any(Object),
            addressLine1: expect.any(Object),
            addressLine2: expect.any(Object),
            zipCode: expect.any(Object),
            city: expect.any(Object),
            country: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrderCustomer', () => {
      it('should return NewOrderCustomer for default OrderCustomer initial value', () => {
        const formGroup = service.createOrderCustomerFormGroup(sampleWithNewData);

        const orderCustomer = service.getOrderCustomer(formGroup) as any;

        expect(orderCustomer).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrderCustomer for empty OrderCustomer initial value', () => {
        const formGroup = service.createOrderCustomerFormGroup();

        const orderCustomer = service.getOrderCustomer(formGroup) as any;

        expect(orderCustomer).toMatchObject({});
      });

      it('should return IOrderCustomer', () => {
        const formGroup = service.createOrderCustomerFormGroup(sampleWithRequiredData);

        const orderCustomer = service.getOrderCustomer(formGroup) as any;

        expect(orderCustomer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrderCustomer should not enable id FormControl', () => {
        const formGroup = service.createOrderCustomerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrderCustomer should disable id FormControl', () => {
        const formGroup = service.createOrderCustomerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
