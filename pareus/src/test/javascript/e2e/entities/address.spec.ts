import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Address e2e test', () => {

    let navBarPage: NavBarPage;
    let addressDialogPage: AddressDialogPage;
    let addressComponentsPage: AddressComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().loginWithOAuth('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Addresses', () => {
        navBarPage.goToEntity('address');
        addressComponentsPage = new AddressComponentsPage();
        expect(addressComponentsPage.getTitle())
            .toMatch(/pareusApp.address.home.title/);

    });

    it('should load create Address dialog', () => {
        addressComponentsPage.clickOnCreateButton();
        addressDialogPage = new AddressDialogPage();
        expect(addressDialogPage.getModalTitle())
            .toMatch(/pareusApp.address.home.createOrEditLabel/);
        addressDialogPage.close();
    });

    it('should create and save Addresses', () => {
        addressComponentsPage.clickOnCreateButton();
        addressDialogPage.setStreetInput('street');
        expect(addressDialogPage.getStreetInput()).toMatch('street');
        addressDialogPage.setHouseNumberInput('houseNumber');
        expect(addressDialogPage.getHouseNumberInput()).toMatch('houseNumber');
        addressDialogPage.setZipCodeInput('zipCode');
        expect(addressDialogPage.getZipCodeInput()).toMatch('zipCode');
        addressDialogPage.setCityInput('city');
        expect(addressDialogPage.getCityInput()).toMatch('city');
        addressDialogPage.save();
        expect(addressDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class AddressComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-address div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class AddressDialogPage {
    modalTitle = element(by.css('h4#myAddressLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    streetInput = element(by.css('input#field_street'));
    houseNumberInput = element(by.css('input#field_houseNumber'));
    zipCodeInput = element(by.css('input#field_zipCode'));
    cityInput = element(by.css('input#field_city'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setStreetInput = function(street) {
        this.streetInput.sendKeys(street);
    }

    getStreetInput = function() {
        return this.streetInput.getAttribute('value');
    }

    setHouseNumberInput = function(houseNumber) {
        this.houseNumberInput.sendKeys(houseNumber);
    }

    getHouseNumberInput = function() {
        return this.houseNumberInput.getAttribute('value');
    }

    setZipCodeInput = function(zipCode) {
        this.zipCodeInput.sendKeys(zipCode);
    }

    getZipCodeInput = function() {
        return this.zipCodeInput.getAttribute('value');
    }

    setCityInput = function(city) {
        this.cityInput.sendKeys(city);
    }

    getCityInput = function() {
        return this.cityInput.getAttribute('value');
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
