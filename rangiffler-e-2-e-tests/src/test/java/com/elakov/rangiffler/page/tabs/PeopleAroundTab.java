package com.elakov.rangiffler.page.tabs;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.elakov.rangiffler.page.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class PeopleAroundTab extends BasePage<PeopleAroundTab> {

    private final SelenideElement usersTable = $("tbody");
    private final ElementsCollection userTableRows = usersTable.$$("tr");
    private final String addFriendIcon = "button[aria-label='Add friend']";
    private final String acceptInvitationIcon = "button[aria-label='Accept invitation']";
    private final String declineInvitationIcon = "button[aria-label='Decline invitation']";
    private final String removeFriendIcon = "button[aria-label='Remove friend']";
    private final SelenideElement declineInvitationButton = $(byText("Decline"));
    private final SelenideElement infoMessage = $("div[role='alert']");
    private final SelenideElement deleteFriendBtn = $(byText("Delete"));

    @Override
    public PeopleAroundTab checkThatPageLoaded() {
        usersTable.shouldBe(visible);
        return null;
    }

    //region Invitation condition
    @Step("Send invitation")
    public PeopleAroundTab sendInvitation(String friendName) {
        userTableRows.find(text(friendName)).$(addFriendIcon).click();
        return this;
    }

    @Step("Accept invitation")
    public PeopleAroundTab acceptInvitation(String friendName) {
        userTableRows.find(text(friendName)).scrollTo().$(acceptInvitationIcon).click();
        return this;
    }

    @Step("Decline invitation")
    public PeopleAroundTab declineInvitation(String friendName) {
        userTableRows.find(text(friendName)).scrollTo().$(declineInvitationIcon).click();
        declineInvitationButton.click();
        return this;
    }

    @Step("Remove friend")
    public PeopleAroundTab removeFriend(String friendName) {
        userTableRows.find(text(friendName)).scrollTo().$(removeFriendIcon).click();
        deleteFriendBtn.click();
        return this;
    }

    //endregion

    //region Icon condition
    @Step("Add friend icon should be displayed")
    public PeopleAroundTab addFriendIconShouldBeDisplayed(String username) {
        userTableRows.find(text(username)).scrollTo().$(addFriendIcon).shouldBe(visible);
        return this;
    }

    @Step("Delete icon should be displayed")
    public PeopleAroundTab deleteIconShouldBeDisplayed(String username) {
        userTableRows.find(text(username)).scrollTo().$(removeFriendIcon).shouldBe(visible);
        return this;
    }

    @Step("Invitation sent icon should be visible")
    public PeopleAroundTab invitationSentIconShouldBeVisible(String username, int index) {
        userTableRows.find(text(username))
                .scrollTo()
                .$$("td")
                .get(index)
                .shouldHave(text("Invitation sent"));
        return this;
    }

    @Step("Alert message should displayed and contains text")
    public PeopleAroundTab alertMessageShouldBeDisplayedAndContainsText(String alertMessage) {
        infoMessage.shouldHave(text(alertMessage));
        return this;
    }
    //endregion
}
