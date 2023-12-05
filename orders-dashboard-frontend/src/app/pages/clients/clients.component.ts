import {Component, inject, TemplateRef} from '@angular/core';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";

@Component({
  selector: 'app-clients',
  standalone: true,
  imports: [
    NavbarComponent
  ],
  templateUrl: './clients.component.html',
  styleUrl: './clients.component.css'
})
export class ClientsComponent {
  private modalService = inject(NgbModal);

  constructor() {
  }

  open(content: TemplateRef<any>) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'});
  }

  createNewClient() {
  }

}
