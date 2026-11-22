import { Component, input, output } from '@angular/core';
import { View } from '../../models/music.models';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
  readonly activeView = input.required<View>();
  readonly viewChange = output<View>();
}
