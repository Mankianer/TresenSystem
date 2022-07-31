import {Component, OnInit} from '@angular/core';
import {User, UserService} from "../services/user.service";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {

  displayedColumns: string[] = ['id', 'name', 'age', 'invoice'];
  dataSource: User[];

  constructor(public userService: UserService) {
    this.dataSource = this.userService.getUsers();
  }

  ngOnInit(): void {
  }

}
