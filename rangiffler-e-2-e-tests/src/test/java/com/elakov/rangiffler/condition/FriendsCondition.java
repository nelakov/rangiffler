package com.elakov.rangiffler.condition;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.impl.CollectionSource;
import com.elakov.rangiffler.model.UserJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FriendsCondition {

    public static CollectionCondition friends(UserJson... expectedFriends) {
        return new CollectionCondition() {
            @Override
            @ParametersAreNonnullByDefault
            public void fail(CollectionSource collection, @Nullable List<WebElement> elements, @Nullable Exception lastError, long timeoutMs) {
                if (elements == null || elements.isEmpty()) {
                    ElementNotFound elementNotFound = new ElementNotFound(collection, List.of("Can`t find elements"), lastError);
                    throw elementNotFound;
                } else if (elements.size() != expectedFriends.length) {
                    throw new FriendsSizeMismatch(collection, Arrays.asList(expectedFriends), bindElementsToFriends(elements), explanation, timeoutMs);
                } else {
                    throw new FriendsSizeMismatch(collection, Arrays.asList(expectedFriends), bindElementsToFriends(elements), explanation, timeoutMs);
                }
            }

            @Override
            public boolean missingElementSatisfiesCondition() {
                return false;
            }

            @Override
            public boolean test(List<WebElement> elements) {
                if (elements.size() != expectedFriends.length) {
                    return false;
                }
                for (int i = 0; i < expectedFriends.length; i++) {
                    WebElement row = elements.get(i);
                    UserJson expectedFriend = expectedFriends[i];
                    List<WebElement> cells = row.findElements(By.cssSelector("td"));

                    if (!cells.get(1).getText().equals(expectedFriend.getUsername())) {
                        return false;
                    }
                }
                return true;
            }

            private List<UserJson> bindElementsToFriends(List<WebElement> elements) {
                return elements.stream()
                        .map(e -> {
                            List<WebElement> cells = e.findElements(By.cssSelector("td"));
                            UserJson actual = new UserJson();
                            actual.setUsername(cells.get(1).getText());
                            return actual;
                        }).collect(Collectors.toList());
            }
        };
    }
}
