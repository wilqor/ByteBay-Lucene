import {Component, Input, OnInit} from '@angular/core';


export class Result {

  title: string;
  link: string;
  description: string;

}

@Component({
  selector: 'app-result-entry',
  templateUrl: './result-entry.component.html',
  styleUrls: ['./result-entry.component.css']
})
export class ResultEntryComponent implements OnInit {

  @Input()
  result: Result;


  constructor() { }

  ngOnInit() {
  }

}
