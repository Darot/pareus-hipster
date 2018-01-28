import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Image e2e test', () => {

    let navBarPage: NavBarPage;
    let imageDialogPage: ImageDialogPage;
    let imageComponentsPage: ImageComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().loginWithOAuth('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Images', () => {
        navBarPage.goToEntity('image');
        imageComponentsPage = new ImageComponentsPage();
        expect(imageComponentsPage.getTitle())
            .toMatch(/pareusApp.image.home.title/);

    });

    it('should load create Image dialog', () => {
        imageComponentsPage.clickOnCreateButton();
        imageDialogPage = new ImageDialogPage();
        expect(imageDialogPage.getModalTitle())
            .toMatch(/pareusApp.image.home.createOrEditLabel/);
        imageDialogPage.close();
    });

    it('should create and save Images', () => {
        imageComponentsPage.clickOnCreateButton();
        imageDialogPage.setPathInput('path');
        expect(imageDialogPage.getPathInput()).toMatch('path');
        imageDialogPage.setDescriptionInput('description');
        expect(imageDialogPage.getDescriptionInput()).toMatch('description');
        imageDialogPage.estateSelectLastOption();
        imageDialogPage.save();
        expect(imageDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ImageComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-image div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ImageDialogPage {
    modalTitle = element(by.css('h4#myImageLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    pathInput = element(by.css('input#field_path'));
    descriptionInput = element(by.css('input#field_description'));
    estateSelect = element(by.css('select#field_estate'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setPathInput = function(path) {
        this.pathInput.sendKeys(path);
    }

    getPathInput = function() {
        return this.pathInput.getAttribute('value');
    }

    setDescriptionInput = function(description) {
        this.descriptionInput.sendKeys(description);
    }

    getDescriptionInput = function() {
        return this.descriptionInput.getAttribute('value');
    }

    estateSelectLastOption = function() {
        this.estateSelect.all(by.tagName('option')).last().click();
    }

    estateSelectOption = function(option) {
        this.estateSelect.sendKeys(option);
    }

    getEstateSelect = function() {
        return this.estateSelect;
    }

    getEstateSelectedOption = function() {
        return this.estateSelect.element(by.css('option:checked')).getText();
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
