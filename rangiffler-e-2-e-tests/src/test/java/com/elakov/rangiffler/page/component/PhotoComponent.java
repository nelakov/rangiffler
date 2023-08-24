package com.elakov.rangiffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.elakov.rangiffler.helper.allure.AllureSoftStepsHelper;
import com.elakov.rangiffler.page.BaseComponent;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;

public class PhotoComponent extends BaseComponent<PhotoComponent> {

    public PhotoComponent() {
        super($(".MuiPaper-root.MuiCard-root"));
    }

    // Add new photo mode
    private final SelenideElement templatePhotoImg = self.$("[alt='New image']");
    private final SelenideElement addPhotoBtn = self.$(By.id("file"));
    private final SelenideElement countryDropDown = self.$("[data-testid='ArrowDropDownIcon']");
    private final ElementsCollection countriesList = $$("ul.MuiMenu-list li");
    private final SelenideElement descriptionInput = self.$(By.id(":r17:"));

    // Save / Delete
    private final SelenideElement submitBtn = self.$(" [type='submit']");

    // Edit photo mode
    private final SelenideElement editPhotoBtn = self.$("button [data-testid='EditIcon']");
    private final SelenideElement deletePhotoBtn = self.$("button [data-testid='DeleteOutlineIcon']");
    private final SelenideElement closeEditPhotoViewBtn = self.$("button [data-testid='CloseIcon']");
    private final SelenideElement photoPlace = self.$("[data-testid='PlaceIcon']");
    private final SelenideElement photoDescription = self.$("p.MuiTypography-body2");

    @Override
    public PhotoComponent checkThatComponentDisplayed() {
        AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        return step("Check the 'Photo component' was loaded", () -> {

            softstep.add("Default photo is visible", () -> defaultImageShouldBeVisible());
            softstep.add("First name input is visible", () -> countryDropDown.should(visible));
            softstep.add("Last name input is visible", () -> descriptionInput.should(visible));

            softstep.execute();
            return this;
        });
    }

    //region Add photo
    @Step("Tap on the 'Add photo' button")
    public PhotoComponent addNewPhoto(String newPhotoPath) {
        addPhotoBtn.uploadFromClasspath(newPhotoPath);
        return this;
    }

    @Step("Tap on the 'Save' button")
    public void savePhoto() {
        submitBtn.click();
    }

    @Step("Default image should be")
    public PhotoComponent defaultImageShouldBeVisible() {
        templatePhotoImg.shouldBe(visible);
        return this;
    }

    @Step("'Save' button should be disabled")
    public void saveBtnShouldNotBeVisible() {
        submitBtn.shouldBe(disabled);
    }
    //endregion

    //region EditPhoto

    @Step("Tap on the 'Edit' button")
    public PhotoComponent editPhoto() {
        editPhotoBtn.click();
        return this;
    }

    public void deletePhoto() {
        AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        softstep.add("Tap on the 'Delete' button", () -> deletePhotoBtn.click());
        softstep.add("Tap on the 'Delete' button", () ->  submitBtn.click());

        softstep.execute();
    }
    //endregion

    //region Country
    @Step("Tap on the 'Country drop down list' for photo")
    public PhotoComponent addCountry(String country) {
        countryDropDown.click();
        countriesList.find(exactText(country)).click();
        return this;
    }

    @Step("Photo place should be earlier selected country for photo")
    public PhotoComponent photoPlaceShouldBeSelectedCountry(String country) {
        photoPlace.shouldHave(text(country));
        return this;
    }
    //endregion

    //region Description
    @Step("Tap on the 'Description' for photo")
    public PhotoComponent setDescription(String description) {
        descriptionInput.setValue(description);
        return this;
    }

    @Step("Description should be earlier inputted text for photo")
    public PhotoComponent descriptionShouldBeInputtedText(String description) {
        photoDescription.shouldHave(text(description));
        return this;
    }

    @Step("Description should be empty")
    public PhotoComponent descriptionShouldBeEmpty() {
        photoDescription.shouldBe(empty);
        return this;
    }
    //endregion

    //region Close btn
    @Step("Tap on the 'Close' button Edit photo mode")
    public  <T> T closeEditPhotoView(T tab) {
        closeEditPhotoViewBtn.click();
        return tab;
    }

    @Step("'Photo edit view' should not be visible")
    public void verifyUploadPhotoModalWindowIsClosed() {
        self.shouldNotBe(visible);
    }
    //endregion
}
