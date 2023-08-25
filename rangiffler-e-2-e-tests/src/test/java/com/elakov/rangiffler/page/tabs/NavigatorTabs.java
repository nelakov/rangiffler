package com.elakov.rangiffler.page.tabs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.elakov.rangiffler.helper.allure.AllureSoftStepsHelper;
import com.elakov.rangiffler.page.BaseComponent;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;

public class NavigatorTabs extends BaseComponent<NavigatorTabs> {

    public NavigatorTabs() {
        super($("div[aria-label='Tabs']"));
    }
    private final SelenideElement travelsTab = self.$(byText("Your travels"));
    private final SelenideElement friendsTravelsTab = self.$(byText("Friends travels"));
    private final SelenideElement peopleAroundTab = self.$(byText("People Around"));

    @Override
    public NavigatorTabs checkThatComponentDisplayed() {
        AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        return step("Check the 'Navigator tab' was loaded", () -> {
            softstep.add("Check 'Your travels tab' visible", () -> {
                travelsTab.shouldBe(Condition.visible);

            });
            softstep.add("Check 'Friends travels tab' visible", () -> {
                friendsTravelsTab.shouldBe(Condition.visible);

            });
            softstep.add("Check 'People around tab' visible", () -> {
                peopleAroundTab.shouldBe(Condition.visible);
            });
            softstep.execute();
            return this;
        });
    }

    @Step("Open 'Your travels' tab ")
    public TravelsTab openYourTravelsTab() {
        travelsTab.click();
        return new TravelsTab();
    }

    @Step("Open 'Friends travels' tab")
    public TravelsTab openFriendsTravelsTab() {
        friendsTravelsTab.click();
        return new TravelsTab();
    }

    @Step("Open 'People Around' tab")
    public PeopleAroundTab openPeopleAroundTab() {
        peopleAroundTab.click();
        return new PeopleAroundTab();
    }

}
