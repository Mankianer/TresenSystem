import {Injectable} from '@angular/core';
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {ErrorDialogComponent} from "../error-dialog/error-dialog.component";

@Injectable({
  providedIn: 'root'
})
export class ErrorMessageService {

  constructor(public dialog: MatDialog) { }

  public importantMessage(title: string, message: string): MatDialogRef<ErrorDialogComponent> {
    console.error(title, message);
    return this.dialog.open(ErrorDialogComponent, {
      width: '50%',
      data: {title: title, message: message},
      disableClose: true,
      autoFocus: 'first-header',
    });
  }

  public simpleMessage(title: string, message: string): MatDialogRef<ErrorDialogComponent> {
    console.error(title, message);
    return this.dialog.open(ErrorDialogComponent, {
      width: '50%',
      data: {title: title, message: message},
      autoFocus: 'first-header',
    });
  }

}
