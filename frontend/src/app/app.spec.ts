import { TestBed } from '@angular/core/testing';
import { App } from './app';

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
    })
      .compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should render the dashboard heading', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Deine Musik. Auf einen Blick.');
  });

  it('should compose the dashboard from focused components', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.querySelector('app-sidebar')).not.toBeNull();
    expect(compiled.querySelector('app-page-heading')).not.toBeNull();
    expect(compiled.querySelector('app-overview-page')).not.toBeNull();
  });

  it('should navigate to the track library', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    const tracksButton = [...compiled.querySelectorAll('app-sidebar button')]
      .find((button) => button.textContent?.includes('Tracks'));

    tracksButton?.dispatchEvent(new Event('click'));
    fixture.detectChanges();

    expect(compiled.querySelector('h1')?.textContent).toContain('Alle Tracks');
  });

  it('should filter the overview tracks by the search term', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    const search = compiled.querySelector<HTMLInputElement>('input[type="search"]');

    if (!search) throw new Error('Search input was not rendered');
    search.value = 'Dreams';
    search.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    expect(compiled.textContent).toContain('Dreams');
    expect(compiled.textContent).not.toContain('Midnight City');
  });

  it('should keep collection totals unchanged while filtering', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    const search = compiled.querySelector<HTMLInputElement>('input[type="search"]');

    if (!search) throw new Error('Search input was not rendered');
    search.value = 'Dreams';
    search.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    const totals = [...compiled.querySelectorAll('app-overview-page .stat-card strong')]
      .map((element) => element.textContent?.trim());
    expect(totals).toEqual(['5', '3', '3']);
  });
});
