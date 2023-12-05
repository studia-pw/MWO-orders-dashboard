import {Component, inject, TemplateRef} from '@angular/core';
import {ModalDismissReasons, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [
    NavbarComponent
  ],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent {
  private modalService = inject(NgbModal);

  open(content: TemplateRef<any>) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'});
  }
}

