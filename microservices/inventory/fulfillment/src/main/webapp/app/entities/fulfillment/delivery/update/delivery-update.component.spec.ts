import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { DeliveryService } from '../service/delivery.service';
import { IDelivery } from '../delivery.model';
import { DeliveryFormService } from './delivery-form.service';

import { DeliveryUpdateComponent } from './delivery-update.component';

describe('Delivery Management Update Component', () => {
  let comp: DeliveryUpdateComponent;
  let fixture: ComponentFixture<DeliveryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deliveryFormService: DeliveryFormService;
  let deliveryService: DeliveryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DeliveryUpdateComponent],
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
      .overrideTemplate(DeliveryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeliveryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deliveryFormService = TestBed.inject(DeliveryFormService);
    deliveryService = TestBed.inject(DeliveryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const delivery: IDelivery = { id: 9797 };

      activatedRoute.data = of({ delivery });
      comp.ngOnInit();

      expect(comp.delivery).toEqual(delivery);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDelivery>>();
      const delivery = { id: 16325 };
      jest.spyOn(deliveryFormService, 'getDelivery').mockReturnValue(delivery);
      jest.spyOn(deliveryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ delivery });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: delivery }));
      saveSubject.complete();

      // THEN
      expect(deliveryFormService.getDelivery).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(deliveryService.update).toHaveBeenCalledWith(expect.objectContaining(delivery));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDelivery>>();
      const delivery = { id: 16325 };
      jest.spyOn(deliveryFormService, 'getDelivery').mockReturnValue({ id: null });
      jest.spyOn(deliveryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ delivery: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: delivery }));
      saveSubject.complete();

      // THEN
      expect(deliveryFormService.getDelivery).toHaveBeenCalled();
      expect(deliveryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDelivery>>();
      const delivery = { id: 16325 };
      jest.spyOn(deliveryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ delivery });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deliveryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
