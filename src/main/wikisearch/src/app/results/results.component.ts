import {Component, Input, OnInit} from '@angular/core';
import {Result} from "../result-entry/result-entry.component";


export class Results {
  searchResults: Result[];
  executionTimeMs = 0;
}

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit {

  _model: Results;

  constructor() { }

  ngOnInit() {
  }


  @Input()
  set model(model: Results) {
      this._model = model;
  }
}
