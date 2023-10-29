@UAT
Feature: Demo 1


  Scenario: This is scenario 1 : 1
    Given Case scenario datatable
      | Data | UserName | Password   |
      | 1234 | user123  | userpwd123 |
    When scenario condition 1 : 1
    Then scenario assertion 1 : 1

  Scenario Outline: This is scenario 1 : 2
    Given Case scenario datatable
      | Data   | UserName   | Password   |
      | <Data> | <UserName> | <Password> |
    When scenario condition 1 : 2
    Then scenario assertion 1 : 2

    @Sanity
    Examples:
      | Data | UserName | Password   |
      | 1234 | user123  | userpwd123 |
      | 1234 | user123  | userpwd123 |
      | 1234 | user123  | userpwd123 |
      | 1234 | user123  | userpwd123 |
      | 1234 | user123  | userpwd123 |

    @UAT
    Examples:
      | Data | UserName | Password   |
      | 1234 | user123  | userpwd123 |
      | 1234 | user123  | userpwd123 |


  Scenario: This is scenario 1 : 3
    Given Case scenario 1 : 3
    When scenario condition 1 : 3
    Then scenario assertion 1 : 3
