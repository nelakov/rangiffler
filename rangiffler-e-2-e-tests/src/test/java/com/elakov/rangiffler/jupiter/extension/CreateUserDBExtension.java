package com.elakov.rangiffler.jupiter.extension;

import com.elakov.rangiffler.data.entity.AuthUserEntity;
import com.elakov.rangiffler.data.entity.Authority;
import com.elakov.rangiffler.data.entity.AuthorityEntity;
import com.elakov.rangiffler.data.jpa.EmfProvider;
import com.elakov.rangiffler.data.repository.authDAO.AuthRepository;
import com.elakov.rangiffler.data.repository.authDAO.AuthRepositoryImpl;
import com.elakov.rangiffler.helpers.AllureSteps;
import com.elakov.rangiffler.jupiter.annotation.GenerateUserWith;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import java.lang.reflect.Parameter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.elakov.rangiffler.jupiter.annotation.GenerateUserWith.ClientDB.HIBERNATE;
import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.create;

public class CreateUserDBExtension implements
        BeforeEachCallback,
        ParameterResolver,
        AfterEachCallback,
        AfterAllCallback {

    public static Namespace USER_DB_NAMESPACE = create(CreateUserDBExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Faker faker = new Faker();
        AuthRepository authRepository = null;
        final String testId = context.getRequiredTestClass() + context.getRequiredTestMethod().toString();
        List<AuthUserEntity> authUserEntities = new ArrayList<>();
        List<Parameter> parameters = Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(GenerateUserWith.class))
                .filter(parameter -> parameter.getType().isAssignableFrom(AuthUserEntity.class))
                .toList();

        for (Parameter parameter : parameters) {
            GenerateUserWith annotation = parameter.getAnnotation(GenerateUserWith.class);
            if (HIBERNATE.equals(annotation.clientDB())) {
                authRepository = new AuthRepositoryImpl();
            }
            AuthUserEntity entity = new AuthUserEntity();
            entity.setUsername("".equals(annotation.username()) ? faker.funnyName().name() : annotation.username());
            entity.setPassword("12345");
            entity.setEnabled(annotation.enabled());
            entity.setAccountNonExpired(annotation.accountNonExpired());
            entity.setAccountNonLocked(annotation.accountNonLocked());
            entity.setCredentialsNonExpired(annotation.credentialsNonExpired());
            entity.setAuthorities(Arrays.stream(Authority.values()).map(
                    a -> {
                        AuthorityEntity authorityEntity = new AuthorityEntity();
                        authorityEntity.setAuthority(a);
                        authorityEntity.setUser(entity);
                        return authorityEntity;
                    }
            ).toList());
            authUserEntities.add(entity);
            authRepository.createUser(entity);
        }
        context.getStore(USER_DB_NAMESPACE).put(testId + "user", authUserEntities);
        context.getStore(USER_DB_NAMESPACE).put(testId + "dao", authRepository);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(AuthUserEntity.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public AuthUserEntity resolveParameter(ParameterContext parameterContext,
                                           ExtensionContext extensionContext) throws ParameterResolutionException {
        final String testId = extensionContext.getRequiredTestClass() + extensionContext.getRequiredTestMethod().toString();
        List<AuthUserEntity> userEntityList = extensionContext.getStore(USER_DB_NAMESPACE).get(testId + "user", List.class);
        return userEntityList.get(parameterContext.getIndex());
    }

    @Override
    public void afterEach(ExtensionContext context) throws SQLException {
        final String testId = context.getRequiredTestClass() + context.getRequiredTestMethod().toString();
        List<AuthUserEntity> userEntityList = context.getStore(USER_DB_NAMESPACE).get(testId + "user", List.class);
        AuthRepository usersDAO = context.getStore(USER_DB_NAMESPACE).get(testId + "dao", AuthRepository.class);
        List<Parameter> parameters = Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(GenerateUserWith.class))
                .toList();

        for (Parameter parameter : parameters) {
            GenerateUserWith annotation = parameter.getAnnotation(GenerateUserWith.class);
            // Удалять ли юзера после каждого теста?
            if (annotation.deleteUserAfterEach()) {
                for (AuthUserEntity userEntity : userEntityList) {
                    usersDAO.removeUser(userEntity);
                }
            }
        }

    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        AllureSteps.step("Connection was closed", () -> {
            EmfProvider.INSTANCE.closeEmf()
                    .forEach(EntityManagerFactory::close);
        });
    }
}
