package com.elakov.rangiffler.step.web;

import com.elakov.rangiffler.helper.allure.AllureSoftStepsHelper;
import com.elakov.rangiffler.helper.data.DataFakeHelper;
import com.elakov.rangiffler.test.TestContext;

public class ProfileWebStep extends CommonWebStep<ProfileWebStep> {

    public ProfileWebStep updateProfileAndSave(String avatarClassPath) {
        AllureSoftStepsHelper softStep = new AllureSoftStepsHelper();
        TestContext.setFirstName(DataFakeHelper.generateRandomFunnyUsername());
        TestContext.setSurName(DataFakeHelper.generateRandomSurname());

        softStep.add("Add Profile photo and fill Profile info",
                () -> travelsTab
                        .openTravelsTab(travelsTab)
                        .getHeader()
                        .openProfile()
                        .checkThatComponentDisplayed()
                        .addNewPhotoToProfile(avatarClassPath)
//                        .inputFirstName(TestContext.getFirstName())
//                        .inputLastName(TestContext.getSurName())
                        .saveProfile()
        );
        softStep.execute();
        return this;
    }

    public ProfileWebStep updateProfileWithoutSave(String avatarClassPath) {
        AllureSoftStepsHelper softStep = new AllureSoftStepsHelper();


        softStep.add("Add Profile photo and fill Profile info",
                () -> travelsTab
                        .openTravelsTab(travelsTab)
                        .getHeader()
                        .openProfile()
                        .checkThatComponentDisplayed()
                        .addNewPhotoToProfile(avatarClassPath)
                        .inputFirstName(TestContext.getFirstName())
                        .inputLastName(TestContext.getSurName())
        );
        softStep.execute();
        return this;
    }

    public ProfileWebStep updateProfileAndSaveWithoutUpdatePhoto() {
        AllureSoftStepsHelper softStep = new AllureSoftStepsHelper();
        TestContext.setFirstName(DataFakeHelper.generateRandomFunnyUsername());
        TestContext.setSurName(DataFakeHelper.generateRandomSurname());

        softStep.add("Add Profile photo and fill Profile info",
                () -> travelsTab
                        .openTravelsTab(travelsTab)
                        .getHeader()
                        .openProfile()
                        .checkThatComponentDisplayed()
                        .inputFirstName(TestContext.getFirstName())
                        .inputLastName(TestContext.getSurName())
                        .saveProfile()
        );
        softStep.execute();
        return this;
    }

    public ProfileWebStep checkProfileInfoAndAvatarAfterUpdate(String avatarClassPath) throws InterruptedException {
        AllureSoftStepsHelper softStep = new AllureSoftStepsHelper();
        Thread.sleep(5000);
        softStep.add("Open Profile and check photo and info",
                () -> travelsTab
                        .refreshPage()
                        .getHeader()
                        .openProfile()
                        .checkThatComponentDisplayed()
                        .avatarShouldExist(avatarClassPath)
                        .firstNameShouldHaveInputtedName(TestContext.getFirstName())
                        .lastNameShouldHaveInputtedName(TestContext.getSurName())
        );
        softStep.execute();
        return this;
    }

    public ProfileWebStep profileShouldBeEmpty() {
        AllureSoftStepsHelper softStep = new AllureSoftStepsHelper();

        softStep.add("Open Profile. Profile info should be empty and hasn't photo",
                () -> travelsTab
                        .refreshPage()
                        .getHeader()
                        .openProfile()
                        .checkThatComponentDisplayed()
                        .defaultImageShouldBeVisible()
                        .firstnameShouldBeEmpty()
                        .lastnameShouldBeEmpty()
        );
        softStep.execute();
        return this;
    }

    public ProfileWebStep saveBtnShouldNotVisible() {

        profileComponent.saveBtnShouldNotBeVisible();

        return this;
    }

    public ProfileWebStep saveBtnShouldNotVisibleWithoutUpdates() {
        AllureSoftStepsHelper softStep = new AllureSoftStepsHelper();

        softStep.add("'Save button' should not visible without any updates in 'Profile'",

                () -> travelsTab
                        .refreshPage()
                        .getHeader()
                        .openProfile()
                        .saveBtnShouldNotBeVisible()
        );
        softStep.execute();
        return this;
    }

    public ProfileWebStep errorMessageForFirstNameShouldBeVisible(String errorMessage) {
        AllureSoftStepsHelper softStep = new AllureSoftStepsHelper();


        softStep.add("Update Profile with firstname 51 symbols",
                () -> travelsTab
                        .openTravelsTab(travelsTab)
                        .getHeader()
                        .openProfile()
                        .checkThatComponentDisplayed()
                        .inputFirstName(TestContext.getFirstName())
                        .errorMessageForFirstNameShouldBeDisplayed(errorMessage)
        );
        softStep.execute();
        return this;

    }

    public ProfileWebStep errorMessageForLastNameShouldBeVisible(String errorMessage) {
        AllureSoftStepsHelper softStep = new AllureSoftStepsHelper();


        softStep.add("Update Profile with lastname 51 symbols",
                () -> travelsTab
                        .openTravelsTab(travelsTab)
                        .getHeader()
                        .openProfile()
                        .checkThatComponentDisplayed()
                        .inputLastName(TestContext.getSurName())
                        .errorMessageForLastNameShouldBeDisplayed(errorMessage)
        );
        softStep.execute();
        return this;

    }
}
