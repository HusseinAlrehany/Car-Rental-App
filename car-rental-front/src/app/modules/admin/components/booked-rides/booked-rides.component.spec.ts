import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookedRidesComponent } from './booked-rides.component';

describe('BookedRidesComponent', () => {
  let component: BookedRidesComponent;
  let fixture: ComponentFixture<BookedRidesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookedRidesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BookedRidesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
