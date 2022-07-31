import {Component, Inject, OnInit} from '@angular/core';
import {User, UserService} from "../services/user.service";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {FormControl} from "@angular/forms";
import {Observable, startWith} from "rxjs";
import {map} from "rxjs/operators";
import {OrderService} from "../services/order.service";
import {ErrorMessageService} from "../services/error-message.service";

export interface Product {
  name: string;
  amount: number | string;
}

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {

  displayedColumns: string[] = ['id', 'name', 'age', 'invoice', 'order'];
  dataSource: User[];

  constructor(public dialog: MatDialog, public userService: UserService, public orderService: OrderService, public errorMessageService: ErrorMessageService) {
    this.dataSource = this.userService.getUsers();
  }

  ngOnInit(): void {
  }

  public createOrder(user: User | null) {
    const data = {user: user, order: this.getEmptyOrder()};
    const dialogRef = this.dialog.open(UserListOrderDialog, {
      width: '50%',
      data: data,
      disableClose: true,
      autoFocus: 'first-header',
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result == 'success') {
        if (data.user) {
          this.orderService.sendOrder(data.user, data.order);
        } else {
          this.errorMessageService.importantMessage('Fehler', 'Bestellung konnte nicht aufgenommen werden!\nBitte wähle einen Benutzer aus').afterClosed().subscribe(() => {
            this.createOrder(data.user);
          });
        }
      }
    });
  }

  getEmptyOrder(): Product[] {
    return [{name: 'Bier', amount: 0}, {name: 'Schneeflittchen', amount: 0}, {name: 'Cola', amount: 0}, {
      name: 'Sprite',
      amount: 0
    }];
  }
}

@Component({
  selector: 'app-user-list-order',
  templateUrl: './user-list-order.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListOrderDialog implements OnInit {
  orderControl = new FormControl();
  options: User[] = this.userService.getUsers();
  filteredOptions!: Observable<User[]>;
  order: Product[] = this.data.order;

  constructor(public dialogRef: MatDialogRef<UserListOrderDialog>,
              @Inject(MAT_DIALOG_DATA) public data: { user: User | null, order: Product[] },
              public userService: UserService) {
    if (this.data.user) this.orderControl.setValue(this.data.user.name);
    this.orderControl.valueChanges.subscribe(value => {
      this.data.user = this.options.find((option) => value === option.name) || null;
    });
  }

  private _filter(value: string): User[] {
    const filterValue = value.toLowerCase();
    return this.options.filter(option => option.name.toLowerCase().includes(filterValue));
  }

  ngOnInit(): void {
    this.filteredOptions = this.orderControl.valueChanges
      .pipe(startWith(''), map(value => this._filter(value || '')));
  }

  addAmount(product: { name: string, amount: any }) {
    product.amount++;
  }

  subAmount(product: { name: string, amount: any }) {
    if (product.amount > 0) product.amount--;
  }

  isValid() {
    return this.options.find((value) => value.id === this.data.user?.id) && this.order.filter(product => product.amount > 0).length > 0;
  }
}
