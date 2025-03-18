import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../delivery-item.test-samples';

import { DeliveryItemFormService } from './delivery-item-form.service';

describe('DeliveryItem Form Service', () => {
  let service: DeliveryItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeliveryItemFormService);
  });

  describe('Service methods', () => {
    describe('createDeliveryItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDeliveryItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sku: expect.any(Object),
            description: expect.any(Object),
            quantity: expect.any(Object),
            weight: expect.any(Object),
            volume: expect.any(Object),
            delivery: expect.any(Object),
          }),
        );
      });

      it('passing IDeliveryItem should create a new form with FormGroup', () => {
        const formGroup = service.createDeliveryItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sku: expect.any(Object),
            description: expect.any(Object),
            quantity: expect.any(Object),
            weight: expect.any(Object),
            volume: expect.any(Object),
            delivery: expect.any(Object),
          }),
        );
      });
    });

    describe('getDeliveryItem', () => {
      it('should return NewDeliveryItem for default DeliveryItem initial value', () => {
        const formGroup = service.createDeliveryItemFormGroup(sampleWithNewData);

        const deliveryItem = service.getDeliveryItem(formGroup) as any;

        expect(deliveryItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewDeliveryItem for empty DeliveryItem initial value', () => {
        const formGroup = service.createDeliveryItemFormGroup();

        const deliveryItem = service.getDeliveryItem(formGroup) as any;

        expect(deliveryItem).toMatchObject({});
      });

      it('should return IDeliveryItem', () => {
        const formGroup = service.createDeliveryItemFormGroup(sampleWithRequiredData);

        const deliveryItem = service.getDeliveryItem(formGroup) as any;

        expect(deliveryItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDeliveryItem should not enable id FormControl', () => {
        const formGroup = service.createDeliveryItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDeliveryItem should disable id FormControl', () => {
        const formGroup = service.createDeliveryItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
