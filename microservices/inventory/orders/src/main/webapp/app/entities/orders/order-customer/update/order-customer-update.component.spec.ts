import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { OrderCustomerService } from '../service/order-customer.service';
import { IOrderCustomer } from '../order-customer.model';
import { OrderCustomerFormService } from './order-customer-form.service';

import { OrderCustomerUpdateComponent } from './order-customer-update.component';

describe('OrderCustomer Management Update Component', () => {
  let comp: OrderCustomerUpdateComponent;
  let fixture: ComponentFixture<OrderCustomerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderCustomerFormService: OrderCustomerFormService;
  let orderCustomerService: OrderCustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OrderCustomerUpdateComponent],
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
      .overrideTemplate(OrderCustomerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderCustomerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderCustomerFormService = TestBed.inject(OrderCustomerFormService);
    orderCustomerService = TestBed.inject(OrderCustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const orderCustomer: IOrderCustomer = { id: 4148 };

      activatedRoute.data = of({ orderCustomer });
      comp.ngOnInit();

      expect(comp.orderCustomer).toEqual(orderCustomer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderCustomer>>();
      const orderCustomer = { id: 18682 };
      jest.spyOn(orderCustomerFormService, 'getOrderCustomer').mockReturnValue(orderCustomer);
      jest.spyOn(orderCustomerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderCustomer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderCustomer }));
      saveSubject.complete();

      // THEN
      expect(orderCustomerFormService.getOrderCustomer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderCustomerService.update).toHaveBeenCalledWith(expect.objectContaining(orderCustomer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderCustomer>>();
      const orderCustomer = { id: 18682 };
      jest.spyOn(orderCustomerFormService, 'getOrderCustomer').mockReturnValue({ id: null });
      jest.spyOn(orderCustomerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderCustomer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderCustomer }));
      saveSubject.complete();

      // THEN
      expect(orderCustomerFormService.getOrderCustomer).toHaveBeenCalled();
      expect(orderCustomerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderCustomer>>();
      const orderCustomer = { id: 18682 };
      jest.spyOn(orderCustomerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderCustomer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderCustomerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
