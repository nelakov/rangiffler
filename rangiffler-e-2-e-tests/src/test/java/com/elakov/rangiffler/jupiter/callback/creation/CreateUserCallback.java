package com.elakov.rangiffler.jupiter.callback.creation;

import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.jupiter.callback.service.CreateUserService;
import com.elakov.rangiffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.extension.*;

import java.util.Objects;


public class CreateUserCallback implements BeforeEachCallback, ParameterResolver {

    public static ExtensionContext.Namespace CREATE_USER_NAMESPACE = ExtensionContext.Namespace
            .create(CreateUserCallback.class);


    private static final CreateUserService CREATE_USER_SERVICE = new CreateUserService();

    @Override
    public void beforeEach(ExtensionContext context) {
        CreateUser annotation = context.getRequiredTestMethod()
                .getAnnotation(CreateUser.class);

        if (annotation != null) {
            context.getStore(CREATE_USER_NAMESPACE).put(getTestId(context), CREATE_USER_SERVICE.createUser(annotation));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CREATE_USER_NAMESPACE).get(getTestId(extensionContext), UserJson.class);
    }

    private String getTestId(ExtensionContext context) {
        return Objects
                .requireNonNull(context.getRequiredTestMethod().getAnnotation(AllureId.class))
                .value();
    }
}
