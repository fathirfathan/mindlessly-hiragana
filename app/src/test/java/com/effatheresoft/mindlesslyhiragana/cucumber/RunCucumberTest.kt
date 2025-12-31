package com.effatheresoft.mindlesslyhiragana.cucumber

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["src\\test\\assets\\features"],
    glue = ["com.effatheresoft.mindlesslyhiragana.home"],
    plugin = ["pretty"]
)
class HomeScreenCucumberTest {}
