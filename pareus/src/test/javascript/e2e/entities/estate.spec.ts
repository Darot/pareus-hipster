import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Estate e2e test', () => {

    let navBarPage: NavBarPage;
    let estateDialogPage: EstateDialogPage;
    let estateComponentsPage: EstateComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().loginWithOAuth('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Estates', () => {
        navBarPage.goToEntity('estate');
        estateComponentsPage = new EstateComponentsPage();
        expect(estateComponentsPage.getTitle())
            .toMatch(/pareusApp.estate.home.title/);

    });

    it('should load create Estate dialog', () => {
        estateComponentsPage.clickOnCreateButton();
        estateDialogPage = new EstateDialogPage();
        expect(estateDialogPage.getModalTitle())
            .toMatch(/pareusApp.estate.home.createOrEditLabel/);
        estateDialogPage.close();
    });

    it('should create and save Estates', () => {
        estateComponentsPage.clickOnCreateButton();
        estateDialogPage.setDescriptionInput('description');
        expect(estateDialogPage.getDescriptionInput()).toMatch('description');
        estateDialogPage.setPriceInput('5');
        expect(estateDialogPage.getPriceInput()).toMatch('5');
        estateDialogPage.addressSelectLastOption();
        estateDialogPage.claimedBySelectLastOption();
        estateDialogPage.watchlistSelectLastOption();
        estateDialogPage.save();
        expect(estateDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class EstateComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-estate div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class EstateDialogPage {
    modalTitle = element(by.css('h4#myEstateLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    descriptionInput = element(by.css('input#field_description'));
    priceInput = element(by.css('input#field_price'));
    addressSelect = element(by.css('select#field_address'));
    claimedBySelect = element(by.css('select#field_claimedBy'));
    watchlistSelect = element(by.css('select#field_watchlist'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setDescriptionInput = function(description) {
        this.descriptionInput.sendKeys(description);
    }

    getDescriptionInput = function() {
        return this.descriptionInput.getAttribute('value');
    }

    setPriceInput = function(price) {
        this.priceInput.sendKeys(price);
    }

    getPriceInput = function() {
        return this.priceInput.getAttribute('value');
    }

    addressSelectLastOption = function() {
        this.addressSelect.all(by.tagName('option')).last().click();
    }

    addressSelectOption = function(option) {
        this.addressSelect.sendKeys(option);
    }

    getAddressSelect = function() {
        return this.addressSelect;
    }

    getAddressSelectedOption = function() {
        return this.addressSelect.element(by.css('option:checked')).getText();
    }

    claimedBySelectLastOption = function() {
        this.claimedBySelect.all(by.tagName('option')).last().click();
    }

    claimedBySelectOption = function(option) {
        this.claimedBySelect.sendKeys(option);
    }

    getClaimedBySelect = function() {
        return this.claimedBySelect;
    }

    getClaimedBySelectedOption = function() {
        return this.claimedBySelect.element(by.css('option:checked')).getText();
    }

    watchlistSelectLastOption = function() {
        this.watchlistSelect.all(by.tagName('option')).last().click();
    }

    watchlistSelectOption = function(option) {
        this.watchlistSelect.sendKeys(option);
    }

    getWatchlistSelect = function() {
        return this.watchlistSelect;
    }

    getWatchlistSelectedOption = function() {
        return this.watchlistSelect.element(by.css('option:checked')).getText();
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
