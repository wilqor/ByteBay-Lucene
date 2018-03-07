import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  searchString = "";

  @Output() searchEvent = new EventEmitter();


  constructor() { }


  search(searchString) {
    this.searchEvent.emit(searchString);
  }

  ngOnInit() {
  }

}
