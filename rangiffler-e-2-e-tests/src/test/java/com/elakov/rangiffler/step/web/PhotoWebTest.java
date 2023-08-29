package com.elakov.rangiffler.step.web;

public class PhotoWebTest extends CommonWebStep<PhotoWebTest> {

/*    public ProfileWebStep fillAllFieldsAndAddPhotoToProfile(String img, String country, String description, UserJson userJson) {
        AllureSoftStepsHelper softStep = new AllureSoftStepsHelper();
        TestContext.setFirstName(userJson.getFirstName());
        TestContext.setSurName(userJson.getSurname());

        softStep.add("Add info to profile",
                () -> travelsTab
                        .openTravelsTab(travelsTab)
                        .getHeader()
                        .openProfile()
                        .checkThatComponentDisplayed()
                        .inputFirstName(TestContext.getFirstName())
                        .inputLastName(TestContext.getSurName())
                        .addNewPhotoToProfile(img)
                        .saveProfile()
        );
        softStep.execute();
        return this;
    }

    public ProfileWebStep checkProfileInfo() {
        AllureSoftStepsHelper softStep = new AllureSoftStepsHelper();

        softStep.add("Check profile",

                () -> travelsTab
                        .refreshPage()
                        .getHeader()
                        .openProfile()
                        .checkThatComponentDisplayed()
                        .firstNameShouldHaveInputtedName(TestContext.getFirstName())
                        .lastNameShouldHaveInputtedName(TestContext.getSurName())
        );
        softStep.execute();
        return this;
    }*/
}
