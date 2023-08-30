package com.elakov.rangiffler.step.web;

import com.elakov.rangiffler.data.entity.country.CountryEntity;
import com.elakov.rangiffler.data.entity.photo.PhotoEntity;
import com.elakov.rangiffler.data.repository.country.CountryRepository;
import com.elakov.rangiffler.data.repository.country.CountryRepositoryImpl;
import com.elakov.rangiffler.data.repository.photo.PhotoRepository;
import com.elakov.rangiffler.data.repository.photo.PhotoRepositoryImpl;
import com.elakov.rangiffler.helper.allure.AllureSoftStepsHelper;
import io.qameta.allure.Step;

public class PhotoWebStep extends CommonWebStep<PhotoWebStep> {

    public PhotoWebStep addPhotoWithCountryAndDescription(String photoClassPath, String countryCode, String description) {
        AllureSoftStepsHelper softStep = new AllureSoftStepsHelper();
        softStep.add("Add new travel photo",
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

    public PhotoWebStep photoShouldBeVisibleAfterAddingAndHasCountryAndDescription(String photoByClasspath, String countryCode, String description) {
        AllureSoftStepsHelper softStep = new AllureSoftStepsHelper();

        softStep.add("Open personal travel photo and check photo and info",
                () -> travelsTab
                        .refreshPage()
                        .checkThatPageLoaded()
                        .getImagesList()
                        .checkCountOfPhotoList(1)
                        .photoShouldExist(photoByClasspath)
                        .clickByPhoto()
                        .photoPlaceShouldBeSelectedCountry(getCountry(countryCode))
                        .descriptionShouldBeInputtedText(description)
                        .getHeader()
                        .checkPhotosCountInHeader(1)
                        .checkCountriesCountInHeader(1)
        );
        softStep.execute();
        return this;
    }

    // TODO: Оставить в этом классе или вынести?
    @Step("Get country from database by country code")
    public String getCountry(String countryCode) {
        CountryRepository countryRepository = new CountryRepositoryImpl();
        CountryEntity country = countryRepository.findByCode(countryCode);
        return country.getName();
    }

    @Step("Get photo from database by username")
    public String getPhoto(String username) {
        PhotoRepository photoRepository = new PhotoRepositoryImpl();
        PhotoEntity photoEntity = photoRepository.findByUsername(username);
        return photoEntity.getPhoto().toString();
    }

}
