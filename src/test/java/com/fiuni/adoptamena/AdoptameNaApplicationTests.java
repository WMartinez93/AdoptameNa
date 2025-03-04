package com.fiuni.adoptamena;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("AdoptameNa Test Suite")
@SelectClasses({
        com.fiuni.adoptamena.unit.auth.AuthControllerTest.class,
        com.fiuni.adoptamena.unit.auth.JwtServiceTest.class
})
public class AdoptameNaApplicationTests {
}
