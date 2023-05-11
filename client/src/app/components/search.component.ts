import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  
  form!:FormGroup
  charName?:string
  constructor(private fb:FormBuilder, private router:Router){ }

  ngOnInit(): void {
      this.form = this.createForm();
  }

  createForm():FormGroup {
    return this.fb.group({
      charName:this.fb.control('')
    })
  }

  search(){
    const charName = this.form?.value['charName']
    this.router.navigate(['/list',charName])
  }
}
