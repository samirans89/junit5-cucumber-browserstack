@Local
Feature: Product Feature

  Scenario: Apply Apple Vendor Filter
    When I navigate to "http://localhost:3000"
    And I press the Apple Vendor Filter
    Then I should see 9 items in the list

  Scenario: Apply Lowest to Highest Order By
    When I navigate to "http://localhost:3000"
    And I order by lowest to highest
    Then I should see prices in ascending order