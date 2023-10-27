package com.se233.spaceinvader;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@SelectClasses({CharacterActionsTesting.class, CharacterTesting.class})
@Suite
public class JUnitTestSuite {
}
