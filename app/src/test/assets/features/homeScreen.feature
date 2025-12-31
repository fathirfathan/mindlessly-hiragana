Feature: Home screen
  Background:
    Given viewmodel is set up

  Scenario: User open home screen
    When user progress is himikase
    Then category up to `himikase` is unlocked
    And `Test All Learned` category is unlocked
    And category after `himikase` is locked