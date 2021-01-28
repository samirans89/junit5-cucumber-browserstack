Junit 5 Cucumber Browserstack
==============================

<a href="https://junit.org/junit5/"><img src="https://junit.org/junit5/assets/img/junit5-logo.png" alt="JUnit5" height="43" /></a>
<a href="https://cucumber.io/"><img src="https://brandslogos.com/wp-content/uploads/images/large/cucumber-logo.png" alt="Cucumber" height="43" /></a>
<a href="https://browserstack.com"><img src="https://d98b8t1nnulk5.cloudfront.net/production/images/layout/logo-header.png?1469004780" alt="Browserstack" width="188" height="43" /></a>

JUnit 5 with Cucumber Integration with BrowserStack.

## Setup
* Clone the repo
* Install dependencies `mvn install`
* Update *.conf.json files inside the `src/test/resources/conf` directory with your [BrowserStack Username and Access Key](https://www.browserstack.com/accounts/settings).

## Running your tests
* Run Tests with single browsers
  - Single Browser Parallel Scenarios: `mvn install -P single` or `gradle test`
  - Single Browser Without Parallel Scenarios: `mvn install -P single -Dparallel-scenarios-execution-enabled=false`
* Parallel Tests: `mvn compile exec:java -P parallel` or `gradle parallel_test`
* Local Tests: `mvn compile exec:java -P local` or `gradle local_test`

Understand how many parallel sessions you need by using our [Parallel Test Calculator](https://www.browserstack.com/automate/parallel-calculator?ref=github)

## Notes
* You can view your test results on the [BrowserStack Automate dashboard](https://www.browserstack.com/automate)
* To test on a different set of browsers, check out our [platform configurator](https://www.browserstack.com/automate/java#setting-os-and-browser)
* You can export the environment variables for the Username and Access Key of your BrowserStack account.

  ```shell
  export BROWSERSTACK_USERNAME=<browserstack-username> &&
  export BROWSERSTACK_ACCESS_KEY=<browserstack-access-key>
  ```

## Addtional Resources
* [Documentation for writing Automate test scripts in Java](https://www.browserstack.com/automate/java)
* [Customizing your tests on BrowserStack](https://www.browserstack.com/automate/capabilities)
* [Browsers & mobile devices for selenium testing on BrowserStack](https://www.browserstack.com/list-of-browsers-and-platforms?product=automate)
* [Using REST API to access information about your tests via the command-line interface](https://www.browserstack.com/automate/rest-api)
