package com.elakov.rangiffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.elakov.rangiffler.helper.allure.AllureSoftStepsHelper;
import com.elakov.rangiffler.page.BaseComponent;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;

public class MapComponent extends BaseComponent<MapComponent> {

    private final SelenideElement zoomIcon = $("svg[data-testid='ZoomInIcon']");
    private final SelenideElement worldIcon = $("svg.MuiSvgIcon-root.MuiSvgIcon-fontSizeMedium.css-x0sd41-MuiSvgIcon-root[data-testid='PublicIcon']");

    // Map ToolTip
    SelenideElement visitedCountryIcon = $("polygon[fill='black'][visibility='visible']");
    SelenideElement countryToolTip = $("rect[fill='black'][visibility='visible']");
    SelenideElement textsToolTip = $("text[fill='white'][visibility='visible']");

    public MapComponent() {
        super($(".worldmap__figure-container"));
    }

    @Override
    public MapComponent checkThatComponentDisplayed() {
        AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        return step("Check the 'Map component' was loaded", () -> {

            softstep.add("Check that map visible", () -> self.shouldBe(visible));
            softstep.add("Check zoom icon visible", () -> zoomIcon.shouldBe(visible));
            softstep.add("Check world icon visible", () -> worldIcon.shouldBe(visible));

            softstep.execute();
            return this;
        });
    }

    public MapComponent hoverOnTheVisitedCountry() {
        AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        return step("Hover mouse by visited country", () -> {

            softstep.add("Make element visible", () -> executeJavaScript("arguments[0].setAttribute('visibility', 'visible');", visitedCountryIcon));
            softstep.add("Hover mouse", () -> visitedCountryIcon.shouldBe(visible).hover());

            softstep.execute();
            return this;
        });
    }

    @Step("Check text in the Tool Tip")
    public MapComponent hoverOnTheVisitedCountry(String text) {
        countryToolTip.shouldBe(visible);
        textsToolTip.shouldHave(Condition.exactText(text));
        return this;
    }

}
