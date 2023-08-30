package com.elakov.rangiffler.condition;

import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.CollectionSource;
import com.elakov.rangiffler.model.UserJson;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static java.lang.System.lineSeparator;

@ParametersAreNonnullByDefault
public class FriendsMismatch extends UIAssertionError {
    public FriendsMismatch(CollectionSource collection,
                           List<UserJson> expectedFriends, List<UserJson> actualFriends,
                           @Nullable String explanation, long timeoutMs) {
        super(
                collection.driver(),
                "Friends table mismatch" +
                        lineSeparator() + "Actual: " + actualFriends +
                        lineSeparator() + "Expected: " + expectedFriends +
                        (explanation == null ? "" : lineSeparator() + "Because: " + explanation) +
                        lineSeparator() + "Collection: " + collection.description(),
                expectedFriends, actualFriends,
                timeoutMs);
    }
}
