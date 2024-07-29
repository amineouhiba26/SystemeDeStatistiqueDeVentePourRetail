import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoleDetailComponent } from './role-detail.component';

describe('Role Management Detail Component', () => {
  let comp: RoleDetailComponent;
  let fixture: ComponentFixture<RoleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RoleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ role: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(RoleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RoleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load role on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.role).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
