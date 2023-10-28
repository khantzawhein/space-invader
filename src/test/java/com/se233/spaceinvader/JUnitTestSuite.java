package com.se233.spaceinvader;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@SelectClasses({CharacterActionsTest.class, CharacterTest.class})
@Suite
public class JUnitTestSuite {
}
