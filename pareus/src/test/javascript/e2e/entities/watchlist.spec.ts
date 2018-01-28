import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Watchlist e2e test', () => {

    let navBarPage: NavBarPage;
    let watchlistDialogPage: WatchlistDialogPage;
    let watchlistComponentsPage: WatchlistComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().loginWithOAuth('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Watchlists', () => {
        navBarPage.goToEntity('watchlist');
        watchlistComponentsPage = new WatchlistComponentsPage();
        expect(watchlistComponentsPage.getTitle())
            .toMatch(/pareusApp.watchlist.home.title/);

    });

    it('should load create Watchlist dialog', () => {
        watchlistComponentsPage.clickOnCreateButton();
        watchlistDialogPage = new WatchlistDialogPage();
        expect(watchlistDialogPage.getModalTitle())
            .toMatch(/pareusApp.watchlist.home.createOrEditLabel/);
        watchlistDialogPage.close();
    });

    it('should create and save Watchlists', () => {
        watchlistComponentsPage.clickOnCreateButton();
        watchlistDialogPage.save();
        expect(watchlistDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class WatchlistComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-watchlist div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class WatchlistDialogPage {
    modalTitle = element(by.css('h4#myWatchlistLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
