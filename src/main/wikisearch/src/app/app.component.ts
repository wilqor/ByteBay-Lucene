import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Results} from "./results/results.component";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  results: Results;

  constructor(private http: HttpClient) {
  }



  searchEvent($event) {
    this.http.get("http://localhost:8080/search?searchString=" + $event).subscribe(data => {
      this.results = Object.assign(new Results(), data);
    })
  }



}
