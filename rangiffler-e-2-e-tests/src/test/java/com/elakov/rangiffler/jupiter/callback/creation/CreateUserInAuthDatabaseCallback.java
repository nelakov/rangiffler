package com.elakov.rangiffler.jupiter.callback.creation;

import com.elakov.rangiffler.data.entity.auth.UserAuthEntity;
import com.elakov.rangiffler.data.jpa.EmfProvider;
import com.elakov.rangiffler.data.repository.auth.AuthRepository;
import com.elakov.rangiffler.data.repository.auth.AuthRepositoryImpl;
import com.elakov.rangiffler.helper.allure.AllureStepHelper;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateUserInDB;
import com.elakov.rangiffler.jupiter.callback.TestSuiteCallback;
import com.elakov.rangiffler.jupiter.callback.service.UserService;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;

public class CreateUserInAuthDatabaseCallback implements
        BeforeEachCallback,
        ParameterResolver,
        AfterEachCallback,
        TestSuiteCallback {

    private static final UserService USER_SERVICE = new UserService();
    public static Namespace USER_DB_NAMESPACE = create(CreateUserInAuthDatabaseCallback.class);

    private static final AuthRepository authRepository = new AuthRepositoryImpl();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        //TODO: Вынести в общий класс получение testID
        String testID = getTestId(context);
        List<UserAuthEntity> userAuthEntities = new ArrayList<>();
        CreateUserInDB annotation = context.getRequiredTestMethod()
                .getAnnotation(CreateUserInDB.class);

        if (annotation == null) {
            List<Parameter> parameters = Arrays.stream(context.getRequiredTestMethod().getParameters())
                    .filter(parameter -> parameter.isAnnotationPresent(CreateUserInDB.class))
                    .filter(parameter -> parameter.getType().isAssignableFrom(UserAuthEntity.class))
                    .toList();
            for (Parameter parameter : parameters) {
                annotation = parameter.getAnnotation(CreateUserInDB.class);
                UserAuthEntity userAuthEntity = USER_SERVICE.createUserInDb(annotation);
                userAuthEntities.add(userAuthEntity);
                authRepository.createUser(userAuthEntity);
            }
        } else {
            UserAuthEntity userAuthEntity = USER_SERVICE.createUserInDb(annotation);
            userAuthEntities.add(userAuthEntity);
            authRepository.createUser(userAuthEntity);
        }
        context.getStore(USER_DB_NAMESPACE).put(testID + " : entity : ", userAuthEntities);
        context.getStore(USER_DB_NAMESPACE).put(testID + " : repository : ", authRepository);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        //TODO: Вынести в общий класс получение testID
        String testID = getTestId(context);
        //TODO: Вынести в общий класс получение из стора
        List<UserAuthEntity> userEntityList =
                context.getStore(USER_DB_NAMESPACE)
                        .get(testID + " : entity : ", List.class);
        //TODO: Вынести в общий класс получение из стора
        AuthRepository authRepository =
                context.getStore(USER_DB_NAMESPACE)
                        .get(testID + " : repository : ", AuthRepository.class);

        List<Parameter> parameters = Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(CreateUserInDB.class))
                .toList();

        for (Parameter parameter : parameters) {
            CreateUserInDB annotation = parameter.getAnnotation(CreateUserInDB.class);
            // Удалять ли юзера после каждого теста?
            if (annotation.deleteUserAfterEach()) {
                for (UserAuthEntity userEntity : userEntityList) {
                    authRepository.removeUser(userEntity);
                }
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserAuthEntity.class);
    }

    @Override
    public UserAuthEntity resolveParameter(ParameterContext parameterContext,
                                           ExtensionContext extensionContext) throws ParameterResolutionException {
        //TODO: Вынести в общий класс получение testID
        String testID = getTestId(extensionContext);
        List<UserAuthEntity> userAuthEntities =
                extensionContext
                        .getStore(USER_DB_NAMESPACE)
                        .get(testID + " : entity : ", List.class);

        return userAuthEntities.get(parameterContext.getIndex());
    }

    @Override
    public void afterSuite() {
        AllureStepHelper.step("Connection was closed", () -> {
            EmfProvider.INSTANCE.closeEmf()
                    .forEach(EntityManagerFactory::close);
        });
    }

    private String getTestId(ExtensionContext context) {
        return context.getRequiredTestClass().getSimpleName() + " " +
                context.getRequiredTestMethod().getName();
    }

}
