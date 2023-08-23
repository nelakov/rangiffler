package com.elakov.rangiffler.jupiter.callback.creation;

import com.elakov.rangiffler.data.entity.auth.Authority;
import com.elakov.rangiffler.data.entity.auth.AuthorityEntity;
import com.elakov.rangiffler.data.entity.auth.UserAuthEntity;
import com.elakov.rangiffler.data.jpa.EmfProvider;
import com.elakov.rangiffler.data.repository.auth.AuthRepository;
import com.elakov.rangiffler.data.repository.auth.AuthRepositoryImpl;
import com.elakov.rangiffler.helper.allure.AllureStepHelper;
import com.elakov.rangiffler.helper.data.DataFakeHelper;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateUserInDB;
import com.elakov.rangiffler.jupiter.callback.TestSuiteCallback;
import com.elakov.rangiffler.model.UserJson;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import java.util.Arrays;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;

public class CreateUserInAuthDatabaseCallback implements
        BeforeEachCallback,
        ParameterResolver,
        AfterEachCallback,
        TestSuiteCallback {

    public static Namespace USER_DB_NAMESPACE = create(CreateUserInAuthDatabaseCallback.class);

    private static final AuthRepository authRepository = new AuthRepositoryImpl();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        //TODO: Вынести в общий класс получение testID
        String testID = getTestId(context);
        CreateUserInDB annotation = context.getRequiredTestMethod()
                .getAnnotation(CreateUserInDB.class);

        if (!annotation.handleAnnotation())
            return;

            UserAuthEntity dbUserEntity = new UserAuthEntity();
            dbUserEntity.setUsername("".equals(annotation.username()) ? DataFakeHelper.generateRandomFunnyUsername() : annotation.username());
            dbUserEntity.setPassword("".equals(annotation.password()) ? DataFakeHelper.generateRandomPassword() : annotation.password());
            dbUserEntity.setEnabled(true);
            dbUserEntity.setAccountNonExpired(true);
            dbUserEntity.setAccountNonLocked(true);
            dbUserEntity.setCredentialsNonExpired(true);
            dbUserEntity.setAuthorities(Arrays.stream(Authority.values()).map(
                    a -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setAuthority(a);
                        ae.setUser(dbUserEntity);
                        return ae;
                    }
            ).toList());
            authRepository.createUser(dbUserEntity);

            UserJson user = new UserJson();
            user.setUsername(annotation.username());
            user.setPassword(annotation.password());

            context.getStore(USER_DB_NAMESPACE).put(testID + "entity", dbUserEntity);
            context.getStore(USER_DB_NAMESPACE).put(testID + "user", user);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        //TODO: Вынести в общий класс получение testID
        String testID = getTestId(context);
        UserAuthEntity entity = context.getStore(USER_DB_NAMESPACE).get(testID + "entity", UserAuthEntity.class);
        authRepository.removeUser(entity);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        //TODO: Вынести в общий класс получение testID
        String testID = getTestId(extensionContext);
        return extensionContext.getStore(USER_DB_NAMESPACE).get(testID + "user", UserJson.class);
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
