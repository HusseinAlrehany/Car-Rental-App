import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookingCarComponent } from './booking-car.component';

describe('BookingCarComponent', () => {
  let component: BookingCarComponent;
  let fixture: ComponentFixture<BookingCarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookingCarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BookingCarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
