Feature: End to End Feature

  @Local
  Scenario: End to End Scenario
    When I navigate to "http://localhost:3000"
    And I click on "Sign In" link
    And I type "fav_user" in "user-name"
    And I type "testingisfun99" in "password"
    And I press Log In Button
    And I add two products to cart
    And I click on Buy Button
    And I type "first" in "firstNameInput"
    And I type "last" in "lastNameInput"
    And I type "test address" in "addressLine1Input"
    And I type "test province" in "provinceInput"
    And I type "123456" in Post Code
    And I click on Checkout Button
    And I click on "Orders" link
    Then I should see elements in list

  Scenario: Placeholder
    When I navigate to "https://google.com"

  Scenario: Placeholder Single
    When I navigate to "https://google.com"