import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IOrderCustomer } from 'app/entities/orders/order-customer/order-customer.model';
import { OrderCustomerService } from 'app/entities/orders/order-customer/service/order-customer.service';
import { OrderService } from '../service/order.service';
import { IOrder } from '../order.model';
import { OrderFormService } from './order-form.service';

import { OrderUpdateComponent } from './order-update.component';

describe('Order Management Update Component', () => {
  let comp: OrderUpdateComponent;
  let fixture: ComponentFixture<OrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderFormService: OrderFormService;
  let orderService: OrderService;
  let orderCustomerService: OrderCustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OrderUpdateComponent],
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
      .overrideTemplate(OrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderFormService = TestBed.inject(OrderFormService);
    orderService = TestBed.inject(OrderService);
    orderCustomerService = TestBed.inject(OrderCustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call OrderCustomer query and add missing value', () => {
      const order: IOrder = { id: 4253 };
      const customer: IOrderCustomer = { id: 18682 };
      order.customer = customer;

      const orderCustomerCollection: IOrderCustomer[] = [{ id: 18682 }];
      jest.spyOn(orderCustomerService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCustomerCollection })));
      const additionalOrderCustomers = [customer];
      const expectedCollection: IOrderCustomer[] = [...additionalOrderCustomers, ...orderCustomerCollection];
      jest.spyOn(orderCustomerService, 'addOrderCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(orderCustomerService.query).toHaveBeenCalled();
      expect(orderCustomerService.addOrderCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        orderCustomerCollection,
        ...additionalOrderCustomers.map(expect.objectContaining),
      );
      expect(comp.orderCustomersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const order: IOrder = { id: 4253 };
      const customer: IOrderCustomer = { id: 18682 };
      order.customer = customer;

      activatedRoute.data = of({ order });
      comp.ngOnInit();

      expect(comp.orderCustomersSharedCollection).toContainEqual(customer);
      expect(comp.order).toEqual(order);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 14644 };
      jest.spyOn(orderFormService, 'getOrder').mockReturnValue(order);
      jest.spyOn(orderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: order }));
      saveSubject.complete();

      // THEN
      expect(orderFormService.getOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderService.update).toHaveBeenCalledWith(expect.objectContaining(order));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 14644 };
      jest.spyOn(orderFormService, 'getOrder').mockReturnValue({ id: null });
      jest.spyOn(orderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: order }));
      saveSubject.complete();

      // THEN
      expect(orderFormService.getOrder).toHaveBeenCalled();
      expect(orderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrder>>();
      const order = { id: 14644 };
      jest.spyOn(orderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ order });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOrderCustomer', () => {
      it('Should forward to orderCustomerService', () => {
        const entity = { id: 18682 };
        const entity2 = { id: 4148 };
        jest.spyOn(orderCustomerService, 'compareOrderCustomer');
        comp.compareOrderCustomer(entity, entity2);
        expect(orderCustomerService.compareOrderCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
