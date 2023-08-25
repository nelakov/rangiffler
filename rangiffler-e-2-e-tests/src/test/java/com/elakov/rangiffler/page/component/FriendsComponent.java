package com.elakov.rangiffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.elakov.rangiffler.model.UserJson;
import com.elakov.rangiffler.page.BaseComponent;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.elakov.rangiffler.condition.FriendsCondition.friends;

public class FriendsComponent extends BaseComponent<FriendsComponent> {

    public FriendsComponent() {
        super($(".MuiPaper-root.MuiCard-root"));
    }

    private final SelenideElement closeIcon = $("svg[data-testid='CloseIcon']");
    private final ElementsCollection friendsTable = self.$$("tbody tr");
    private final String removeFriendBtn = "button[aria-label='Remove friend']";
    private final SelenideElement deleteBtn = $(byText("Delete"));
    private final SelenideElement noFriendsYetLabel = $(byText("No friends yet"));

    private final SelenideElement infoMessage = $("div[role='alert']");

    @Override
    @Step("'Friends' component displayed and close icon visible")
    public FriendsComponent checkThatComponentDisplayed() {
        closeIcon.shouldBe(Condition.visible);
        return null;
    }

    @Step("Friend's list contain friends")
    public FriendsComponent checkTableContainsFriends(UserJson...friend) {
        friendsTable.shouldHave(friends(friend));
        return this;
    }

    @Step(" 'No friends yet' message should be displayed")
    public void checkTableNotContainsFriends() {
        noFriendsYetLabel.shouldBe(Condition.visible);
    }

    @Step("Remove friend")
    public FriendsComponent removeFriend(String name) {
        friendsTable.filter(text(name)).first().$(removeFriendBtn).click();
        deleteBtn.click();
        return this;
    }

    @Step("Alert message should displayed and contains text")
    public FriendsComponent alertMessageShouldBeDisplayedAndContainsText(String alertMessage) {
        infoMessage.shouldHave(text(alertMessage));
        return this;
    }

}
