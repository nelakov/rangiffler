package com.elakov.rangiffler.step.web;

import com.elakov.rangiffler.data.entity.country.CountryEntity;
import com.elakov.rangiffler.data.repository.country.CountryRepository;
import com.elakov.rangiffler.data.repository.country.CountryRepositoryImpl;
import com.elakov.rangiffler.helper.allure.AllureSoftStepsHelper;
import com.elakov.rangiffler.model.UserJson;
import com.elakov.rangiffler.test.TestContext;

public class ProfileWebStep extends CommonWebStep<ProfileWebStep> {



    public ProfileWebStep addPhotoWithCountryAndDescription(String photoClassPath, String countryCode, String description) {
        AllureSoftStepsHelper softStep = new AllureSoftStepsHelper();
        softStep.add("Add new photo travel",
                () -> travelsTab
                        .openTravelsTab(travelsTab)
                        .getHeader()
                        .addPhoto()
                        .addNewPhoto(photoClassPath)
                        .addCountry(getCountry(countryCode))
                        .addDescription(description)
                        .savePhoto()
        );
        softStep.execute();
        return this;
    }

    public ProfileWebStep checkPhotoInfo(String photoClassPath, String countryCode, String description) {
        AllureSoftStepsHelper softStep = new AllureSoftStepsHelper();

        softStep.add("Check photo",

                () -> travelsTab
                        .refreshPage()
                        .checkThatPageLoaded()
                        .getImagesList()
                        .checkPhotosCount(1)
                        .openPhotoModalView(photoClassPath)
//                        .photoPlaceShouldBeSelectedCountry(getCountry(countryCode))
//                        .descriptionShouldBeInputtedText(description)
                        .getHeader()
                        .checkPhotosCountInHeader(1)
                        .checkCountriesCountInHeader(1)
        );
        softStep.execute();
        return this;
    }

    public ProfileWebStep fillAllFieldsAndAddPhotoToProfile(String img, String country, String description, UserJson userJson) {
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
    }

    public String getCountry(String countryCode) {
        CountryRepository countryRepository = new CountryRepositoryImpl();
        CountryEntity country = countryRepository.findByCode(countryCode);
        return country.getName();
    }

}
