@Local
Feature: User Feature

  Scenario: Click on Favourites button when not logged in
    When I navigate to "http://localhost:3000"
    And I click on "Sign In" link
    And I type "fav_user" in "user-name"
    And I type "testingisfun99" in "password"
    And I press Log In Button
    Then I should see user "fav_user" logged in

  Scenario: Login as Locked User
    When I navigate to "http://localhost:3000"
    And I click on "Sign In" link
    And I type "locked_user" in "user-name"
    And I type "testingisfun99" in "password"
    And I press Log In Button
    Then I should see "Your account has been locked." as Login Error Message

  Scenario: Login as User with no image loaded
    When I navigate to "http://localhost:3000"
    And I click on "Sign In" link
    And I type "image_not_loading_user" in "user-name"
    And I type "testingisfun99" in "password"
    And I press Log In Button
    Then I should see no image loaded

  Scenario: Login as User with existing Orders
    When I navigate to "http://localhost:3000"
    And I click on "Sign In" link
    And I type "existing_orders_user" in "user-name"
    And I type "testingisfun99" in "password"
    And I press Log In Button
    And I click on "Orders" link
    Then I should see elements in list
