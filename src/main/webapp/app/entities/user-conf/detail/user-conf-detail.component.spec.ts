import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UserConfDetailComponent } from './user-conf-detail.component';

describe('UserConf Management Detail Component', () => {
  let comp: UserConfDetailComponent;
  let fixture: ComponentFixture<UserConfDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserConfDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ userConf: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(UserConfDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UserConfDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load userConf on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.userConf).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
