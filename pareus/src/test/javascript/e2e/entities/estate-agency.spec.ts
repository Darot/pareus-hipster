import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('EstateAgency e2e test', () => {

    let navBarPage: NavBarPage;
    let estateAgencyDialogPage: EstateAgencyDialogPage;
    let estateAgencyComponentsPage: EstateAgencyComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().loginWithOAuth('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load EstateAgencies', () => {
        navBarPage.goToEntity('estate-agency');
        estateAgencyComponentsPage = new EstateAgencyComponentsPage();
        expect(estateAgencyComponentsPage.getTitle())
            .toMatch(/pareusApp.estateAgency.home.title/);

    });

    it('should load create EstateAgency dialog', () => {
        estateAgencyComponentsPage.clickOnCreateButton();
        estateAgencyDialogPage = new EstateAgencyDialogPage();
        expect(estateAgencyDialogPage.getModalTitle())
            .toMatch(/pareusApp.estateAgency.home.createOrEditLabel/);
        estateAgencyDialogPage.close();
    });

    it('should create and save EstateAgencies', () => {
        estateAgencyComponentsPage.clickOnCreateButton();
        estateAgencyDialogPage.setNameInput('name');
        expect(estateAgencyDialogPage.getNameInput()).toMatch('name');
        estateAgencyDialogPage.addressSelectLastOption();
        estateAgencyDialogPage.save();
        expect(estateAgencyDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class EstateAgencyComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-estate-agency div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class EstateAgencyDialogPage {
    modalTitle = element(by.css('h4#myEstateAgencyLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    addressSelect = element(by.css('select#field_address'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNameInput = function(name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function() {
        return this.nameInput.getAttribute('value');
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
