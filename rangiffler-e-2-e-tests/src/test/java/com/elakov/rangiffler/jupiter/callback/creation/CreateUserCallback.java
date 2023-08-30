package com.elakov.rangiffler.jupiter.callback.creation;

import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.jupiter.callback.service.UserService;
import com.elakov.rangiffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CreateUserCallback implements BeforeEachCallback, ParameterResolver {

    public static ExtensionContext.Namespace CREATE_USER_NAMESPACE = ExtensionContext.Namespace
            .create(CreateUserCallback.class);


    private static final UserService CREATE_USER_SERVICE = new UserService();

    @Override
    public void beforeEach(ExtensionContext context) {
        CreateUser annotation = context.getRequiredTestMethod()
                .getAnnotation(CreateUser.class);

        if (annotation == null) {
            List<Parameter> parameters = Arrays.stream(context.getRequiredTestMethod().getParameters())
                    .filter(parameter -> parameter.isAnnotationPresent(CreateUser.class))
                    .filter(parameter -> parameter.getType().isAssignableFrom(UserJson.class))
                    .toList();
            for (Parameter parameter : parameters) {
                annotation = parameter.getAnnotation(CreateUser.class);
                context.getStore(CREATE_USER_NAMESPACE).put(getTestId(context), CREATE_USER_SERVICE.createUserViaApi(annotation));
            }
        } else {
            context.getStore(CREATE_USER_NAMESPACE).put(getTestId(context), CREATE_USER_SERVICE.createUserViaApi(annotation));
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

    //TODO: Вынести в базовый экстеншион
    private String getTestId(ExtensionContext context) {
        return Objects
                .requireNonNull(context.getRequiredTestMethod().getAnnotation(AllureId.class))
                .value();
    }
}
