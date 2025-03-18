import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDelivery } from 'app/entities/fulfillment/delivery/delivery.model';
import { DeliveryService } from 'app/entities/fulfillment/delivery/service/delivery.service';
import { DeliveryItemService } from '../service/delivery-item.service';
import { IDeliveryItem } from '../delivery-item.model';
import { DeliveryItemFormService } from './delivery-item-form.service';

import { DeliveryItemUpdateComponent } from './delivery-item-update.component';

describe('DeliveryItem Management Update Component', () => {
  let comp: DeliveryItemUpdateComponent;
  let fixture: ComponentFixture<DeliveryItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deliveryItemFormService: DeliveryItemFormService;
  let deliveryItemService: DeliveryItemService;
  let deliveryService: DeliveryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DeliveryItemUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DeliveryItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeliveryItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deliveryItemFormService = TestBed.inject(DeliveryItemFormService);
    deliveryItemService = TestBed.inject(DeliveryItemService);
    deliveryService = TestBed.inject(DeliveryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Delivery query and add missing value', () => {
      const deliveryItem: IDeliveryItem = { id: 12510 };
      const delivery: IDelivery = { id: 16325 };
      deliveryItem.delivery = delivery;

      const deliveryCollection: IDelivery[] = [{ id: 16325 }];
      jest.spyOn(deliveryService, 'query').mockReturnValue(of(new HttpResponse({ body: deliveryCollection })));
      const additionalDeliveries = [delivery];
      const expectedCollection: IDelivery[] = [...additionalDeliveries, ...deliveryCollection];
      jest.spyOn(deliveryService, 'addDeliveryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deliveryItem });
      comp.ngOnInit();

      expect(deliveryService.query).toHaveBeenCalled();
      expect(deliveryService.addDeliveryToCollectionIfMissing).toHaveBeenCalledWith(
        deliveryCollection,
        ...additionalDeliveries.map(expect.objectContaining),
      );
      expect(comp.deliveriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const deliveryItem: IDeliveryItem = { id: 12510 };
      const delivery: IDelivery = { id: 16325 };
      deliveryItem.delivery = delivery;

      activatedRoute.data = of({ deliveryItem });
      comp.ngOnInit();

      expect(comp.deliveriesSharedCollection).toContainEqual(delivery);
      expect(comp.deliveryItem).toEqual(deliveryItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeliveryItem>>();
      const deliveryItem = { id: 16562 };
      jest.spyOn(deliveryItemFormService, 'getDeliveryItem').mockReturnValue(deliveryItem);
      jest.spyOn(deliveryItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deliveryItem }));
      saveSubject.complete();

      // THEN
      expect(deliveryItemFormService.getDeliveryItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(deliveryItemService.update).toHaveBeenCalledWith(expect.objectContaining(deliveryItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeliveryItem>>();
      const deliveryItem = { id: 16562 };
      jest.spyOn(deliveryItemFormService, 'getDeliveryItem').mockReturnValue({ id: null });
      jest.spyOn(deliveryItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deliveryItem }));
      saveSubject.complete();

      // THEN
      expect(deliveryItemFormService.getDeliveryItem).toHaveBeenCalled();
      expect(deliveryItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDeliveryItem>>();
      const deliveryItem = { id: 16562 };
      jest.spyOn(deliveryItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deliveryItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDelivery', () => {
      it('Should forward to deliveryService', () => {
        const entity = { id: 16325 };
        const entity2 = { id: 9797 };
        jest.spyOn(deliveryService, 'compareDelivery');
        comp.compareDelivery(entity, entity2);
        expect(deliveryService.compareDelivery).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
