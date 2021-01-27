Feature: Offers Feature

  @Local
  Scenario: Offers for mumbai geo-location
    When I navigate to "http://localhost:3000"
    And I click on "Sign In" link
    And I type "fav_user" in "user-name"
    And I type "testingisfun99" in "password"
    And I press Log In Button
    And I click on "Offers" link
    Then I should see Offer elements
