import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription as RxSubscription} from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Image } from './image.model';
import { ImageService } from './image.service';

@Component({
    selector: 'jhi-image-detail',
    templateUrl: './image-detail.component.html'
})
export class ImageDetailComponent implements OnInit, OnDestroy {

    image: Image;
    private rxSubscription: any;
    private eventSubscriber: RxSubscription;

    constructor(
        private eventManager: JhiEventManager,
        private imageService: ImageService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.rxSubscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInImages();
    }

    load(id) {
        this.imageService.find(id).subscribe((image) => {
            this.image = image;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.rxSubscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInImages() {
        this.eventSubscriber = this.eventManager.subscribe(
            'imageListModification',
            (response) => this.load(this.image.id)
        );
    }
}
