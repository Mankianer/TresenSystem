import {Injectable} from '@angular/core';

export interface User {
  id: number;
  name: string;
  age: number;
  invoice: number;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor() { }

  getUsers(): User[] {
    return [
      {id: 1, name: 'John', age: 30, invoice: 100},
      {id: 2, name: 'Jane', age: 25, invoice: 200},
      {id: 3, name: 'Bob', age: 20, invoice: 300},
    ];
  }

}
