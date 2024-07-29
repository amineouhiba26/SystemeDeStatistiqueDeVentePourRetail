import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PermissionDetailComponent } from './permission-detail.component';

describe('Permission Management Detail Component', () => {
  let comp: PermissionDetailComponent;
  let fixture: ComponentFixture<PermissionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PermissionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ permission: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(PermissionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PermissionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load permission on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.permission).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
