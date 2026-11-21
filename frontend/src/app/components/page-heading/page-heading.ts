import { Component, input, output } from '@angular/core';

@Component({
  selector: 'app-page-heading',
  templateUrl: './page-heading.html',
  styleUrl: './page-heading.css',
})
export class PageHeading {
  readonly title = input.required<string>();
  readonly description = input.required<string>();
  readonly searchTerm = input.required<string>();
  readonly searchChange = output<string>();

  protected updateSearch(event: Event): void {
    this.searchChange.emit((event.target as HTMLInputElement).value);
  }
}
