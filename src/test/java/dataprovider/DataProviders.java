package dataprovider;

import com.google.gson.reflect.TypeToken;
import datamodel.UserRegisterDataModel;
import lib.DataReaders;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Stream;
import java.lang.reflect.Method;

public class DataProviders {

    private static final Type type = new TypeToken<List<UserRegisterDataModel>>() {
    }.getType();

    @DataProvider(name = "provideUserRegisterPositiveData")
    public Object[] provideUserRegisterPositiveData() {
        return DataReaders.readJson("UserRegisterPositiveData.json", type).toArray();
    }

    @DataProvider(name = "provideUserLoginPositiveData")
    public Object[] provideUserLoginPositiveData() {
        return DataReaders.readJson("UserLoginPositiveData.json", type).toArray();
    }

    @DataProvider(name = "provideTakeUserInfoNegativeData")
    public Object[]  provideTakeUserInfoNegativeData() {
        return DataReaders.readJson("UserInfoRequestNegativeData.json", type).toArray();
    }

    @DataProvider(name = "provideChangeUserInfoNegativeData")
    public Object[]  provideChangeUserInfoNegativeData() {
        return DataReaders.readJson("UserChangeRequestNegativeData.json", type).toArray();
    }

    @DataProvider(name = "provideUserDeleteRequestData")
    public Object[]  provideDeleteUserData() {
        return DataReaders.readJson("UserDeleteRequestData.json", type).toArray();
    }

    @DataProvider(name = "provideUserRegisterNegativeData")
    public  Object[]  provideUserRegisterNegativeData() {
        return DataReaders.readJson("UserRegisterNegativeData.json", type).toArray();
    }
}