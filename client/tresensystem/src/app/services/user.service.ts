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
      {id: 1, name: 'John Müller', age: 30, invoice: 10.0},
      {id: 2, name: 'Jane Test', age: 25, invoice: 22.0},
      {id: 3, name: 'Bob Extra', age: 20, invoice: 40.50},
      {id: 4, name: 'Max Mustermann', age: 35, invoice: 0.0},
      {id: 5, name: 'Sandra Musterfrau', age: 40, invoice: 0.0},
      {id: 6, name: 'Peter Pan', age: 50, invoice: 0.0},
      {id: 7, name: 'Paula Puff', age: 55, invoice: 0.0},
      {id: 8, name: 'Donald Duck', age: 60, invoice: 0.0},
      {id: 9, name: 'Hans Hund', age: 65, invoice: 0.0},
      {id: 10, name: 'Sara Schwan', age: 70, invoice: 0.0},
      {id: 11, name: 'Ursula Understudy', age: 75, invoice: 0.0},
      {id: 12, name: 'Peter Parker', age: 80, invoice: 0.0},
      {id: 13, name: 'Max Mustermann', age: 35, invoice: 39.0},
      {id: 14, name: 'Sandra Musterfrau', age: 40, invoice: 0.0},
      {id: 15, name: 'Peter Pan', age: 50, invoice: 20.0},
      {id: 16, name: 'Paula Puff', age: 55, invoice: 0.4},
      {id: 17, name: 'Donald Duck', age: 60, invoice: 0.0},
    ];
  }

}
