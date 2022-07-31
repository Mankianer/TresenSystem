import {Injectable} from '@angular/core';
import {User} from "./user.service";
import {Product} from "../userlist/user-list.component";
import {ErrorMessageService} from "./error-message.service";

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private messageService: ErrorMessageService) { }

  sendOrder(user: User,order: Product[]): boolean {
    this.messageService.simpleMessage('Bestellung', 'Bestellung wurde aufgenommen');
    return true;
  }
}
